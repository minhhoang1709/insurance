package com.ninelives.insurance.api.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.api.model.OrderProduct;
import com.ninelives.insurance.api.model.PurchaseOrder;

@Mapper
public interface OrderProductMapper {
	@Insert({
        "insert into public.order_product (order_product_id, order_id, ",
        "coverage_id, period_id, ",
        "product_id, coverage_name, ",
        "coverage_max_limit, premi, ",
        "created_date, update_date, ",
        "coverage_has_beneficiary)",
        "values (#{orderProductId,jdbcType=BIGINT}, #{orderId,jdbcType=VARCHAR}, ",
        "#{coverageId,jdbcType=VARCHAR}, #{periodId,jdbcType=VARCHAR}, ",
        "#{productId,jdbcType=VARCHAR}, #{coverageName,jdbcType=VARCHAR}, ",
        "#{coverageMaxLimit,jdbcType=BIGINT}, #{premi,jdbcType=INTEGER}, ",
        "#{createdDate,jdbcType=TIMESTAMP}, #{updateDate,jdbcType=TIMESTAMP}, ",
        "#{coverageHasBeneficiary,jdbcType=BIT})"
    })
    int insert(OrderProduct record);
	
	@Select({
        "select",
        "order_product_id, order_id, coverage_id, period_id, product_id, coverage_name, ",
        "coverage_has_beneficiary, coverage_max_limit, premi, created_date, update_date",
        "from public.order_product",
        "where order_id = #{orderId,jdbcType=VARCHAR}"
    })
    List<OrderProduct> selectByOrderId(String orderId);
}
