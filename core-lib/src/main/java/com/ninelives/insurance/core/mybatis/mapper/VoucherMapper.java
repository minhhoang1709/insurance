package com.ninelives.insurance.core.mybatis.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.model.Voucher;
import com.ninelives.insurance.model.VoucherProduct;

@Mapper
public interface VoucherMapper {
	Voucher selectById(@Param("voucherId") Integer voucherId);
	Voucher selectByCode(@Param("code") String code);
	Voucher selectByInviteCode(@Param("code") String code);
	
	@Select({
        "select ",
        "id, code, title, subtitle, description ",
        "from public.voucher ",
        "where id = #{id,jdbcType=INTEGER}"
    })
    Voucher selectVoucherForInviteById(@Param("id") Integer id);
	
	@Select({
        "select ",
        "v.id, uiv.code, v.title, v.subtitle, v.description ",
        "from public.user_invite_voucher uiv, public.voucher v",
        "where uiv.user_id = #{userId,jdbcType=VARCHAR} and v.id=uiv.voucher_id"
    })
    Voucher selectVoucherForInviteByUserId(@Param("userId") String userId);
	
	@Update({
		"update public.user_invite_voucher ",
		"set inviter_reward_count=inviter_reward_count+1 ",
		"where code=#{code,jdbcType=VARCHAR} and user_id=#{userId,jdbcType=VARCHAR}"
	})
	int increamentInviterRewardCount(@Param("code") String code, @Param("userId") String userId);
	
	@Update({
		"update public.voucher ",
		"set approve_cnt=approve_cnt+1 ",
		"where id = #{id,jdbcType=INTEGER}"
	})
	int increamentVoucherApproveCount(@Param("id") Integer id);

	@Select({
        "select ",
        "id, code, title, subtitle, description ",
        "from public.voucher ",
        "where code = #{code,jdbcType=VARCHAR} and corporate_client_id = #{corporateClientId,jdbcType=INTEGER} "
    })
    Voucher selectByCodeAndCorporateCliendId(@Param("code") String code, @Param("corporateClientId") Integer corporateClientId);

	@Insert({
        "insert into public.voucher (code, title,subtitle, description,policy_start_date,",
        "policy_end_date, use_start_date,use_end_date,base_premi, total_premi,",
        "has_beneficiary,product_count,period_id,status, voucher_type,",
        "created_date,inviter_reward_limit,max_use,approve_cnt,corporate_client_id) ",
        "values (",
        "#{code,jdbcType=VARCHAR}, #{title,jdbcType=VARCHAR},#{subtitle,jdbcType=VARCHAR}, #{description,jdbcType=VARCHAR},#{policyStartDate,jdbcType=TIMESTAMP},",
        "#{policyEndDate,jdbcType=TIMESTAMP},#{useStartDate,jdbcType=TIMESTAMP}, #{useEndDate,jdbcType=TIMESTAMP},#{basePremi,jdbcType=INTEGER}, #{totalPremi,jdbcType=INTEGER},",
        "#{hasBeneficiary,jdbcType=BOOLEAN},#{productCount,jdbcType=INTEGER}, #{periodId,jdbcType=VARCHAR},#{status,jdbcType=TIMESTAMP}, #{voucherType,jdbcType=VARCHAR},",
        "#{createdDate,jdbcType=TIMESTAMP},#{inviterRewardLimit,jdbcType=INTEGER}, #{maxUse,jdbcType=INTEGER},#{approveCnt,jdbcType=INTEGER}, #{corporateClientId,jdbcType=INTEGER})"
    })
    int insertVoucherSelective(Voucher voucher);

	@Insert({
        "insert into public.voucher_product (voucher_id, product_id,premi, created_date,update_date) ",
        "values (",
        "#{voucherId,jdbcType=INTEGER}, #{productId,jdbcType=VARCHAR},#{premi,jdbcType=INTEGER},",
        "now(),now())"
    })
	int insertVouherProductSelective(VoucherProduct voucherProduct);

	
	@Delete({
        "delete from public.voucher_product",
        "where voucher_id = #{voucherId,jdbcType=INTEGER}"
    })
	int deleteVoucherProductByVoucherId(@Param("voucherId") Integer voucherId);

