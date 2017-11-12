package com.ninelives.insurance.api.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.api.model.PurchaseOrder;

@Mapper
public interface PurchaseOrderMapper {
	    
	@Select({
        "select",
        "order_id, order_date, user_id, policy_number, policy_start_date, policy_end_date, ",
        "period, total_premi, has_beneficiary, product_count, status, created_date, update_date",
        "from public.purchase_order",
        "where user_id = #{userId,jdbcType=VARCHAR}"
    })
	List<PurchaseOrder> selectByUserId(String userId);
	
	@Select({
        "select",
        "order_id, order_date, user_id, policy_number, policy_start_date, policy_end_date, ",
        "period, total_premi, has_beneficiary, product_count, status, created_date, update_date",
        "from public.purchase_order",
        "where order_id = #{orderId,jdbcType=VARCHAR}"
    })
	PurchaseOrder selectByOrderId(String orderId);
}
