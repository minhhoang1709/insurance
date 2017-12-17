package com.ninelives.insurance.api.mybatis.mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderCoverage;

@Mapper
public interface PolicyOrderMapper {
	    
//	@Select({
//        "select",
//        "order_id, order_date, user_id, policy_number, policy_start_date, policy_end_date, ",
//        "period_id, total_premi, has_beneficiary, product_count, status, created_date, update_date",
//        "from public.policy_order",
//        "where user_id = #{userId,jdbcType=VARCHAR}"
//    })
//	List<PolicyOrder> selectByUserId(String userId);
	
//	@Select({
//        "select",
//        "order_id, order_date, user_id, policy_number, policy_start_date, policy_end_date, ",
//        "period_id, total_premi, has_beneficiary, product_count, status, created_date, update_date",
//        "from public.policy_order",
//        "where order_id = #{orderId,jdbcType=VARCHAR}"
//    })
//	PolicyOrder selectByOrderId(String orderId);
	
	
	List<PolicyOrderCoverage> selectCoverageWithConflictedPolicyDate(@Param ("userId") String userId, 
			@Param ("policyStartDate") LocalDate policyStartDate, 
			@Param ("policyEndDate") LocalDate policyEndDate,
			@Param ("dueOrderDate") LocalDate dueOrderDate,
			@Param ("coverageIds") List<String> coverageIds);
	
	@Insert({
        "insert into public.policy_order (order_id, order_date, ",
        "user_id, coverage_category_id, ",
        "has_beneficiary, period_id, ",
        "policy_number, policy_start_date, ",
        "policy_end_date, total_premi, ",
        "product_count, status)",
        "values (#{orderId,jdbcType=VARCHAR}, #{orderDate,jdbcType=DATE}, ",
        "#{userId,jdbcType=VARCHAR}, #{coverageCategoryId,jdbcType=VARCHAR}, ",
        "#{hasBeneficiary,jdbcType=BIT}, #{periodId,jdbcType=VARCHAR}, ",
        "#{policyNumber,jdbcType=VARCHAR}, #{policyStartDate,jdbcType=DATE}, ",
        "#{policyEndDate,jdbcType=DATE}, #{totalPremi,jdbcType=INTEGER}, ",
        "#{productCount,jdbcType=INTEGER}, #{status,jdbcType=VARCHAR})"
    })
    int insert(PolicyOrder record);

	PolicyOrder selectByUserIdAndOrderId(@Param("userId") String userId, @Param("orderId") String orderId);
	
	List<PolicyOrder> selectByUserId(@Param("userId") String userId, @Param("limit") int limit, @Param("offset") int offset);
	List<PolicyOrder> selectWhereStatusActiveByUserId(@Param("userId") String userId, @Param("limit") int limit, @Param("offset") int offset);
	List<PolicyOrder> selectWhereStatusApprovedByUserId(@Param("userId") String userId, @Param("limit") int limit, @Param("offset") int offset);
	List<PolicyOrder> selectWhereStatusExpiredOrTerminatedByUserId(@Param("userId") String userId, @Param("limit") int limit, @Param("offset") int offset);
	List<PolicyOrder> selectWhereStatusBeforeApprovedByUserId(@Param("userId") String userId, @Param("limit") int limit, @Param("offset") int offset);
	
	PolicyOrder selectWithBeneficiaryByUserIdAndOrderId(@Param("userId") String userId, @Param("orderId") String orderId);
}
