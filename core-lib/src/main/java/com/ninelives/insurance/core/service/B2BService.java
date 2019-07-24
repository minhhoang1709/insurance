package com.ninelives.insurance.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.core.mybatis.mapper.CorporateClientMapper;
import com.ninelives.insurance.core.mybatis.mapper.CoverageCategoryMapper;
import com.ninelives.insurance.core.mybatis.mapper.PeriodMapper;
import com.ninelives.insurance.core.mybatis.mapper.PolicyClaimMapper;
import com.ninelives.insurance.core.mybatis.mapper.ProductMapper;
import com.ninelives.insurance.core.mybatis.mapper.ReportCmsMapper;
import com.ninelives.insurance.core.mybatis.mapper.UserMapper;
import com.ninelives.insurance.core.mybatis.mapper.VoucherMapper;
import com.ninelives.insurance.model.CorporateClient;
import com.ninelives.insurance.model.Coverage;
import com.ninelives.insurance.model.CoverageCategory;
import com.ninelives.insurance.model.Period;
import com.ninelives.insurance.model.PolicyClaim;
import com.ninelives.insurance.model.PolicyClaimCoverage;
import com.ninelives.insurance.model.PolicyOrderProduct;
import com.ninelives.insurance.model.Product;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.model.Voucher;
import com.ninelives.insurance.model.VoucherProduct;

@Service
public class B2BService {
	private static final Logger logger = LoggerFactory.getLogger(B2BService.class);
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	
	@Autowired
	CorporateClientMapper corporateClientMapper;
	
	@Autowired
	VoucherMapper voucherMapper;
	
	@Autowired
	ReportCmsMapper reportCmsMapper;
	
	@Autowired
	PeriodMapper periodMapper;
	
	@Autowired
	ProductMapper productMapper;
	
	@Autowired
	PolicyClaimMapper policyClaimMapper;
	
	@Autowired
	CoverageCategoryMapper coverageCategoryMapper;
	
	
	public int insertCorporateClient(CorporateClient corporateClient){
		return corporateClientMapper.insertSelective(corporateClient);
	}
	
	public CorporateClient selectByCompanyIdAndCorporateId(String companyName, String corporateId){
		return corporateClientMapper.selectByCompanyIdAndCorporateId(companyName,corporateId);
	}

	public List<CorporateClient> fetchAllCorporateClient() {
		return corporateClientMapper.selectAllCorporateClient();
	}

	public CorporateClient getCorporateClientById(String ccId) {
		int id = Integer.parseInt(ccId);
		return corporateClientMapper.selectCorporateClientById(id);
	}

	public int updateCorporateClient(CorporateClient cc) {
		return corporateClientMapper.updateCorporateClientSelective(cc);
	}

	public Voucher selectVoucherByCode(String codeVoucher, String corporateClientId) {
		int id = Integer.parseInt(corporateClientId);
		return voucherMapper.selectByCodeAndCorporateCliendId(codeVoucher,id);
	}

	public int insertVoucher(Voucher voucher) {
		return voucherMapper.insertVoucherSelective(voucher);
		
	}

	public Period selectPeriodById(String periodId) {
		return periodMapper.selectPeriodByPeriodId(periodId);
	}
	
	public String selectProductAndCoverage(String productId1, String productId2, String productId3,
			String productId4, String productId5, String productId6){
		return periodMapper.selectProductAndCoverage(productId1, productId2, productId3,productId4, productId5, productId6);
	}

	public Product selectProductByCoverageAndPeriod(String coverageId, String periodId) {
		return productMapper.selectProductByCoverageIdAndPeriodId(coverageId,periodId);
	}

	public int insertVoucherProduct(VoucherProduct voucherProduct) {
		return voucherMapper.insertVouherProductSelective(voucherProduct);
		
	}

	public int deleteVoucherProduct(String voucherId) {
		int id = Integer.parseInt(voucherId);
		int rValue = voucherMapper.deleteVoucherProductByVoucherId(id);
		return rValue;
		
	}
	
	
	public List<Voucher> fetchAllVoucherByCorporateClientId() {
		return voucherMapper.selectAllVoucherByCorporateClientId();
	}

	public List<String> getListCoverages(Integer id) {
		return voucherMapper.getListCoverages(id);
	}

	public Voucher selectVoucherById(String voucherId) {
		int id = Integer.parseInt(voucherId);
		return voucherMapper.selectVoucherById(id);
	}

	public int updateVoucher(Voucher voucher) {
		return voucherMapper.updateVoucherSelective(voucher);
	}

	public List<String> getListB2BReport() {
		return voucherMapper.getListB2BReport();
	}

	public List<String> getListClaimManagement() {
		return voucherMapper.getListClaimManagement();
	}

	public List<String> getListCoveragesByClaimId(String claimId) {
		return voucherMapper.getListCoveragesByClaimId(claimId);
	}

	public List<String> getListB2BReportByDate(String start, String end) {
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(start);
			endDate=new SimpleDateFormat("yyyy-MM-dd").parse(end); 
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		
		
		return reportCmsMapper.getListB2BReportByDate(startDate, endDate);
	}

	public List<String> getListClaimManagementByDate(String start, String end) {
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(start);
			endDate=new SimpleDateFormat("yyyy-MM-dd").parse(end); 
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		
		return reportCmsMapper.getListClaimByDate(startDate, endDate);
	}

	public List<String> getDocumentDetail(String claimId) {
		return reportCmsMapper.getDocumentDetail(claimId);
	}

