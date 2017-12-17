package com.ninelives.insurance.api.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.model.PolicyOrder;

@Mapper
public interface TestMapper {
	
	@Update({
        "update public.policy_order ",
        "set status = #{status,jdbcType=VARCHAR} ",
        "where user_id = #{userId,jdbcType=VARCHAR} and order_id=#{orderId,jdbcType=VARCHAR}"
    })
	int updateStatus(PolicyOrder order);
}
