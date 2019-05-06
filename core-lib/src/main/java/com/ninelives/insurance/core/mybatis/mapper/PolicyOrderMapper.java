package com.ninelives.insurance.core.mybatis.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
	
	Boolean selectPaidOrderExists(@Param ("userId") String userId);
	
	List<PolicyOrderCoverage> selectCoverageWithConflictedPolicyDate(@Param ("userId") String userId, 
			@Param ("policyStartDate") LocalDate policyStartDate, 
			@Param ("policyEndDate") LocalDate policyEndDate,
			@Param ("dueOrderDate") LocalDate dueOrderDate,
			@Param ("coverageIds") List<String> coverageIds);
	
	@Insert({
        "insert into public.policy_order (order_id, order_date, order_time, ",
        "user_id, coverage_category_id, ",
        "has_beneficiary, period_id, ",
        "policy_number, policy_start_date, ",
        "policy_end_date, base_premi, total_premi, ",
        "product_count, has_voucher, provider_order_number, ",
        "provider_download_url, status, is_family)",
        "values (#{orderId,jdbcType=VARCHAR}, #{orderDate,jdbcType=DATE}, #{orderTime,jdbcType=TIMESTAMP}, ",
        "#{userId,jdbcType=VARCHAR}, #{coverageCategoryId,jdbcType=VARCHAR}, ",
        "#{hasBeneficiary,jdbcType=BIT}, #{periodId,jdbcType=VARCHAR}, ",
        "#{policyNumber,jdbcType=VARCHAR}, #{policyStartDate,jdbcType=DATE}, ",
        "#{policyEndDate,jdbcType=DATE}, #{basePremi,jdbcType=INTEGER}, #{totalPremi,jdbcType=INTEGER}, ",
        "#{productCount,jdbcType=INTEGER}, #{hasVoucher,jdbcType=BIT}, #{providerOrderNumber,jdbcType=VARCHAR}, ",
        "#{providerDownloadUrl,jdbcType=VARCHAR}, #{status,jdbcType=VARCHAR}, #{isFamily,jdbcType=BIT})"
    })
    int insert(PolicyOrder record);

	PolicyOrder selectByUserIdAndOrderId(@Param("userId") String userId, @Param("orderId") String orderId);
	
	PolicyOrder selectByOrderId(@Param("orderId") String orderId);
	
	List<PolicyOrder> selectByUserId(@Param("userId") String userId, @Param("limit") int limit,
			@Param("offset") int offset, @Param("isHide") Boolean isHide);

	List<PolicyOrder> selectWhereStatusActiveByUserId(@Param("userId") String userId, @Param("limit") int limit,
			@Param("offset") int offset, @Param("isHide") Boolean isHide);

	List<PolicyOrder> selectWhereStatusApprovedByUserId(@Param("userId") String userId, @Param("limit") int limit,
			@Param("offset") int offset, @Param("isHide") Boolean isHide);

	List<PolicyOrder> selectWhereStatusExpiredOrTerminatedByUserId(@Param("userId") String userId,
			@Param("limit") int limit, @Param("offset") int offset, @Param("isHide") Boolean isHide);

	List<PolicyOrder> selectWhereStatusBeforeApprovedByUserId(@Param("userId") String userId, @Param("limit") int limit,
			@Param("offset") int offset, @Param("isHide") Boolean isHide);
	
	PolicyOrder selectWithBeneficiaryByUserIdAndOrderId(@Param("userId") String userId, @Param("orderId") String orderId);
	
	LocalDate selectMaxPolicyEndDateByCoverage(@Param ("userId") String userId, 
			@Param ("policyEndDate") LocalDate policyEndDate,
			@Param ("coverageIds") List<String> coverageIds);
	
	int updateStatusAndProviderResponseByOrderIdSelective(PolicyOrder record);
	
	@Update({
		"update public.policy_order ",
		"set is_hide=#{isHide,jdbcType=BIT} ",
		"where order_id=#{orderId,jdbcType=VARCHAR}" 
	})
	int updateIsHideByOrderId(PolicyOrder record);

	@Select({
     "select",
      "order_id, order_id_map, order_date, user_id, policy_number, policy_start_date, policy_end_date, ",
      "period_id, total_premi, has_beneficiary, product_count, status, created_date, update_date",
      "from public.policy_order",
      "where order_id = #{orderId,jdbcType=VARCHAR}",
      "and user_id = #{userId,jdbcType=VARCHAR}"
	})
	PolicyOrder selectByOrderIdAndUserId(@Param ("orderId") String orderId,@Param ("userId") String userId);
}
