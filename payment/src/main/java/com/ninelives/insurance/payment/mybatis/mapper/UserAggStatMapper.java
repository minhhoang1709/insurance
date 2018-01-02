package com.ninelives.insurance.payment.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.model.UserAggStat;

@Mapper
public interface UserAggStatMapper {
    @Insert({
        "insert into public.user_agg_stat (user_id, success_payment_amount) ",
        "values (#{userId,jdbcType=VARCHAR}, #{successPaymentAmount,jdbcType=INTEGER})"
    })
    int insert(UserAggStat record);
    
    @Update({
    	"update public.user_agg_stat ",
    	"set success_payment_amount=#{successPaymentAmount,jdbcType=INTEGER}, ",
    	  "update_date=now() ",
    	"where user_id=#{userId,jdbcType=VARCHAR}"
    })
    int updateByUserId(UserAggStat record);
    
    @Select({
    	"select user_id, success_payment_amount ",
    	"from public.user_agg_stat ",
    	"where user_id=#{userId,jdbcType=VARCHAR}"
    })
    UserAggStat selectByUserId(String userId);
}