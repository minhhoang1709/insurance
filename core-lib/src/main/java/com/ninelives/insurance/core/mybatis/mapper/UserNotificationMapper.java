package com.ninelives.insurance.core.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.model.UserNotification;

public interface UserNotificationMapper {
    
    int insertSelective(UserNotification record);

    @Select({
        "select ",
        "id, user_id, title, body, action, action_data, created_date ",
        "from public.user_notification ",
        "where user_id = #{userId,jdbcType=VARCHAR} ",
        "order by created_date desc ",
    	"limit #{limit,jdbcType=INTEGER} ",
    	"offset #{offset,jdbcType=INTEGER}"
    })
    List<UserNotification> selectByUserId(@Param("userId") String userId, @Param("limit") int limit, @Param("offset") int offset);

}