package com.ninelives.insurance.api.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.api.model.PolicyClaim;

@Mapper
public interface PolicyClaimMapper {
	@Insert({
        "insert into public.policy_claim (claim_id, order_id, ",
        "user_id, claim_date, ",
        "incident_date_time, incident_summary, ",
        "status) ",
        "values (#{claimId,jdbcType=VARCHAR}, #{orderId,jdbcType=VARCHAR}, ",
        "#{userId,jdbcType=VARCHAR}, #{claimDate,jdbcType=DATE}, ",
        "#{incidentDateTime,jdbcType=TIMESTAMP}, #{incidentSummary,jdbcType=VARCHAR}, ",
        "#{status,jdbcType=VARCHAR})"
    })
    int insert(PolicyClaim record);    
}