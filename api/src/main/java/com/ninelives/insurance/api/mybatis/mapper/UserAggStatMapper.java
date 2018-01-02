package com.ninelives.insurance.api.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserAggStatMapper {
  
    @Select({
    	"select success_payment_amount ",
    	"from public.user_agg_stat ",
    	"where user_id=#{userId,jdbcType=VARCHAR}"
    })
    Integer selecSuccessPaymentAmounttByUserId(String userId);
}