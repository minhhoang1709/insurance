package com.ninelives.insurance.core.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;

import com.ninelives.insurance.model.PolicyClaimFamily;

public interface PolicyClaimFamilyMapper {
    @Insert({
        "insert into public.policy_claim_family (id, claim_id, ",
        "sub_id, name, relationship, ",
        "birth_date, gender) ",
        "values (#{id,jdbcType=BIGINT}, #{claimId,jdbcType=VARCHAR}, ",
        "#{subId,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR}, #{relationship,jdbcType=VARCHAR}, ",
        "#{birthDate,jdbcType=DATE}, #{gender,jdbcType=VARCHAR}) "
    })
    int insert(PolicyClaimFamily record);
    
    int insertList(List<PolicyClaimFamily> records);
}