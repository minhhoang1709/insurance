package com.ninelives.insurance.core.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.model.EmailLog;

@Mapper
public interface EmailLogMapper {
    @Insert({
        "insert into public.email_log (email, ",
        "param, response_http_status, ",
        "response_http_body)",
        "values (#{email,jdbcType=VARCHAR}, ",
        "#{param,jdbcType=VARCHAR}, #{responseHttpStatus,jdbcType=VARCHAR}, ",
        "#{responseHttpBody,jdbcType=VARCHAR})"
    })
    int insert(EmailLog record);

    @Update({
        "update public.email_log",
        "set email = #{email,jdbcType=VARCHAR},",
          "param = #{param,jdbcType=VARCHAR},",
          "response_http_status = #{responseHttpStatus,jdbcType=VARCHAR},",
          "response_http_body = #{responseHttpBody,jdbcType=VARCHAR},",
          "created_date = #{createdDate,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(EmailLog record);
}