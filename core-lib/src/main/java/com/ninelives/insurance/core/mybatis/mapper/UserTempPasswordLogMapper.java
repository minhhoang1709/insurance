package com.ninelives.insurance.core.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.model.UserTempPasswordLog;

@Mapper
public interface UserTempPasswordLogMapper {
    @Insert({
        "insert into public.user_temp_password_log (user_id, ",
        "email, password, ",
        "old_status, new_status)",
        "values (#{userId,jdbcType=VARCHAR}, ",
        "#{email,jdbcType=VARCHAR}, #{password,jdbcType=VARCHAR}, ",
        "#{oldStatus,jdbcType=VARCHAR}, #{newStatus,jdbcType=VARCHAR}) "
    })
    int insert(UserTempPasswordLog record);

}