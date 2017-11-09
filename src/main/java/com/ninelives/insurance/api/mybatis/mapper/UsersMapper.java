package com.ninelives.insurance.api.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.api.model.Users;

@Mapper
public interface UsersMapper {
	
	@Select("select nextval('user_seq_id_seq')")
	long selectNextUserIdSeqId();
	
	@Insert({
        "insert into public.users (user_id, password, ",
        "email, google_refresh_token, ",
        "google_auth_code, google_access_token, ",
        "name, gender, birth_date, ",
        "birth_place, phone, ",
        "address, id_card_file_id, ",
        "created_date, update_date)",
        "values (#{userId,jdbcType=INTEGER}, #{password,jdbcType=VARCHAR}, ",
        "#{email,jdbcType=VARCHAR}, #{googleRefreshToken,jdbcType=VARCHAR}, ",
        "#{googleAuthCode,jdbcType=VARCHAR}, #{googleAccessToken,jdbcType=VARCHAR}, ",
        "#{name,jdbcType=VARCHAR}, #{gender,jdbcType=VARCHAR}, #{birthDate,jdbcType=DATE}, ",
        "#{birthPlace,jdbcType=VARCHAR}, #{phone,jdbcType=VARCHAR}, ",
        "#{address,jdbcType=VARCHAR}, #{idCardFileId,jdbcType=BIGINT}, ",
        "#{createdDate,jdbcType=TIMESTAMP}, #{updateDate,jdbcType=TIMESTAMP})"
    })
    int insert(Users record);
	
	@Select ({"select user_id, password, email, google_refresh_token, google_auth_code, google_access_token, ", 
		"name, gender, birth_date, birth_place, phone, address, id_card_file_id, created_date, ", 
    	"update_date ",
    	"from public.users ",
    	"where email=#{email,jdbcType=VARCHAR} ",
    	"limit 1"
		})
	Users selectByEmail(@Param("email") String email);
	
}
