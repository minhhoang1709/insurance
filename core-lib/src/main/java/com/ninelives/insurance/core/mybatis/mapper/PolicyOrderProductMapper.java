package com.ninelives.insurance.core.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.model.PolicyOrderProduct;

@Mapper
public interface PolicyOrderProductMapper {
    @Insert({
        "insert into public.policy_order_product (order_product_id, order_id, ",
        "coverage_id, period_id, ",
        "product_id, product_name, coverage_name, ",
        "coverage_max_limit, coverage_has_beneficiary, ",
        "base_premi, premi)",
        "values (#{orderProductId,jdbcType=BIGINT}, #{orderId,jdbcType=VARCHAR}, ",
        "#{coverageId,jdbcType=VARCHAR}, #{periodId,jdbcType=VARCHAR}, ",
        "#{productId,jdbcType=VARCHAR}, #{productName,jdbcType=VARCHAR}, #{coverageName,jdbcType=VARCHAR}, ",
        "#{coverageMaxLimit,jdbcType=BIGINT}, #{coverageHasBeneficiary,jdbcType=BIT}, ",
        "#{basePremi,jdbcType=INTEGER}, #{premi,jdbcType=INTEGER})"
    })
    int insert(PolicyOrderProduct record);
    
    int insertList(List<PolicyOrderProduct> records);
}