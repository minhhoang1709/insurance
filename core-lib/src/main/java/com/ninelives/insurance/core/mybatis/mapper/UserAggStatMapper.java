package com.ninelives.insurance.core.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.model.UserAggStat;

@Mapper
public interface UserAggStatMapper {
  
    @Select({
    	"select success_payment_amount, voucher_b2b_use_cnt",
    	"from public.user_agg_stat ",
    	"where user_id=#{userId,jdbcType=VARCHAR}"
    })
    UserAggStat selecSuccessPaymentAmountAndB2BUseCntByUserId(String userId);
}