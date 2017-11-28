package com.ninelives.insurance.api.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.api.model.PolicyClaimDetailAccident;

@Mapper
public interface PolicyClaimDetailAccidentMapper {
    @Insert({
        "insert into public.policy_claim_detail_accident (claim_id, accident_address_country, ",
        "accident_address_province, accident_address_city, ",
        "accident_address) ",
        "values (#{claimId,jdbcType=VARCHAR}, #{accidentAddressCountry,jdbcType=VARCHAR}, ",
        "#{accidentAddressProvince,jdbcType=VARCHAR}, #{accidentAddressCity,jdbcType=VARCHAR}, ",
        "#{accidentAddress,jdbcType=VARCHAR})"
    })
    int insert(PolicyClaimDetailAccident record);
    
}