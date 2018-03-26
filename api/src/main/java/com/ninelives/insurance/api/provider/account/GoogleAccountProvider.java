package com.ninelives.insurance.api.provider.account;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.ninelives.insurance.api.dto.RegistrationDto;
import com.ninelives.insurance.core.config.NinelivesConfigProperties;

@Service
public class GoogleAccountProvider implements AccountProvider{
	private static final Logger logger = LoggerFactory.getLogger(GoogleAccountProvider.class);
	
	@Autowired NinelivesConfigProperties config;

	private List<String> clientIds;
	private List<String> googleIss;
	
	@Override
	public String verifyEmail(RegistrationDto registrationDo) {
		logger.debug("Start verify account, registrationDto:<{}>", registrationDo);
		if(registrationDo==null||registrationDo.getGoogleToken()==null){
			return null;
		}
		
        JacksonFactory jacksonFactory = new JacksonFactory();
        GoogleIdTokenVerifier googleIdTokenVerifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), jacksonFactory)
        		.setAudience(clientIds)
        		.build();

        String emailVerify = null;
		try {
			GoogleIdToken token = GoogleIdToken.parse(jacksonFactory, registrationDo.getGoogleToken());
			
			if (googleIdTokenVerifier.verify(token)) {
			    GoogleIdToken.Payload payload = token.getPayload();
			    logger.debug("Success verify account, payload:<{}>", payload);
			    if(payload!=null){
			    	emailVerify = payload.getEmail();
			    }
			}else{
				if (token !=null){
					GoogleIdToken.Payload payload = token.getPayload();
					if(payload!=null){
						logger.error("Failed verify account, payload: <{}>, registrationDto:<{}>", payload, registrationDo);
					}else{
						logger.error("Failed verify account, null payload, registrationDto:<{}>", registrationDo);
					}
				}
			}
		} catch (IOException | GeneralSecurityException e) {
			logger.error("Verify account exception", e);
		} 
        return emailVerify;
	}	
	
	@PostConstruct
	private void init(){
		clientIds = config.getAccount().getGoogleOauth2ClientIds();
		googleIss = config.getAccount().getGoogleIss();
	}

	
}
