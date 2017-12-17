package com.ninelives.insurance.api.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.model.PolicyOrderBeneficiary;

@Mapper
public interface PolicyOrderBeneficiaryMapper {

    @Insert({
        "insert into public.policy_order_beneficiary (order_id, ",
        "name, phone, email, ",
        "relationship) ",
        "values (#{orderId,jdbcType=VARCHAR}, ",
        "#{name,jdbcType=VARCHAR}, #{phone,jdbcType=VARCHAR}, #{email,jdbcType=VARCHAR}, ",
        "#{relationship,jdbcType=VARCHAR})"
    })
    int insert(PolicyOrderBeneficiary record);
}