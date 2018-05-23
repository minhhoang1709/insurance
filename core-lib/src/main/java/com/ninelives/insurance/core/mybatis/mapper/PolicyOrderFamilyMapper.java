package com.ninelives.insurance.core.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.model.PolicyOrderFamily;

@Mapper
public interface PolicyOrderFamilyMapper {
    @Insert({
        "insert into public.policy_order_family (order_id, ",
        "name, relationship, ",
        "birth_date, gender ",
        "values (#{orderId,jdbcType=VARCHAR}, ",
        "#{name,jdbcType=VARCHAR}, #{relationship,jdbcType=VARCHAR}, ",
        "#{birthDate,jdbcType=DATE}, #{gender,jdbcType=VARCHAR})"
    })
    int insert(PolicyOrderFamily record);  

    int insertList(List<PolicyOrderFamily> records);
}