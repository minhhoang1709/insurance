package com.ninelives.insurance.core.mybatis.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.model.UserLogin;

@Mapper
public interface UserLoginMapper {
    @Delete({
        "delete from public.user_login",
        "where token_id = #{tokenId,jdbcType=VARCHAR}"
    })
    int deleteByTokenId(String tokenId);

    @Insert({
        "insert into public.user_login (token_id, user_id) ",    
        "values (#{tokenId,jdbcType=VARCHAR}, #{userId,jdbcType=VARCHAR})",        
    })
    int insert(UserLogin record);


    @Select({
        "select",
        "token_id, user_id, created_date",
        "from public.user_login",
        "where token_id = #{tokenId,jdbcType=VARCHAR}"
    })
    UserLogin selectByTokenId(String tokenId);

    @Select({
        "select",
        "token_id, user_id, created_date",
        "from public.user_login",
        "where user_id = #{userId,jdbcType=VARCHAR}"
    })
    UserLogin selectByUserId(String userId);

    @Update({
        "update public.user_login",
        "set token_id = #{tokenId,jdbcType=VARCHAR},",
          "created_date = now()",
        "where user_id = #{userId,jdbcType=VARCHAR}"
    })
    int updateByUserId(UserLogin record);
}