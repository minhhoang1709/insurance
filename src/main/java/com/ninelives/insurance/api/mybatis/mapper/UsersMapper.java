package com.ninelives.insurance.api.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.api.model.Users;

@Mapper
public interface UsersMapper {
	
    @Insert({
        "insert into public.users (user_id, password, ",
        "email, google_refresh_token, ",
        "google_auth_code, google_access_token, ",
        "fcm_token, device_id, ",
        "name, gender, birth_date, ",
        "birth_place, phone, ",
        "address, id_card_file_id, ",
        "status, created_date, ",
        "update_date)",
        "values (#{userId,jdbcType=VARCHAR}, #{password,jdbcType=VARCHAR}, ",
        "#{email,jdbcType=VARCHAR}, #{googleRefreshToken,jdbcType=VARCHAR}, ",
        "#{googleAuthCode,jdbcType=VARCHAR}, #{googleAccessToken,jdbcType=VARCHAR}, ",
        "#{fcmToken,jdbcType=VARCHAR}, #{deviceId,jdbcType=VARCHAR}, ",
        "#{name,jdbcType=VARCHAR}, #{gender,jdbcType=VARCHAR}, #{birthDate,jdbcType=DATE}, ",
        "#{birthPlace,jdbcType=VARCHAR}, #{phone,jdbcType=VARCHAR}, ",
        "#{address,jdbcType=VARCHAR}, #{idCardFileId,jdbcType=BIGINT}, ",
        "#{status,jdbcType=VARCHAR}, #{createdDate,jdbcType=TIMESTAMP}, ",
        "#{updateDate,jdbcType=TIMESTAMP})"
    })
    int insert(Users record);
	
    @Select({
        "select",
        "user_id, password, email, google_refresh_token, google_auth_code, google_access_token, ",
        "fcm_token, device_id, name, gender, birth_date, birth_place, phone, address, ",
        "id_card_file_id, status, created_date, update_date",
        "from public.users",
    	"where email=#{email,jdbcType=VARCHAR} "
		})
	Users selectByEmail(@Param("email") String email);
	
    @Update({
        "update public.users ",
        "set fcm_token = #{fcmToken,jdbcType=VARCHAR}, ",
        	"update_date = now() ",
        "where user_id = #{userId,jdbcType=VARCHAR}"
    })
    int updateFcmTokenByUserId(@Param("userId") String userId, @Param("fcmToken") String fcmToken);    
}
