package com.ninelives.insurance.api.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.UserAggStat;

@Mapper
public interface TestMapper {
	
	@Update({
        "update public.policy_order ",
        "set status = #{status,jdbcType=VARCHAR} ",
        "where user_id = #{userId,jdbcType=VARCHAR} and order_id=#{orderId,jdbcType=VARCHAR}"
    })
	int updateStatus(PolicyOrder order);
	
//	@Update({
//    	"update public.user_agg_stat ",
//    	"set success_payment_amount=#{successPaymentAmount,jdbcType=INTEGER}, ",
//    	  "update_date=now() ",
//    	"where user_id=#{userId,jdbcType=VARCHAR}"
//    })
//    int updateByUserId(UserAggStat record);
	
	@Update({
		"INSERT INTO public.user_agg_stat (user_id, success_payment_amount) ", 
		"VALUES (#{userId,jdbcType=VARCHAR}, #{successPaymentAmount,jdbcType=INTEGER}) ",
		"ON CONFLICT (user_id) DO UPDATE ",
		"SET success_payment_amount = EXCLUDED.success_payment_amount"
	})
	int updateByUserId(UserAggStat record);
}