	public PolicyClaim selectPolicyClaimById(String claimId) {
		return policyClaimMapper.selectByClaimId(claimId);
	}

	public int updatePolicyClaimStatus(PolicyClaim policyClaim) {
		return policyClaimMapper.updatePolicyClaimStatus(policyClaim);
		
	}

	public List<String> getListTransactionByInsuranceType() {
		return reportCmsMapper.getListTransactionByInsuranceType();
	}

	public List<String> getListTransactionByInsuranceTypeByDate(String end, String start) {
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(start);
			endDate=new SimpleDateFormat("yyyy-MM-dd").parse(end); 
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		return reportCmsMapper.getListTransactionByInsuranceTypeAndDate(startDate, endDate);
	}

	public List<String> getListTransactionByDatePeriod() {
		return reportCmsMapper.getListTransactionDatePeriod();
	}
	
	
	public List<String> getListTransactionByBenefitPeriod() {
		return reportCmsMapper.getListTransactionByBenefitPeriod();
	}

	public List<String> getListTransactionDatePeriodByDate(String start, String end) {
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(start);
			endDate=new SimpleDateFormat("yyyy-MM-dd").parse(end); 
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		return reportCmsMapper.getListTransactionDatePeriodByDate(startDate, endDate);
	}

	public PolicyClaimCoverage getListPolicyCliemCoverageByClaimId(String claimId, String coverageId) {
		return reportCmsMapper.getListPolicyCliemCoverageByClaimId(claimId, coverageId);
	}

	public int updatePolicyClaimCoverageStatus(PolicyClaimCoverage policyClaimCoverage) {
		return policyClaimMapper.updatePolicyClaimCoverageStatus(policyClaimCoverage);
		
	}

	public Voucher selectFreeVoucherByCode(String type, String codeVoucher) {
		return voucherMapper.selectByCodeAndType(codeVoucher,type);
	}

	public CoverageCategory getCoverageCategoryById(String coverageCategoryId) {
		return coverageCategoryMapper.selectByCoverageCategoryId(coverageCategoryId);
	}

	public List<Product> getProductByProductTypeAndStatus(String productType) {
		return productMapper.selectByProductTypeAndStatusActive(productType);
	}

	public List<String> fetchAllVoucherByVoucherType(String voucherType) {
		return voucherMapper.selectAllVoucherByVoucherType(voucherType);
	}

	public List<String> fetchAllVoucherByVoucherTypeAndDate(String voucherType, String start, String end) {
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(start);
			endDate=new SimpleDateFormat("yyyy-MM-dd").parse(end); 
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		return voucherMapper.selectAllVoucherByVoucherTypeAndDate(voucherType,startDate, endDate);
	}

	public List<String> getListFreeInsuranceReportByVoucherType(String voucherType) {
		return reportCmsMapper.getListFreeInsuranceReportByVoucherType(voucherType);
	}

	public List<String> getListGenderChart() {
		return reportCmsMapper.getListGenderChart();
	}

	public String getListAgeChart() {
		return reportCmsMapper.getAgeChart();
	}

	public List<String> getListAgeNoRangeChart() {
		return reportCmsMapper.getAgeNoRangeChart();
	}
	
	public String getDemographicCount() {
		return reportCmsMapper.getDemographicCount();
	}

	public List<String> getListB2bTransaction() {
		return reportCmsMapper.getB2bTransaction();
	}

	public List<PolicyOrderProduct> getListOrderDetail(String orderId) {
		return reportCmsMapper.getDetailB2bTransaction(orderId);
	}

	public List<String> getListB2bTransactionByDate(String start, String end) {
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(start);
			endDate=new SimpleDateFormat("yyyy-MM-dd").parse(end); 
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		return reportCmsMapper.getB2bTransactionByDate(startDate, endDate);
	}

	
	public List<String> getListB2cTransaction() {
		return reportCmsMapper.getB2cTransaction();
	}

	
	public List<String> getListB2cTransactionByDate(String start, String end) {
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(start);
			endDate=new SimpleDateFormat("yyyy-MM-dd").parse(end); 
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		return reportCmsMapper.getB2cTransactionByDate(startDate, endDate);
	}
	
	public List<String> getListUser() {
		return reportCmsMapper.getUsers();
	}

	public List<String> getListCoverageForForm() {
		return reportCmsMapper.getListCoverage();
	}
	
	public List<String> getListCoverageByInsuranceType(String insuranceTypeId) {
		return reportCmsMapper.getListCoverageByInsuranceType(insuranceTypeId);
	}

	public List<String> getListPeriodForForm() {
		return reportCmsMapper.getListPeriod();
	}

	public List<String> getListInsuranceTypeForForm() {
		return reportCmsMapper.getListInsuranceType();
	}

	public List<Voucher> fetchAllVoucherByDate(String start, String end) {
		return voucherMapper.selectVoucherByDate(start, end);
		
	}
	
	public String getInsuranceTypeByVoucherId(Integer integer) {
		return reportCmsMapper.getInsuranceTypeByVoucherId(integer);
	}

	public List<String> fetchUserB2bByVoucherCode() {
		return reportCmsMapper.getUserB2bByVoucherCode();
	}

	public List<String> fetchUserB2bByOrderDate(String start, String end) {
		return reportCmsMapper.getUserB2bByOrderDate(start, end);
	}
	
	public List<String> fetchB2bOrderConfirm() {
		return reportCmsMapper.getB2bOrderConfirm();
	}
	
}