	List<Voucher> selectAllVoucherByCorporateClientId();
	
	@Select({
		"select (c.coverage_id , c.name) as coverageName from voucher_product a, voucher b, coverage c ", 
		"where a.voucher_id=b.id and substring(a.product_id,2,6)=c.coverage_id",
		"and a.voucher_id=#{id,jdbcType=INTEGER}" 
    })
	List<String> getListCoverages(@Param("id") Integer id);
	
	@Select({
        "select * ",
        "from public.voucher ",
        "where id = #{id,jdbcType=INTEGER}"
    })
    Voucher selectVoucherById(@Param("id") Integer id);

	int updateVoucherSelective(Voucher voucher);

	@Select({
		"select b2breport  from ( ",
		"select corporate_client_name,voucher_code,channel,",
		"count(*) filter (where status='PAID') as order,",
		"count(*) filter (where old_status='APPROVED' and  status='PAID') as  order_confirm,",
		"count(*) filter (where old_status='APPROVED' and  status='ACTIVE') as  active,",
		"count(*) filter (where status='EXPIRED') as  expired ", 
		"from hist_policy_order where voucher_type= 'B2B' ",
		"group by corporate_client_name,voucher_code,channel order by 1 ) as b2breport"
    })
	List<String> getListB2BReport();
	
	@Select({
		"select  (a.claim_id, replace(b.name,',',' '), b.email, c.name, a.status, a.claim_date, a.incident_date_time,a.incident_summary,", 
		"d.accident_address_country, d.accident_address_city, e.account_name, e.account_bank_name, e.account_bank_swift_code,",
		"e.account_number, f.policy_number) as claimList ",
		"from policy_claim a, users b, coverage_category c,policy_claim_detail_accident d,policy_claim_bank_account e, policy_order f ",
		"where a.user_id=b.user_id ",
		"and a.coverage_category_id= c.coverage_category_id ",
		"and a.claim_id=d.claim_id ",
		"and a.claim_id=e.claim_id ",
		"and a.order_id=f.order_id ",
		"order by a.update_date desc "
    })
	List<String> getListClaimManagement();

	@Select({
		"select (a.coverage_id, b.name, a.status) as coverageName from policy_claim_coverage a, coverage b ", 
		"where a.claim_id=#{claimId,jdbcType=VARCHAR} ",
		"and a.coverage_id=b.coverage_id "
    })
	List<String> getListCoveragesByClaimId(@Param("claimId") String claimId);

	@Select({
        "select ",
        "id, code, title, subtitle, description ",
        "from public.voucher ",
        "where code = #{code,jdbcType=VARCHAR} and voucher_type = #{voucherType,jdbcType=VARCHAR} "
    })
    Voucher selectByCodeAndType(@Param("code") String code, @Param("voucherType") String voucherType);

	@Select({
		"select (a.code, a.title, a.period_id, b.name, a.use_start_date, a.use_end_date, ",
		"a.max_use, COALESCE( NULLIF(a.approve_cnt,0) , 0 )) as voucherByType ",
		"from voucher a, period b ", 
		"where a.period_id=b.period_id ",
		"and a.voucher_type= #{voucherType,jdbcType=VARCHAR} order by a.created_date desc"
		
    })
	List<String> selectAllVoucherByVoucherType(@Param("voucherType") String voucherType);

	@Select({
		"select (a.code, a.title, a.period_id, b.name, a.use_start_date, a.use_end_date, ",
		"a.max_use, COALESCE( NULLIF(a.approve_cnt,0) , 0 )) as voucherByType ",
		"from voucher a, period b ", 
		"where a.period_id=b.period_id ",
		"and a.voucher_type=#{voucherType,jdbcType=VARCHAR}",
		"and a.use_start_date >=#{startDate,jdbcType=DATE} ",
		"and a.use_start_date <=#{endDate,jdbcType=DATE} ",
		"order by a.created_date desc"
		
    })
	List<String> selectAllVoucherByVoucherTypeAndDate(@Param("voucherType") String voucherType,
			@Param("startDate") Date startDate,@Param("endDate") Date endDate);

	List<Voucher> selectVoucherByDate(@Param("startDate") String startDate,@Param("endDate") String endDate);


}

	
