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
        "coverage_has_beneficiary, coverage_max_limit, ",
        "premi, created_date, ",
        "update_date)",
        "values (#{orderProductId,jdbcType=BIGINT}, #{orderId,jdbcType=VARCHAR}, ",
        "#{coverageId,jdbcType=VARCHAR}, #{periodId,jdbcType=VARCHAR}, ",
        "#{productId,jdbcType=VARCHAR}, #{coverageName,jdbcType=VARCHAR}, ",
        "#{coverageHasBeneficiary,jdbcType=VARCHAR}, #{coverageMaxLimit,jdbcType=BIGINT}, ",
        "#{premi,jdbcType=INTEGER}, #{createdDate,jdbcType=TIMESTAMP}, ",
        "#{updateDate,jdbcType=TIMESTAMP})"
    })
    int insert(OrderProduct record);
	
	@Update({
        "update public.order_product",
        "set order_id = #{orderId,jdbcType=VARCHAR},",
          "coverage_id = #{coverageId,jdbcType=VARCHAR},",
          "period_id = #{periodId,jdbcType=VARCHAR},",
          "product_id = #{productId,jdbcType=VARCHAR},",
          "coverage_name = #{coverageName,jdbcType=VARCHAR},",
          "coverage_has_beneficiary = #{coverageHasBeneficiary,jdbcType=VARCHAR},",
          "coverage_max_limit = #{coverageMaxLimit,jdbcType=BIGINT},",
          "premi = #{premi,jdbcType=INTEGER},",
          "created_date = #{createdDate,jdbcType=TIMESTAMP},",
          "update_date = #{updateDate,jdbcType=TIMESTAMP}",
        "where order_product_id = #{orderProductId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(OrderProduct record);
	
	@Select({
        "select",
        "order_product_id, order_id, coverage_id, period_id, product_id, coverage_name, ",
        "coverage_has_beneficiary, coverage_max_limit, premi, created_date, update_date",
        "from public.order_product",
        "where order_id = #{orderId,jdbcType=VARCHAR}"
    })
    List<OrderProduct> selectByOrderId(String orderId);
}
