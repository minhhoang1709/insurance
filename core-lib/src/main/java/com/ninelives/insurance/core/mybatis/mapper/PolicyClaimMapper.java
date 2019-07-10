package com.ninelives.insurance.core.mybatis.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.model.PolicyClaim;
import com.ninelives.insurance.model.PolicyClaimCoverage;
import com.ninelives.insurance.model.PolicyClaimDetailAccident;

@Mapper
public interface PolicyClaimMapper {
	@Insert({
        "insert into public.policy_claim (claim_id, coverage_category_id, order_id, ",
        "user_id, claim_date, ",
        "incident_date_time, incident_summary, ",
        "is_user_claimant, has_family, is_lump_sum, ",
        "status) ",
        "values (#{claimId,jdbcType=VARCHAR},  #{coverageCategoryId,jdbcType=VARCHAR}, #{orderId,jdbcType=VARCHAR}, ",
        "#{userId,jdbcType=VARCHAR}, #{claimDate,jdbcType=DATE}, ",
        "#{incidentDateTime,jdbcType=TIMESTAMP}, #{incidentSummary,jdbcType=VARCHAR}, ",
        "#{isUserClaimant,jdbcType=BIT}, #{hasFamily,jdbcType=BIT}, #{isLumpSum,jdbcType=BIT}, ",
        "#{status,jdbcType=VARCHAR})"
    })
    int insert(PolicyClaim record); 
	
	List<PolicyClaim<PolicyClaimDetailAccident>> selectByUserIdAndStatusSet(@Param("userId") String userId,
			@Param("statusSet") Set<String> statusSet, @Param("limit") int limit, @Param("offset") int offset);

	List<PolicyClaim<PolicyClaimDetailAccident>> selectByUserIdAndOrderIdAndStatusSet(@Param("userId") String userId,
			@Param("orderId") String orderId, @Param("statusSet") Set<String> statusSet, @Param("limit") int limit,
			@Param("offset") int offset);

	PolicyClaim<PolicyClaimDetailAccident> selectByUserIdAndClaimId(@Param("userId") String userId, @Param("claimId") String claimId);
	
	@Select({
        "select * ",
        "from public.policy_claim ",
        "where claim_id = #{claimId,jdbcType=VARCHAR}"
    })
	PolicyClaim selectByClaimId(@Param("claimId") String claimId);
	
	int updatePolicyClaimStatus(PolicyClaim policyClaim);
	
	int updatePolicyClaimCoverageStatus(PolicyClaimCoverage policyClaimCoverage);

	
}