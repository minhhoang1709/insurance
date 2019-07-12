package com.ninelives.insurance.core.mybatis.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.model.PolicyClaimCoverage;
import com.ninelives.insurance.model.PolicyOrderProduct;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.model.Voucher;

@Mapper
public interface ReportCmsMapper {
	
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
		"select  (a.claim_id, b.name, b.email, c.name, a.status, a.claim_date, a.incident_date_time,a.incident_summary,", 
		"d.accident_address_country, d.accident_address_city, e.account_name, e.account_bank_name, e.account_bank_swift_code,",
		"e.account_number) as claimList ",
		"from policy_claim a, users b, coverage_category c,policy_claim_detail_accident d,policy_claim_bank_account e ",
		"where a.user_id=b.user_id ",
		"and a.coverage_category_id= c.coverage_category_id ",
		"and a.claim_id=d.claim_id ",
		"and a.claim_id=e.claim_id ",
		"order by a.update_date desc "
    })
	List<String> getListClaimManagement();
	
	@Select({
		"select (a.coverage_id, b.name) as coverageName from policy_claim_coverage a, coverage b ", 
		"where a.claim_id=#{claimId,jdbcType=VARCHAR} ",
		"and a.coverage_id=b.coverage_id "
    })
	List<String> getListCoveragesByClaimId(@Param("claimId") String claimId);
	
	
	@Select({
		"select b2breport  from ( ",
		"select corporate_client_name,voucher_code,channel,",
		"count(*) filter (where status='PAID') as order,",
		"count(*) filter (where old_status='APPROVED' and  status='PAID') as  order_confirm,",
		"count(*) filter (where old_status='APPROVED' and  status='ACTIVE') as  active,",
		"count(*) filter (where status='EXPIRED') as  expired ", 
		"from hist_policy_order ",
		"where hist_date >=#{start,jdbcType=DATE} ",
		"and hist_date <=#{end,jdbcType=DATE} ",
		"and voucher_type= 'B2B' ",
		"group by corporate_client_name,voucher_code,channel order by 1 ) as b2breport"
    })
	List<String> getListB2BReportByDate(@Param("start") Date start, @Param("end") Date end);
	
	@Select({
		"select  (a.claim_id, b.name, b.email, c.name, a.status, a.claim_date, a.incident_date_time,a.incident_summary,", 
		"d.accident_address_country, d.accident_address_city, e.account_name, e.account_bank_name, e.account_bank_swift_code,",
		"e.account_number, f.policy_number) as claimList ",
		"from policy_claim a, users b, coverage_category c,policy_claim_detail_accident d,policy_claim_bank_account e, policy_order f ",
		"where a.user_id=b.user_id ",
		"and a.coverage_category_id= c.coverage_category_id ",
		"and a.claim_id=d.claim_id ",
		"and a.claim_id=e.claim_id ",
		"and a.order_id=f.order_id ",
		"and a.claim_date >=#{start,jdbcType=DATE} ",
		"and a.claim_date <=#{end,jdbcType=DATE} ",
		"order by a.update_date desc "
    })
	List<String> getListClaimByDate(@Param("start") Date start, @Param("end") Date end);
	
	@Select({
		"select (b.file_path, c.name) as documentDetail from policy_claim_document a, user_file b, claim_doc_type c ",
		"where a.file_id=b.file_id ",
		"and a.claim_doc_type_id=c.claim_doc_type_id ",
		"and a.claim_id =#{claimId,jdbcType=VARCHAR}"
    })
	List<String> getDocumentDetail(@Param("claimId") String claimId);
	
	@Select({
		"select trxReportByInsuranceType from ( ",
		"select b.name, ",
		"count(*) filter (where a.status='PAID') as paid, ",
		"count(*) filter (where a.status<>'PAID') as unpaid, ",
		"count(*) filter (where a.old_status='APPROVED' and  a.status='ACTIVE') as  active, ",
		"count(*) filter (where a.status='EXPIRED') as  expired ", 
		"from hist_policy_order a, coverage_category b ",
		"where a.coverage_category_id=b.coverage_category_id ",
		"and a.voucher_type <> 'B2B' ",
		"group by b.name ) as trxReportByInsuranceType "
	})
	List<String> getListTransactionByInsuranceType();
	
	@Select({
		"select trxReportByInsuranceType from ( ",
		"select b.name, ",
		"count(*) filter (where a.status='PAID') as paid, ",
		"count(*) filter (where a.status<>'PAID') as unpaid, ",
		"count(*) filter (where a.old_status='APPROVED' and  a.status='ACTIVE') as  active, ",
		"count(*) filter (where a.status='EXPIRED') as  expired ", 
		"from hist_policy_order a, coverage_category b ",
		"where a.coverage_category_id=b.coverage_category_id ",
		"and a.voucher_type <> 'B2B' ",
		"and a.claim_date >=#{start,jdbcType=DATE} ",
		"and a.claim_date <=#{end,jdbcType=DATE} ",
		"group by b.name ) as trxReportByInsuranceType "
	})
	List<String> getListTransactionByInsuranceTypeAndDate(@Param("start") Date start, @Param("end") Date end);

	
	@Select({
		"select trxReportByDatePeriod from ( ", 
		"select hist_date, ",
		"count(*) filter (where status='PAID') as paid, ", 
		"count(*) filter (where status<>'PAID') as unpaid, ", 
		"count(*) filter (where old_status='APPROVED' and status='ACTIVE') as active, ", 
		"count(*) filter (where status='EXPIRED') as expired ", 
		"from hist_policy_order ",  
		"where voucher_type <> 'B2B' group by hist_date order by 1 desc )", 
		"as trxReportByDatePeriod" 
	})
	List<String> getListTransactionDatePeriod();
	
	@Select({
		"select trxReportByDatePeriod from ( ", 
		"select hist_date, ",
		"count(*) filter (where status='PAID') as paid, ", 
		"count(*) filter (where status<>'PAID') as unpaid, ", 
		"count(*) filter (where old_status='APPROVED' and status='ACTIVE') as active, ", 
		"count(*) filter (where status='EXPIRED') as expired ", 
		"from hist_policy_order ",  
		"where voucher_type <> 'B2B' ",
		"and hist_date >=#{start,jdbcType=DATE} ",
		"and hist_date <=#{end,jdbcType=DATE} ",
		"group by hist_date order by 1  desc )", 
		"as trxReportByDatePeriod" 
	})
	List<String> getListTransactionDatePeriodByDate(@Param("start") Date start, @Param("end") Date end);

	@Select({
		"select claim_coverage_id, claim_id, coverage_id, status, created_date, update_date ",
		"from policy_claim_coverage ",
		"where claim_id=#{claimId,jdbcType=VARCHAR} ",
		"and coverage_id=#{coverageId,jdbcType=VARCHAR}"
	})
	PolicyClaimCoverage getListPolicyCliemCoverageByClaimId(@Param("claimId") String claimId,
			@Param("coverageId") String coverageId);

	@Select({
		"select freeInsuranceReport  from ( ", 
		"select voucher_code, ",
		"count(*) filter (where old_status='APPROVED' and  status='ACTIVE') as  active, ",
		"count(*) filter (where status='EXPIRED') as  expired ", 
		"from hist_policy_order ", 
		"where voucher_type= #{voucherType,jdbcType=VARCHAR} ", 
		"group by voucher_code order by 1 ", 
		") as freeInsuranceReport"
    })
	List<String> getListFreeInsuranceReportByVoucherType(@Param("voucherType") String voucherType);

	
	@Select({
		"select (gender,count(*) ) as chartGender ",  
		"from users where status='ACTIVE' and gender<>'NULL' ",
		"group by gender"
    })
	List<String> getListGenderChart();
	
	@Select({
		"select filterAge from (", 
		"with users as(",
		"select date_part('year',age(birth_date)) as umur ",
		"from users ", 
		"where birth_date is not null )",
		"select ", 
		"count(*) filter (where umur<17) as under17, ",
		"count(*) filter (where umur>=17 and umur<=20) as under20, ",
		"count(*) filter (where umur>21 and umur<=30) as under30, ",
		"count(*) filter (where umur>31 and umur<=40) as under40, ",
		"count(*) filter (where umur>41 and umur<=50) as under50, ",
		"count(*) filter (where umur>51 and umur<=60) as under60, ",
		"count(*) filter (where umur>60) as more60 ", 
		"from users ) as filterAge "
    })
    String getAgeChart();

	@Select({
		"select ageChart from ",
		"( select COALESCE( NULLIF(date_part('year',age(birth_date)),0) , 0 ) as age , count(*) ",
		"from users group by age order by 1 asc ) as ageChart"
    })
	List<String> getAgeNoRangeChart();
	
	@Select({
		"select (count(*) ) as chartGender ",  
		"from users where status='ACTIVE' and gender<>'NULL' "
    })
    String getDemographicCount();

	
	
	@Select({
		"select b2bTrasanction from ",
		"(select a.order_id, a.order_date, b.email, b.name, c.name, d.name, a.policy_start_date, ",
		" a.policy_end_date, a.total_premi, a.product_count, a.status, a.policy_number,COALESCE( NULLIF(a.provider_download_url,'') , '' ) as providerdownload ",
		"from policy_order a, users b, coverage_category c, period d, policy_order_voucher e ",
		"where a.user_id=b.user_id ",
		"and a.coverage_category_id=c.coverage_category_id ",
		"and a.period_id=d.period_id  " +
		"and a.order_id=e.order_id " +
		"and e.voucher_type='B2B' order by a.order_date desc) as b2bTrasanction"
    })
	List<String> getB2bTransaction();
	
	
	@Select({
		"select b2bTrasanction from ",
		"(select a.order_id, a.order_date, b.email, b.name, c.name, d.name, a.policy_start_date, ",
		" a.policy_end_date, a.total_premi, a.product_count, a.status, a.policy_number,COALESCE( NULLIF(a.provider_download_url,'') , '' ) as providerdownload ",
		"from policy_order a, users b, coverage_category c, period d, policy_order_voucher e ",
		"where a.user_id=b.user_id ",
		"and a.coverage_category_id=c.coverage_category_id ",
		"and a.period_id=d.period_id  " +
		"and a.order_id=e.order_id " +
		"and e.voucher_type='B2B' " +
		"and a.order_date >=#{start,jdbcType=DATE} " +
		"and a.order_date <=#{end,jdbcType=DATE} " +
		"order by a.order_date desc) as b2bTrasanction"
    })
	List<String> getB2bTransactionByDate(@Param("start") Date start, @Param("end") Date end);
	
	
	@Select({
		"select product_id, coverage_name, premi,  coverage_max_limit ", 
		"from policy_order_product where order_id=#{orderId,jdbcType=VARCHAR}"
    })
	List<PolicyOrderProduct> getDetailB2bTransaction(@Param("orderId") String orderId);
	
	
	@Select({
		"select b2bTrasanction from ",
		"(select a.order_id, a.order_date, b.email, b.name, c.name, d.name, a.policy_start_date, ",
		" a.policy_end_date, a.total_premi, a.product_count, a.status, a.policy_number,COALESCE( NULLIF(a.provider_download_url,'') , '' ) as providerdownload ",
		"from policy_order a, users b, coverage_category c, period d, policy_order_voucher e ",
		"where a.user_id=b.user_id ",
		"and a.coverage_category_id=c.coverage_category_id ",
		"and a.period_id=d.period_id  " +
		"and a.order_id=e.order_id " +
		"and e.voucher_type <> 'B2B' order by a.order_date desc) as b2bTrasanction"
    })
	List<String> getB2cTransaction();
	
	
	@Select({
		"select b2bTrasanction from ",
		"(select a.order_id, a.order_date, b.email, b.name, c.name, d.name, a.policy_start_date, ",
		" a.policy_end_date, a.total_premi, a.product_count, a.status, a.policy_number,COALESCE( NULLIF(a.provider_download_url,'') , '' ) as providerdownload ",
		"from policy_order a, users b, coverage_category c, period d, policy_order_voucher e ",
		"where a.user_id=b.user_id ",
		"and a.coverage_category_id=c.coverage_category_id ",
		"and a.period_id=d.period_id  " +
		"and a.order_id=e.order_id " +
		"and e.voucher_type <> 'B2B' " +
		"and a.order_date >=#{start,jdbcType=DATE} " +
		"and a.order_date <=#{end,jdbcType=DATE} " +
		"order by a.order_date desc) as b2bTrasanction"
    })
	List<String> getB2cTransactionByDate(@Param("start") Date start, @Param("end") Date end);

	/*@Select({
		"select user_id, COALESCE( NULLIF(name,'') , '' ) as name, " + 
		"COALESCE( NULLIF(email,'') , '' ) as email, " + 
		"gender,birth_date, " +
		"COALESCE( NULLIF(birth_place,'') , '' ) as birth_place, " +
		"COALESCE( NULLIF(phone,'') , '' ) as phone, " +
		"COALESCE( NULLIF(address,'') , '' ) as address, " +
		"COALESCE( NULLIF(id_card_no,'') , '' ) as id_card_no, status " + 
		"from users order by update_date desc "
    })
	List<User> getUsers();*/
	
	
	@Select({
		"select userList from ( " +
		"select user_id,  name,  email, gender,birth_date, " +
		"birth_place, phone, address, id_card_no, status " + 
		"from users order by update_date desc ) as userList"
    })
	List<String> getUsers();
	
	
	@Select({
		"select trxReportByBenefitPeriod from ( " +
		"select b.name, c.name,  " +
		"count(*) filter (where a.status<>'PAID') as unpaid, " + 
		"count(*) filter (where a.status='PAID') as paid, " +
		"count(*) filter (where a.old_status='APPROVED' and a.status='TERMINATED') as active, " + 
		"count(*) filter (where a.old_status='APPROVED' and a.status='ACTIVE') as active,  " +
		"count(*) filter (where a.status='EXPIRED') as expired " +
		"from hist_policy_order a, coverage_category b, period c " +
		"where a.coverage_category_id=b.coverage_category_id " +
		"and a.period_id=c.period_id " +
		"and a.voucher_type <> 'B2B'  " +
		"group by b.name, c.name " +
		")as trxReportByBenefitPeriod"
	})
	List<String> getListTransactionByBenefitPeriod();

	
	
	@Select({
		"select coverageList from ("
		+ " select coverage_id, name from coverage order by coverage_id) as coverageList"
	})
	List<String> getListCoverage();
	
	@Select({
		"select coverageList from ("
		+ " select coverage_id, name ", 
		" from coverage coverage_category_id=#{insuranceTypeid,jdbcType=VARCHAR} order by coverage_id) as coverageList"
	})
	List<String> getListCoverageByInsuranceType(@Param("insuranceTypeid")String insuranceTypeid);
	
	@Select({
		"select periodList from ( select period_id, name from period order by period_id) as periodList"
	})
	List<String> getListPeriod();

	@Select({
		"select insuranceTypeList from ( select coverage_category_id , name from coverage_category order by coverage_category_id) as insuranceTypeList;"
	})
	List<String> getListInsuranceType();

	@Select({
		"select a.id, a.code, a.title, a.subtitle, a.description, a.policy_start_date, ", 
		"a.policy_end_date, a.use_start_date, a.use_end_date, a.base_premi, a.total_premi, a.has_beneficiary, a.product_count, ",
		"a.period_id, a.status, a.voucher_type, a.created_date, a.update_date, a.inviter_reward_limit, a.max_use, a.approve_cnt, ",
		"a.corporate_client_id, b.corporate_client_id, b.corporate_client_name, b.corporate_client_address, b.corporate_client_phone_number, ",
		"b.corporate_client_email, b.corporate_client_provider, b.corporate_client_provider_id, b.created_date, b.created_by, b.update_date, ",
		"b.update_by, c.period_id, c.name, c.value, c.unit, c.created_date, c.update_date, c.status, c.start_value, c.end_value ",
		"from ",
	   	"public.voucher a,public.corporate_client b, public.period c ",
	   	"where a.corporate_client_id=b.corporate_client_id ",  
	   	"and a.period_id=c.period_id   " +
	   	"and to_char(a.created_date, 'yyyy-MM-dd') >= #{startDate,jdbcType=DATE} ",
	   	"and to_char(a.created_date, 'yyyy-MM-dd') <= #{endDate,jdbcType=DATE} ",
	   	"order by a.created_date desc"
	})
    List<Voucher> selectVoucherByDate(@Param("startDate") String startDate,@Param("endDate") String endDate);

	@Select({
		"select insuranceType from ( select distinct substring(b.product_id,2,3), c.name from voucher a ",
		"inner join  public.voucher_product b on b.voucher_id=a.id ",
		"inner join  public.coverage_category c on c.coverage_category_id= substring(b.product_id,2,3) ",
		"where a.id=#{voucherId,jdbcType=INTEGER} group by b.product_id,c.name ) as insuranceType"
    })
	String getInsuranceTypeByVoucherId(@Param("voucherId") Integer integer);

	@Select({
		"select userB2bCode from ( " +
		"select c.name, c.gender, a.order_date, a.order_time, a.user_id, c.email, a.order_id, " +
		"b.voucher_id, b.code, c.phone, c.birth_date, a.status " +
		"from policy_order a, policy_order_voucher b, users c " +
		"where a.order_id=b.order_id " +
		"and a.user_id=c.user_id " +
		"and a.has_voucher=true " +
		"and b.voucher_type='B2B' order by a.created_date desc ) as userB2bCode"
	})
	List<String> getUserB2bByVoucherCode();
	
	
}
