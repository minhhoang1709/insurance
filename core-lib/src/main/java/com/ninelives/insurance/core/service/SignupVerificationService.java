package com.ninelives.insurance.core.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.mybatis.mapper.SignupVerificationMapper;
import com.ninelives.insurance.model.SignupVerification;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.SignupVerificationStatus;
import com.ninelives.insurance.ref.SignupVerificationType;
import com.ninelives.insurance.ref.UserRegisterChannel;
import com.ninelives.insurance.ref.UserSource;
import com.ninelives.insurance.ref.UserStatus;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Service
public class SignupVerificationService {
	private static final Logger logger = LoggerFactory.getLogger(SignupVerificationService.class);
	
	@Autowired NinelivesConfigProperties config;
	
	@Autowired EmailService emailService;
	@Autowired UserService userService;
	@Autowired SignupVerificationMapper signupVerificationMapper;
	
	public class TokenClaim {
		public static final String CODE = "code";
		public static final String EMAIL = "email";
	}
	
	SecretKey signKey;
	
	public SignupVerification fetchSignupVerificationByCodeAndType(String verificationCode, SignupVerificationType verificationType) {
		return signupVerificationMapper.selectByVerificationCodeAndType(verificationCode, verificationType);
	}
	
	public List<SignupVerification> fetchSignupVerificationByEmailAndType(String email, SignupVerificationType verificationType) {
		return signupVerificationMapper.selectByVerificationEmailAndType(email, verificationType);
	}
	
	public SignupVerification fetchActiveSignupVerificationByEmailAndType(String email,
			SignupVerificationType verificationType) {
		SignupVerification verification = signupVerificationMapper.selectByVerificationEmailAndTypeAndStatusAndPeriodInHour(email,
				verificationType, SignupVerificationStatus.ACTIVE, config.getAccount().getEmailVerificationActiveHours());
		return verification;
	}

	public SignupVerification signupRequest(String email, String password, UserSource regSource, UserRegisterChannel regChannel) throws AppException{
		String code = "email"+"-"+UUID.randomUUID().toString();
		
		SignupVerification signupVerification = new SignupVerification();
		signupVerification.setEmail(email);
		signupVerification.setPassword(DigestUtils.sha1Hex(password));
		signupVerification.setVerificationCode(code);
		signupVerification.setVerificationType(SignupVerificationType.EMAIL_LINK);
		signupVerification.setRegSource(regSource);
		signupVerification.setRegChannel(regChannel);
		signupVerification.setStatus(SignupVerificationStatus.ACTIVE);

		signupVerification.setVerificationToken(buildToken(signupVerification)); 
		
		emailService.sendSignupEmail(signupVerification);
		
		signupVerificationMapper.insert(signupVerification);				
		
		return signupVerification;
	}
	
	public void verifyEmail(String token) throws AppException{
		logger.info("Verify token:<{}>", token);
		
		if(StringUtils.isEmpty(token)) {
			logger.info("Verify token error, error:<{}>, token:<{}>", "empty token", token);
			throw new AppBadRequestException(ErrorCode.ERR3201_VALIDATE_EMAIL_TOKEN_ERROR, "Invalid token");
		}
		Jws<Claims> jwsClaims;
		try {
			jwsClaims = Jwts.parser().setSigningKey(signKey).parseClaimsJws(token);
		} catch (Exception e) {
			logger.info("Verify token error, error:<{}>, token:<{}>", "parsing token", token);
			logger.debug("Token not valid error <{}>", e.getMessage());
			throw new AppBadRequestException(ErrorCode.ERR3201_VALIDATE_EMAIL_TOKEN_ERROR, "Invalid token");			
		}
		
		if (jwsClaims==null) {
			throw new AppBadRequestException(ErrorCode.ERR3201_VALIDATE_EMAIL_TOKEN_ERROR, "Invalid token");
		}
		
		String codeFromToken = jwsClaims.getBody().get(TokenClaim.CODE, String.class);
		String emailFromToken = jwsClaims.getBody().get(TokenClaim.EMAIL, String.class);
		//System.out.println("claim.code: "+codeFromToken);
		//System.out.println("claim.email: "+emailFromToken);
			
		SignupVerification verification = fetchActiveSignupVerificationByEmailAndType(emailFromToken, SignupVerificationType.EMAIL_LINK);
		//System.out.println("verification: "+verification);
		
		if(verification==null || !verification.getVerificationCode().equals(codeFromToken)) {
			logger.info("Verify token error, error:<{}>, token:<{}>, code:<{}>, email:<{}>",
					"Not found active verification code", token, codeFromToken, emailFromToken);
			throw new AppBadRequestException(ErrorCode.ERR3202_VALIDATE_EMAIL_VERIFY_NOT_FOUND, "Not found active verification code");
		}
		
		//Check the user
		User user  = userService.fetchByEmail(emailFromToken);
		if(user!=null) {
			if(!user.getIsEmailVerified()) {
				user.setPassword(verification.getPassword());
				user.setVerifyDate(LocalDateTime.now());
				user.setVerifySource(UserSource.EMAIL);
				user.setIsEmailVerified(true);
				user.setStatus(UserStatus.ACTIVE);
				
				userService.updateVerificationInfo(user);
				
				logger.info("Verify token success update existing user, token:<{}>, code:<{}>, email:<{}>, user:<{}>",
						token, codeFromToken, emailFromToken, user);
			}else {
				logger.info("Verify token error, error:<{}>, token:<{}>, code:<{}>, email:<{}>",
						"User already verified", token, codeFromToken, emailFromToken);
				throw new AppBadRequestException(ErrorCode.ERR3203_VALIDATE_EMAIL_USER_EXISTS, "User already verified");				
			}
		}else {
			user = new User();
			user.setRegSource(verification.getRegSource());
			user.setRegChannel(verification.getRegChannel());
			user.setVerifySource(UserSource.EMAIL);
			user.setVerifyDate(LocalDateTime.now());
			user.setIsEmailVerified(true);
			user.setUserId(userService.generateUserId());
			user.setEmail(verification.getEmail());
			user.setPassword(verification.getPassword());
			user.setIsSyncGmailEnabled(false);
			user.setIsNotificationEnabled(UserService.DEFAULT_IS_NOTIFICATION_ENABLED);			
			user.setStatus(UserStatus.ACTIVE);

			userService.insertUser(user);
			
			logger.info("Verify token success insert new user, token:<{}>, code:<{}>, email:<{}>, user:<{}>",
					token, codeFromToken, emailFromToken, user);
		}
		
		verification.setStatus(SignupVerificationStatus.VERIFIED);
		verification.setVerifyDate(LocalDateTime.now());
		signupVerificationMapper.updateStatusAndVerifiedDateSelectiveByEmailAndCode(verification);
	}
	
	
	private String buildToken(SignupVerification signupVerification) {
		return Jwts.builder().claim(TokenClaim.CODE, signupVerification.getVerificationCode())
				.claim(TokenClaim.EMAIL,signupVerification.getEmail())
				.setIssuedAt(new Date())
				.signWith(signKey)
				.compact();
	}
	
	@PostConstruct
	public void init() {		
		signKey = Keys.hmacShaKeyFor(config.getAccount().getVerificationSignKey().getBytes());
	}
	
}
