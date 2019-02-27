package com.ninelives.insurance.core.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.model.User;

@Mapper
public interface UserMapper {
	
    @Select({
        "select",
        "user_id, password, email, google_refresh_token, google_auth_code, google_access_token, ",
        "fcm_token, name, gender, birth_date, birth_place, phone, address, id_card_file_id, passport_file_id, ",
        "status, created_date, update_date, google_user_id, is_sync_gmail_enabled, is_notification_enabled, id_card_no, ",
        "reg_channel, reg_source, verify_source, verify_date, is_email_verified ",
        "from public.users",
    	"where email=#{email,jdbcType=VARCHAR} "
		})
	User selectByEmail(@Param("email") String email);
    
    @Select({
        "select",
        "user_id, password, email, google_refresh_token, google_auth_code, google_access_token, ",
        "fcm_token, name, gender, birth_date, birth_place, phone, address, id_card_file_id, passport_file_id, ",
        "status, created_date, update_date, google_user_id, is_sync_gmail_enabled, is_notification_enabled, id_card_no, ",
        "reg_channel, reg_source, verify_source, verify_date, is_email_verified ",
        "from public.users",
    	"where id_card_no=#{idCardNumber,jdbcType=VARCHAR} and email is null"
		})
	User selectByIdCardNumber(@Param("idCardNumber") String idCardNumber);
    
    
    @Select({
        "select",
        "user_id, password, email, google_refresh_token, google_auth_code, google_access_token, ",
        "fcm_token, name, gender, birth_date, birth_place, phone, address, id_card_file_id, passport_file_id, ",
        "status, created_date, update_date, google_user_id, is_sync_gmail_enabled, is_notification_enabled, id_card_no, ",
        "reg_channel, reg_source, verify_source, verify_date, is_email_verified ",
        "from public.users",
        "where user_id = #{userId,jdbcType=VARCHAR}"
    })
	User selectByUserId(@Param("userId") String userId);
	
    @Update({
        "update public.users ",
        "set fcm_token = #{fcmToken,jdbcType=VARCHAR}, ",
        	"update_date = now() ",
        "where user_id = #{userId,jdbcType=VARCHAR}"
    })
    int updateFcmTokenByUserId(@Param("userId") String userId, @Param("fcmToken") String fcmToken);
    
    @Update({
        "update public.users ",
        "set is_sync_gmail_enabled = #{isSyncGmailEnabled,jdbcType=BIT}, ",
        	"update_date = now() ",
        "where user_id = #{userId,jdbcType=VARCHAR}"
    })
    int updateSyncGmailEnabledByUserId(User record);
    
//    @Update({
//        "update public.users",
//        "set name = #{name,jdbcType=VARCHAR},",
//          "gender = #{gender,jdbcType=VARCHAR},",
//          "birth_date = #{birthDate,jdbcType=DATE},",
//          "birth_place = #{birthPlace,jdbcType=VARCHAR},",
//          "phone = #{phone,jdbcType=VARCHAR},",
//          "address = #{address,jdbcType=VARCHAR},",
//          "update_date = now() ",
//        "where user_id = #{userId,jdbcType=VARCHAR}"
//    })
//    int updateProfileByUserId(User record);
    
    @Update({
        "update public.users",
        "set phone = #{phone,jdbcType=VARCHAR},",
          "update_date = now() ",
        "where user_id = #{userId,jdbcType=VARCHAR}"
    })
    int updatePhoneByUserId(@Param("userId")String userId, @Param("phone") String phone);
    
    @Update({
        "update public.users",
        "set id_card_file_id = #{fileId,jdbcType=BIGINT},",
          "update_date = now() ",
        "where user_id = #{userId,jdbcType=VARCHAR}"
    })
    int updateIdCardFileIdByUserId(@Param("userId")String userId, @Param("fileId") Long fileId);
    
    @Update({
        "update public.users",
        "set passport_file_id = #{fileId,jdbcType=BIGINT},",
          "update_date = now() ",
        "where user_id = #{userId,jdbcType=VARCHAR}"
    })
    int updatePassportFileIdByUserId(@Param("userId")String userId, @Param("fileId") Long fileId);
    
    @Update({
        "update public.users",
        "set password = #{password,jdbcType=VARCHAR},",
          "update_date = now() ",
        "where user_id = #{userId,jdbcType=VARCHAR}"
    })
    int updatePasswordByUserId(@Param("userId")String userId, @Param("password") String password);
    
    int insertSelective(User record);

	int updateProfileAndConfigByUserIdSelective(User record);
	
	int updateVerificationByUserIdSelective(User record);
}
