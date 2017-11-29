package com.ninelives.insurance.api.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ninelives.insurance.api.model.PolicyClaim;
import com.ninelives.insurance.api.model.PolicyClaimDetailAccident;

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
	
	List<PolicyClaim<PolicyClaimDetailAccident>> selectByUserId(@Param("userId") String userId, @Param("limit") int limit, @Param("offset") int offset);
}