package com.ninelives.insurance.api.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.api.model.PurchaseOrder;

@Mapper
public interface PurchaseOrderMapper {
	
    @Insert({
        "insert into public.purchase_order (order_id, order_date, ",
        "user_id, policy_number, ",
        "policy_start_date, policy_end_date, ",
        "period, total_premi, ",
        "product_count, status, ",
        "created_date, update_date, ",
        "has_beneficiary, coverage_category_id)",
        "values (#{orderId,jdbcType=VARCHAR}, #{orderDate,jdbcType=DATE}, ",
        "#{userId,jdbcType=INTEGER}, #{policyNumber,jdbcType=VARCHAR}, ",
        "#{policyStartDate,jdbcType=DATE}, #{policyEndDate,jdbcType=DATE}, ",
        "#{period,jdbcType=VARCHAR}, #{totalPremi,jdbcType=INTEGER}, ",
        "#{productCount,jdbcType=INTEGER}, #{status,jdbcType=VARCHAR}, ",
        "#{createdDate,jdbcType=TIMESTAMP}, #{updateDate,jdbcType=TIMESTAMP}, ",
        "#{hasBeneficiary,jdbcType=BIT}, #{coverageCategoryId,jdbcType=INTEGER})"
    })
    int insert(PurchaseOrder record);
    
	@Select({
        "select",
        "order_id, order_date, user_id, policy_number, policy_start_date, policy_end_date, ",
        "period, total_premi, has_beneficiary, product_count, status, created_date, update_date",
        "from public.purchase_order",
        "where user_id = #{userId,jdbcType=INTEGER}"
    })
	List<PurchaseOrder> selectByUserId(int userId);
	
	@Select({
        "select",
        "order_id, order_date, user_id, policy_number, policy_start_date, policy_end_date, ",
        "period, total_premi, has_beneficiary, product_count, status, created_date, update_date",
        "from public.purchase_order",
        "where order_id = #{orderId,jdbcType=VARCHAR}"
    })
	PurchaseOrder selectByOrderId(String orderId);
}
