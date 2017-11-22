package com.ninelives.insurance.api.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.api.model.User;

@Mapper
public interface UserMapper {
	
    @Select({
        "select",
        "user_id, password, email, google_refresh_token, google_auth_code, google_access_token, ",
        "fcm_token, name, gender, birth_date, birth_place, phone, address, id_card_file_id, ",
        "status, created_date, update_date, google_user_id, is_sync_gmail_enabled, is_notification_enabled",
        "from public.users",
    	"where email=#{email,jdbcType=VARCHAR} "
		})
	User selectByEmail(@Param("email") String email);
    
    @Select({
        "select",
        "user_id, password, email, google_refresh_token, google_auth_code, google_access_token, ",
        "fcm_token, name, gender, birth_date, birth_place, phone, address, id_card_file_id, ",
        "status, created_date, update_date, google_user_id, is_sync_gmail_enabled, is_notification_enabled",
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
    
    int insertSelective(User record);
}
