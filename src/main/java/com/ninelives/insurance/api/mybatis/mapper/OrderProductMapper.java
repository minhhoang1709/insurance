package com.ninelives.insurance.api.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.api.model.PolicyOrder;
import com.ninelives.insurance.api.model.PolicyOrderProduct;

@Mapper
public interface OrderProductMapper {
	
	@Select({
        "select",
        "order_product_id, order_id, coverage_id, period_id, product_id, coverage_name, ",
        "coverage_has_beneficiary, coverage_max_limit, premi",
        "from public.policy_order_product",
        "where order_id = #{orderId,jdbcType=VARCHAR}"
    })
    List<PolicyOrderProduct> selectByOrderId(String orderId);
}
