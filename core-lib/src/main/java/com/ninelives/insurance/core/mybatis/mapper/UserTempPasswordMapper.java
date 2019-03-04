package com.ninelives.insurance.core.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.model.UserTempPassword;

@Mapper
public interface UserTempPasswordMapper {

    @Insert({
        "insert into public.user_temp_password (user_id, email, password, ",
        "status, register_date) ",
        "values (#{userId,jdbcType=VARCHAR}, #{email,jdbcType=VARCHAR}, #{password,jdbcType=VARCHAR}, ",
        "#{status,jdbcType=VARCHAR}, #{registerDate,jdbcType=TIMESTAMP}) ",
    })
    int insert(UserTempPassword record);

    @Select({
        "select",
        "user_id, email, password, status, created_date, register_date, update_date, apply_date, replace_date, ",
        "expire_date",
        "from public.user_temp_password",
        "where user_id = #{userId,jdbcType=VARCHAR}"
    })
    UserTempPassword selectByUserId(String userId);

    @Update({
        "update public.user_temp_password",
        "set email = #{email,jdbcType=VARCHAR},",
          "password = #{password,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR},",
          "update_date = #{updateDate,jdbcType=TIMESTAMP},",
          "register_date = #{registerDate,jdbcType=TIMESTAMP},",
          "apply_date = #{applyDate,jdbcType=TIMESTAMP},",
          "replace_date = #{replaceDate,jdbcType=TIMESTAMP},",
          "expire_date = #{expireDate,jdbcType=TIMESTAMP}",
        "where user_id = #{userId,jdbcType=VARCHAR}"
    })
    int updateByUserId(UserTempPassword record);
    
    int updateByUserIdSelective(UserTempPassword record);

}