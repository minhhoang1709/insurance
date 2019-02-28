package com.ninelives.insurance.core.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.model.SignupVerification;
import com.ninelives.insurance.ref.SignupVerificationStatus;
import com.ninelives.insurance.ref.SignupVerificationType;

public interface SignupVerificationMapper {
    @Insert({
        "insert into public.signup_verification (email, ",
        "password, verification_code, ",
        "verification_type, reg_source, ",
        "reg_channel, status)",
        "values (#{email,jdbcType=VARCHAR}, ",
        "#{password,jdbcType=VARCHAR}, #{verificationCode,jdbcType=VARCHAR}, ",
        "#{verificationType,jdbcType=VARCHAR}, #{regSource,jdbcType=VARCHAR}, ",
        "#{regChannel,jdbcType=VARCHAR}, #{status,jdbcType=VARCHAR})"
    })
    int insert(SignupVerification record);

    @Select({
        "select",
        "id, email, password, verification_code, verification_type, reg_source, reg_channel, status, created_date, ",
        "verify_date",
        "from public.signup_verification",
        "where verification_code = #{verificationCode,jdbcType=VARCHAR} and verification_type=#{verificationType,jdbcType=VARCHAR}"
    })
	SignupVerification selectByVerificationCodeAndType(@Param("verificationCode") String verificationCode,
			@Param("verificationType") SignupVerificationType verificationType);
    
    @Select({
        "select",
        "id, email, password, verification_code, verification_type, reg_source, reg_channel, status, created_date, ",
        "verify_date",
        "from public.signup_verification",
        "where email = #{email,jdbcType=VARCHAR} and verification_type=#{verificationType,jdbcType=VARCHAR}"
    })
    List<SignupVerification> selectByVerificationEmailAndType(@Param("email") String email, 
    		@Param("verificationType") SignupVerificationType verificationType);
    
    @Select({
        "select",
        "id, email, password, verification_code, verification_type, reg_source, reg_channel, status, created_date, ",
        "verify_date",
        "from public.signup_verification",
        "where email = #{email,jdbcType=VARCHAR} and verification_type=#{verificationType,jdbcType=VARCHAR}",
        "and status=#{status,jdbcType=VARCHAR}",
        "and created_date >= (now() - (#{hour,jdbcType=INTEGER} * '1 hour'::interval))"
    })
	SignupVerification selectByVerificationEmailAndTypeAndStatusAndPeriodInHour(@Param("email") String email,
			@Param("verificationType") SignupVerificationType verificationType, 
			@Param("status") SignupVerificationStatus status,
			@Param("hour") int hour);

    
    int updateStatusAndVerifiedDateSelectiveByEmailAndCode(SignupVerification record);

}