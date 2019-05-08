package com.ninelives.insurance.payment.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.model.PolicyOrder;

@Mapper
public interface PolicyOrderMapper {

	PolicyOrder selectByOrderId(@Param("orderId") String orderId);
	
	@Update({
        "update public.policy_order",
        "set status = #{status,jdbcType=VARCHAR},",
          "update_date = now ()",
        "where order_id = #{orderId,jdbcType=VARCHAR}"
    })
	int updateStatusByOrderId(PolicyOrder order);
	
	
	PolicyOrder selectByOrderIdMap(@Param ("orderIdMap") String orderIdMap);
	
	
}
