package com.ninelives.insurance.insurer.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.model.PolicyOrder;

@Mapper
public interface PolicyOrderMapper {

	@Update({
        "update public.policy_order",
        "set status = #{status,jdbcType=VARCHAR},",
          "update_date = now ()",
        "where order_id = #{orderId,jdbcType=VARCHAR}"
    })
	int updateStatusByOrderId(@Param("orderId")String orderId, @Param("status") String status);
	
}
