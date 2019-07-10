package com.ninelives.insurance.core.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.model.UserCms;


@Mapper
public interface UserCmsMapper {
	
    @Select({
        "select",
        "user_id, user_name, role_id, password, email, is_active, ",
        "created_date, created_by, modified_date, modified_by",
        "from public.user_cms",
    	"where user_id=#{userId,jdbcType=VARCHAR} "
		})
	UserCms selectByUserId(@Param("userId") String userId);
    
    
    @Insert({
        "insert into public.user_cms (user_id, user_name, ",
        "role_id, password, ",
        "email, is_active, ",
        "created_date, created_by, ",
        "modified_date, modified_by) ",
        "values (#{userId,jdbcType=VARCHAR}, #{userName,jdbcType=VARCHAR}, ",
        "#{roleId,jdbcType=VARCHAR}, #{password,jdbcType=VARCHAR}, ",
        "#{email,jdbcType=VARCHAR}, #{isActive,jdbcType=VARCHAR}, ",
        "#{createdDate,jdbcType=TIMESTAMP}, #{createdBy,jdbcType=VARCHAR}, ",
        "#{modifiedDate,jdbcType=TIMESTAMP}, #{modifiedBy,jdbcType=VARCHAR})"
    })
    int insertSelective(UserCms userCms);
    
   
    
}
