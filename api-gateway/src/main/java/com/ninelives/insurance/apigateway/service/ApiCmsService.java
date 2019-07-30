package com.ninelives.insurance.apigateway.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.apigateway.dto.B2bOrderConfirmDto;
import com.ninelives.insurance.apigateway.dto.B2bReportDto;
import com.ninelives.insurance.apigateway.dto.B2bTransactionDto;
import com.ninelives.insurance.apigateway.dto.B2bTransactionListDto;
import com.ninelives.insurance.apigateway.dto.ClaimManagementDto;
import com.ninelives.insurance.apigateway.dto.ClaimStatusDto;
import com.ninelives.insurance.apigateway.dto.CompanyRegisterDto;
import com.ninelives.insurance.apigateway.dto.CorporateClientDto;
import com.ninelives.insurance.apigateway.dto.CoveragePolicyClaimDto;
import com.ninelives.insurance.apigateway.dto.DataProviderAgeDto;
import com.ninelives.insurance.apigateway.dto.DataProviderGenderDto;
import com.ninelives.insurance.apigateway.dto.FormObjectDto;
import com.ninelives.insurance.apigateway.dto.FreeInsuranceReportDto;
import com.ninelives.insurance.apigateway.dto.FreeVoucherFormDto;
import com.ninelives.insurance.apigateway.dto.PolicyCoverageItem;
import com.ninelives.insurance.apigateway.dto.PromoCodeDto;
import com.ninelives.insurance.apigateway.dto.StringDto;
import com.ninelives.insurance.apigateway.dto.TransactionReportBenefitPeriodDto;
import com.ninelives.insurance.apigateway.dto.TransactionReportDatePeriodDto;
import com.ninelives.insurance.apigateway.dto.TransactionReportInsuranceTypeDto;
import com.ninelives.insurance.apigateway.dto.UserB2bCodeDto;
import com.ninelives.insurance.apigateway.dto.UserB2bOrderStatusDto;
import com.ninelives.insurance.apigateway.dto.UserDetailDto;
import com.ninelives.insurance.apigateway.dto.UserManagementDto;
import com.ninelives.insurance.apigateway.dto.VoucherFormDto;
import com.ninelives.insurance.apigateway.dto.VoucherRegisterDto;
import com.ninelives.insurance.apigateway.dto.VoucherUpdateFormDto;
import com.ninelives.insurance.apigateway.util.DateTimeFormatUtil;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.service.B2BService;
import com.ninelives.insurance.core.service.OrderService;
import com.ninelives.insurance.core.service.UserService;
import com.ninelives.insurance.model.CorporateClient;
import com.ninelives.insurance.model.CoverageCategory;
import com.ninelives.insurance.model.Period;
import com.ninelives.insurance.model.PolicyClaim;
import com.ninelives.insurance.model.PolicyClaimCoverage;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderProduct;
import com.ninelives.insurance.model.Product;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.model.Voucher;
import com.ninelives.insurance.model.VoucherProduct;
import com.ninelives.insurance.ref.ClaimCoverageStatus;
import com.ninelives.insurance.ref.ClaimStatus;
import com.ninelives.insurance.ref.Gender;
import com.ninelives.insurance.ref.PolicyStatus;
import com.ninelives.insurance.ref.UserStatus;
import com.ninelives.insurance.ref.VoucherType;



@Service
public class ApiCmsService {
	private static final Logger logger = LoggerFactory.getLogger(ApiCmsService.class);
		
	@Autowired 
	B2BService b2bService;
	
	@Autowired 
	OrderService orderService;
	
	@Autowired 
	UserService userService;
	
	DateTimeFormatUtil dateTimeFormatUtil;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	
	public void  insertCorporateClient(CorporateClientDto corporateClientDto) 
			throws AppException {
		
		logger.info("Save Corporate client:<{}>", corporateClientDto);
		LocalDateTime now = LocalDateTime.now();
		CorporateClient cc =  new CorporateClient();
		cc.setCorporateClientName(corporateClientDto.getCompanyName());
		cc.setCorporateClientAddress(corporateClientDto.getAddress());
		cc.setCorporateClientPhoneNumber(corporateClientDto.getPhoneNumber());
		cc.setCorporateClientEmail(corporateClientDto.getEmail());
		cc.setCorporateClientProvider(corporateClientDto.getProviderName());
		cc.setCorporateClientProviderId(corporateClientDto.getProviderId());
		cc.setCreatedDate(now);
		cc.setCreatedBy(corporateClientDto.getUserName());
		cc.setUpdateDate(cc.getCreatedDate());
		
		b2bService.insertCorporateClient(cc);
		
	}
	
	
	public CorporateClient fetchByCompanyIdAndCorporateId(String companyName, String corporateId){
		return b2bService.selectByCompanyIdAndCorporateId(companyName,corporateId);
	}


	@SuppressWarnings("static-access")
	public List<CompanyRegisterDto> getListCompanyRegister() {
		
		List<CompanyRegisterDto> dtoList = new ArrayList<>();
		List<CorporateClient> corporateCompany = b2bService.fetchAllCorporateClient();
		
		for (CorporateClient c : corporateCompany) {
			
			CompanyRegisterDto dto = new CompanyRegisterDto();
			dto.setAddress(c.getCorporateClientAddress()==null?"":c.getCorporateClientAddress());
			dto.setCompanyName(c.getCorporateClientName()==null?"":c.getCorporateClientName());
			dto.setCorporateClientId(String.valueOf(c.getCorporateClientId()));
			dto.setEmail(c.getCorporateClientEmail()==null?"":c.getCorporateClientEmail());
			dto.setPhoneNumber(c.getCorporateClientPhoneNumber()==null?"":c.getCorporateClientPhoneNumber());
			dto.setProvider(c.getCorporateClientProvider()==null?"":c.getCorporateClientProvider());
			dto.setRegisterDate(dateTimeFormatUtil.getPaymentDateTimeString(c.getCreatedDate()));
			dto.setModifiedDate(dateTimeFormatUtil.getPaymentDateTimeString(c.getUpdateDate()));
			dto.setProviderId(c.getCorporateClientProviderId()==null?"":c.getCorporateClientProviderId());
			
			dtoList.add(dto);
		}
		
		return dtoList;
	
	}


	public CompanyRegisterDto updateCompanyClient(CompanyRegisterDto companyRegisterDto) {
		
		CompanyRegisterDto rValue = new CompanyRegisterDto();
		String id = companyRegisterDto.getCorporateClientId();
		String companyName = companyRegisterDto.getCompanyName();
		String phoneNumber = companyRegisterDto.getPhoneNumber();
		String email = companyRegisterDto.getEmail();
		String address = companyRegisterDto.getAddress();
		String providerName = companyRegisterDto.getProvider();
		String providerId = companyRegisterDto.getProviderId();
		
		CorporateClient cc = b2bService.getCorporateClientById(id);
		if(cc!=null){
			cc.setCorporateClientName(companyName);
			cc.setCorporateClientPhoneNumber(phoneNumber);
			cc.setCorporateClientEmail(email);
			cc.setCorporateClientAddress(address);
			cc.setCorporateClientProvider(providerName);
			cc.setCorporateClientProviderId(providerId);
			if(b2bService.updateCorporateClient(cc)==1){
				rValue.setCompanyName(companyName);
				rValue.setPhoneNumber(phoneNumber);
				rValue.setEmail(email);
				rValue.setAddress(address);
				rValue.setProviderId(providerId);
				rValue.setProvider(providerName);
				rValue.setCorporateClientId(id);
			}
	
		}
		
		return rValue;
	
	}


	public Voucher fetchVoucherByCode(String codeVoucher, String corporateClientId) {
		return b2bService.selectVoucherByCode(codeVoucher,corporateClientId);
	}



	@SuppressWarnings("static-access")
	public void insertVoucher(VoucherFormDto voucherFormDto) {
		
		logger.info("Save Voucher :<{}>", voucherFormDto.toString());
		LocalDateTime now = LocalDateTime.now();
		String periodId= voucherFormDto.getPeriod();
		List<String> listProduct= new ArrayList<>();
		boolean beneficiary=true;
		
		LocalDate policyStartDate= dateTimeFormatUtil.getLocalDateFromString(voucherFormDto.getPolicyStartDate());
		LocalDate useStartDate =dateTimeFormatUtil.getLocalDateFromString(voucherFormDto.getUseStartDate());
		LocalDate useEndDate= dateTimeFormatUtil.getLocalDateFromString(voucherFormDto.getUseEndDate());
		Integer corporateClientId =Integer.parseInt(voucherFormDto.getCorporateClientId());
		String voucherCode = voucherFormDto.getPromoCode().trim();
		
		String productId1="";
		String productId2="";
		String productId3="";
		String productId4="";
		String productId5="";
		String productId6="";
		
		Voucher cc =  new Voucher();
			cc.setCode(voucherCode); 
			cc.setTitle(voucherFormDto.getTitle()); 
			cc.setSubtitle(voucherFormDto.getSubtitle()); 
			cc.setDescription(voucherFormDto.getDescription()); 
			cc.setPolicyStartDate(policyStartDate); 
			cc.setUseStartDate(useStartDate);
			cc.setUseEndDate(useEndDate);
			cc.setTotalPremi(0);
	    	cc.setPeriodId(periodId);
			cc.setStatus("ACTIVE");
			cc.setVoucherType(VoucherType.B2B);
			cc.setCreatedDate(now);
			cc.setInviterRewardCount(null);
			cc.setMaxUse(null);
			cc.setApproveCnt(null);
					
			Period period = b2bService.selectPeriodById(periodId);  
			LocalDate policyEndDate = orderService.calculatePolicyEndDate(policyStartDate, period);
			cc.setPolicyEndDate(policyEndDate);
		
			String[] coverages = voucherFormDto.getCoverage().split(",");
			int productCount = coverages.length;
			cc.setProductCount(productCount);
			for (int i = 0; i < coverages.length; i++) {
				Product product = b2bService.selectProductByCoverageAndPeriod(coverages[i],periodId);
				listProduct.add(product.getProductId()+","+product.getPremi());
				if(i==0){productId1=product.getProductId();}
				if(i==1){productId2=product.getProductId();}
				if(i==2){productId3=product.getProductId();}
				if(i==3){productId4=product.getProductId();}
				if(i==4){productId5=product.getProductId();}
				if(i==5){productId6=product.getProductId();}
			}
			String pc = b2bService.selectProductAndCoverage(productId1,productId2,productId3,
					productId4,productId5,productId6);
			if(pc!=null){
				String sumAndBeneficiary = pc.substring(1, pc.length()-1);
				String[] arrayOfString =sumAndBeneficiary.split(",");
				String basePremi = arrayOfString[0];
				if(arrayOfString[1].equals("f")){beneficiary=false;}
				cc.setBasePremi(Integer.parseInt(basePremi));
				cc.setHasBeneficiary(beneficiary);
			}
			else{
				cc.setBasePremi(0);
				cc.setHasBeneficiary(beneficiary);
			}
			
			cc.setCorporateClientId(corporateClientId);
		
		b2bService.insertVoucher(cc);
		
		//Insert to Voucher Product
		for (int i = 0; i < listProduct.size(); i++) {
			
			VoucherProduct voucherProduct =  new VoucherProduct();
			Voucher voucher = b2bService.selectVoucherByCode(voucherCode, voucherFormDto.getCorporateClientId());
			String[] prod = listProduct.get(i).split(",");
			
			voucherProduct.setVoucherId(voucher.getId());
			voucherProduct.setProductId(prod[0]);
			voucherProduct.setPremi(0);
			b2bService.insertVoucherProduct(voucherProduct);
			
		}
	
	}


	@SuppressWarnings("static-access")
	public List<VoucherRegisterDto> getListVoucherRegister() {
		List<VoucherRegisterDto> dtoList = new ArrayList<>();
		List<Voucher> voucher = b2bService.fetchAllVoucherByCorporateClientId();
		
		for (Voucher c : voucher) {
			
			List<String> listCoverageId =  new ArrayList<>();
			VoucherRegisterDto dto = new VoucherRegisterDto();
			String corporateClientId = String.valueOf(c.getCorporateClient().getCorporateClientId());
			String corporateClientName= c.getCorporateClient().getCorporateClientName();
			String insuranceType="";
			String insuranceTypeName="";
			
			String insType = b2bService.getInsuranceTypeByVoucherId(c.getId());
			if(insType!=null){
				String[] iType = insType.substring(1,insType.length()-1).split(",");
				insuranceType=iType[0];
				insuranceTypeName=iType[1].replace("\"","");
			}
			
			dto.setCorporateClientId(corporateClientId==null?"":corporateClientId);
			dto.setB2bCode(c.getCode()==null?"":c.getCode());
			dto.setInsuranceType(insuranceType);
			dto.setInsuranceTypeName(insuranceTypeName);
			dto.setTitle(c.getTitle()==null?"":c.getTitle());
			dto.setSubtitle(c.getSubtitle()==null?"":c.getSubtitle());
			dto.setDescription(c.getDescription()==null?"":c.getDescription());
			dto.setPeriod(c.getPeriodId()==null?"":c.getPeriodId());
			dto.setPeriodName(c.getPeriod().getName()==null?"":c.getPeriod().getName());
			dto.setCorporateClientName(corporateClientName==null?"":corporateClientName);
			dto.setVoucherId(String.valueOf(c.getId()));
			
			String coverages="";
			
			List<String> listCoverage = b2bService.getListCoverages(c.getId());
			for (int i = 0; i < listCoverage.size(); i++) {
				String[] cov = listCoverage.get(i).substring(1,listCoverage.get(i).length()-1).split(",");
				listCoverageId.add(cov[0]);
				coverages += "- "+cov[1].substring(1, cov[1].length()-1)+"<br/>";
				
			}
			
			dto.setListCoverageId(listCoverageId);
			dto.setCoverage(coverages);
			
			String policyStartDate="";
			String policyStartDateForm="";
			
			String useStartDate="";
			String useStartDateForm="";
			
			String useEndDate="";
			String useEndDateForm="";
			
			if(c.getPolicyEndDate()!=null){
				 policyStartDate = dateTimeFormatUtil.getPaymentDateTimeString2(c.getPolicyStartDate());
				 policyStartDateForm = dateTimeFormatUtil.getPaymentDateTimeString3(c.getPolicyStartDate());
			}
			if(c.getUseStartDate()!=null){
				useStartDate= dateTimeFormatUtil.getPaymentDateTimeString2(c.getUseStartDate());
				useStartDateForm= dateTimeFormatUtil.getPaymentDateTimeString3(c.getUseStartDate());
				dto.setEnableEdit("N");
				boolean isEnableEdit = dateTimeFormatUtil.isEnableEdit(useStartDateForm);
				if(isEnableEdit){
					dto.setEnableEdit("Y");
				}
			}
			if(c.getUseEndDate()!=null){
				useEndDate = dateTimeFormatUtil.getPaymentDateTimeString2(c.getUseEndDate());
				useEndDateForm = dateTimeFormatUtil.getPaymentDateTimeString3(c.getUseEndDate());
			}
			
			dto.setPolicyStartDate(policyStartDate);
			dto.setPolicyStartDateForm(policyStartDateForm);
			dto.setUseEndDate(useEndDate);
			dto.setUseStartDate(useStartDate);
			dto.setUseEndDateForm(useEndDateForm);
			dto.setUseStartDateForm(useStartDateForm);
			dto.setModifiedDate(dateTimeFormatUtil.getPaymentDateTimeString(c.getUpdateDate()));
			
			dtoList.add(dto);
		}
		
		return dtoList;
	
	}


	@SuppressWarnings("static-access")
	public VoucherRegisterDto updateVoucherClient(VoucherUpdateFormDto voucherUpdateFormDto) {
		
		VoucherRegisterDto rValue = new VoucherRegisterDto();
		
		List<String> listProduct= new ArrayList<>();
		boolean beneficiary=true;
		
		String voucherId = voucherUpdateFormDto.getVoucherId();
		LocalDate policyStartDate= dateTimeFormatUtil.getLocalDateFromString(voucherUpdateFormDto.getPolicyStartDate());
		LocalDate useStartDate =dateTimeFormatUtil.getLocalDateFromString(voucherUpdateFormDto.getUseStartDate());
		LocalDate useEndDate= dateTimeFormatUtil.getLocalDateFromString(voucherUpdateFormDto.getUseEndDate());
		Integer corporateClientId =Integer.parseInt(voucherUpdateFormDto.getCorporateClientId());
		String voucherCode = voucherUpdateFormDto.getPromoCode().trim();
		String periodId= voucherUpdateFormDto.getPeriod();
		String productId1="";
		String productId2="";
		String productId3="";
		String productId4="";
		String productId5="";
		String productId6="";
		
		Voucher voucher =  b2bService.selectVoucherById(voucherId);
		if(voucher!=null){
			voucher.setCode(voucherCode); 
			voucher.setTitle(voucherUpdateFormDto.getTitle()); 
			voucher.setSubtitle(voucherUpdateFormDto.getSubtitle()); 
			voucher.setDescription(voucherUpdateFormDto.getDescription()); 
			voucher.setPolicyStartDate(policyStartDate); 
			voucher.setUseStartDate(useStartDate);
			voucher.setUseEndDate(useEndDate);
			voucher.setPeriodId(periodId);
			
			Period period = b2bService.selectPeriodById(periodId);  
			LocalDate policyEndDate = orderService.calculatePolicyEndDate(policyStartDate, period);
			voucher.setPolicyEndDate(policyEndDate);
			
			String[] coverages = voucherUpdateFormDto.getCoverage().split(",");
			int productCount = coverages.length;
			voucher.setProductCount(productCount);
			for (int i = 0; i < coverages.length; i++) {
				Product product = b2bService.selectProductByCoverageAndPeriod(coverages[i],periodId);
				listProduct.add(product.getProductId()+","+product.getPremi());
				if(i==0){productId1=product.getProductId();}
				if(i==1){productId2=product.getProductId();}
				if(i==2){productId3=product.getProductId();}
				if(i==3){productId4=product.getProductId();}
				if(i==4){productId5=product.getProductId();}
				if(i==5){productId6=product.getProductId();}
			}
			
			String pc = b2bService.selectProductAndCoverage(productId1,productId2,productId3,
					productId4,productId5,productId6);
			
			if(pc!=null){
				String sumAndBeneficiary = pc.substring(1, pc.length()-1);
				String[] arrayOfString =sumAndBeneficiary.split(",");
				String basePremi = arrayOfString[0];
				if(arrayOfString[1].equals("f")){beneficiary=false;}
				voucher.setBasePremi(Integer.parseInt(basePremi));
				voucher.setHasBeneficiary(beneficiary);
			}
			else{
				voucher.setBasePremi(0);
				voucher.setHasBeneficiary(beneficiary);
			}
			
			voucher.setCorporateClientId(corporateClientId);
		
			if(b2bService.updateVoucher(voucher)==1){
				
				if(b2bService.deleteVoucherProduct(voucherId)>0){
					//Insert to Voucher Product
					for (int i = 0; i < listProduct.size(); i++) {
						VoucherProduct voucherProduct =  new VoucherProduct();
						String[] prod = listProduct.get(i).split(",");
						voucherProduct.setVoucherId(Integer.parseInt(voucherId));
						voucherProduct.setProductId(prod[0]);
						voucherProduct.setPremi(Integer.parseInt(prod[1]));
						b2bService.insertVoucherProduct(voucherProduct);
						
					}
					
				}
		
			}
			
		}
		
		return rValue;
	
	}


	@SuppressWarnings("static-access")
	public List<VoucherRegisterDto> getListVoucherByDate(String startDate, String endDate) {
		List<VoucherRegisterDto> dtoList = new ArrayList<>();
		String start = dateTimeFormatUtil.getFormatStatDate(startDate);
		String end = dateTimeFormatUtil.getFormatStatDate(endDate);
		
		List<Voucher> voucher = b2bService.fetchAllVoucherByDate(start, end);
		
		for (Voucher c : voucher) {
			
			List<String> listCoverageId =  new ArrayList<>();
			VoucherRegisterDto dto = new VoucherRegisterDto();
			String corporateClientId = String.valueOf(c.getCorporateClient().getCorporateClientId());
			String corporateClientName= c.getCorporateClient().getCorporateClientName();
			String insuranceType="";
			String insuranceTypeName="";
			
			String insType = b2bService.getInsuranceTypeByVoucherId(c.getId());
			if(insType!=null){
				String[] iType = insType.substring(1,insType.length()-1).split(",");
				insuranceType=iType[0];
				insuranceTypeName=iType[1].replace("\"","");
			}
			
			dto.setCorporateClientId(corporateClientId==null?"":corporateClientId);
			dto.setB2bCode(c.getCode()==null?"":c.getCode());
			dto.setInsuranceType(insuranceType);
			dto.setInsuranceTypeName(insuranceTypeName);
			dto.setTitle(c.getTitle()==null?"":c.getTitle());
			dto.setSubtitle(c.getSubtitle()==null?"":c.getSubtitle());
			dto.setDescription(c.getDescription()==null?"":c.getDescription());
			dto.setPeriod(c.getPeriodId()==null?"":c.getPeriodId());
			dto.setPeriodName(c.getPeriod().getName()==null?"":c.getPeriod().getName());
			dto.setCorporateClientName(corporateClientName==null?"":corporateClientName);
			dto.setVoucherId(String.valueOf(c.getId()));
			
			String coverages="";
			
			List<String> listCoverage = b2bService.getListCoverages(c.getId());
			for (int i = 0; i < listCoverage.size(); i++) {
				String[] cov = listCoverage.get(i).substring(1,listCoverage.get(i).length()-1).split(",");
				listCoverageId.add(cov[0]);
				coverages += "- "+cov[1].substring(1, cov[1].length()-1)+"<br/>";
				
			}
			
			dto.setListCoverageId(listCoverageId);
			dto.setCoverage(coverages);
			
			String policyStartDate="";
			String policyStartDateForm="";
			
			String useStartDate="";
			String useStartDateForm="";
			
			String useEndDate="";
			String useEndDateForm="";
			
			if(c.getPolicyEndDate()!=null){
				 policyStartDate = dateTimeFormatUtil.getPaymentDateTimeString2(c.getPolicyStartDate());
				 policyStartDateForm = dateTimeFormatUtil.getPaymentDateTimeString3(c.getPolicyStartDate());
			}
			if(c.getUseStartDate()!=null){
				useStartDate= dateTimeFormatUtil.getPaymentDateTimeString2(c.getUseStartDate());
				useStartDateForm= dateTimeFormatUtil.getPaymentDateTimeString3(c.getUseStartDate());
				dto.setEnableEdit("N");
				boolean isEnableEdit = dateTimeFormatUtil.isEnableEdit(useStartDateForm);
				if(isEnableEdit){
					dto.setEnableEdit("Y");
				}
			}
			if(c.getUseEndDate()!=null){
				useEndDate = dateTimeFormatUtil.getPaymentDateTimeString2(c.getUseEndDate());
				useEndDateForm = dateTimeFormatUtil.getPaymentDateTimeString3(c.getUseEndDate());
			}
			
			dto.setPolicyStartDate(policyStartDate);
			dto.setPolicyStartDateForm(policyStartDateForm);
			dto.setUseEndDate(useEndDate);
			dto.setUseStartDate(useStartDate);
			dto.setUseEndDateForm(useEndDateForm);
			dto.setUseStartDateForm(useStartDateForm);
			dto.setModifiedDate(dateTimeFormatUtil.getPaymentDateTimeString(c.getUpdateDate()));
			
			dtoList.add(dto);
		}
		
		return dtoList;
	}


	public List<B2bReportDto> getListB2BReport() {
		
		List<B2bReportDto> dtoList =  new ArrayList<>();
		List<String> listB2BReport = b2bService.getListB2BReport();
		for (int i = 0; i < listB2BReport.size(); i++) {
			String[] cov = listB2BReport.get(i).substring(1,listB2BReport.get(i).length()-1).split(",");
			B2bReportDto b2bReport =  new B2bReportDto();
			
			if(cov[0].isEmpty() ||cov[0]==null){
				b2bReport.setCorporateClientName("");
			}else{
				b2bReport.setCorporateClientName(cov[0].substring(1, cov[0].length()-1));
			}
			
			b2bReport.setB2bCode(cov[1]);
			b2bReport.setChannel(cov[2]==null?"":cov[2]);
			b2bReport.setOrder(cov[3]==null?"":cov[3]);
			b2bReport.setOrderConfirm(cov[4]==null?"":cov[4]);
			b2bReport.setActive(cov[5]==null?"":cov[5]);
			b2bReport.setExpired(cov[6]==null?"":cov[6]);
			
			dtoList.add(b2bReport);
			
		}
		
		return dtoList;
	
	}


	@SuppressWarnings("null")
	public List<ClaimManagementDto> getListClaimManagement() {
		List<ClaimManagementDto> dtoList = new ArrayList<>();
		List<String> listClaimManagement =  b2bService.getListClaimManagement();
		
		for (int i = 0; i < listClaimManagement.size(); i++) {
			String[] claim = listClaimManagement.get(i).substring(1,listClaimManagement.get(i).length()-1).split(",",-1);
			ClaimManagementDto claimManagementDto = new ClaimManagementDto();
				String claimId = claim[0].trim();
				claimManagementDto.setClaimId(claimId);
				claimManagementDto.setClaimUserName(claim[1].replace("\"",""));
				claimManagementDto.setClaimUserEmail(claim[2]);
				claimManagementDto.setInsuranceType(claim[3].replace("\"",""));
				claimManagementDto.setClaimStatus(claim[4]);
				claimManagementDto.setClaimDate(claim[5]);
				claimManagementDto.setIncidentDateTime(claim[6]);
				
				String[] iDateTime = claim[6].replace("\"","").split(" ");
				claimManagementDto.setIncidentDate(iDateTime[0]);
				claimManagementDto.setIncidentTime(iDateTime[1]);
				
				claimManagementDto.setIncidentSummary(claim[7].replace("\"",""));
				claimManagementDto.setAccidentCountry(claim[8]);
				claimManagementDto.setAccidentCity(claim[9]);
				claimManagementDto.setAccountName(claim[10].replace("\"",""));
				claimManagementDto.setBankName(claim[11]);
				claimManagementDto.setBankCode(claim[12]);
				claimManagementDto.setBankAccountNumber(claim[13]);
				claimManagementDto.setPolicyNumber(claim[14]==null?"":claim[14]);
				
				String coverages="";
				String claimType="";
				List<String> listCoverage = b2bService.getListCoveragesByClaimId(claimId);
				//List<CoveragePolicyClaimDto> coveragePolicyClaimDto = new ArrayList<>();
			
				
				StringBuilder sb = new StringBuilder();
				if(listCoverage!=null){
					
					for (int j = 0; j < listCoverage.size(); j++) {
						String[] cov = listCoverage.get(j).substring(1,listCoverage.get(j).length()-1).split(",");
						coverages += "- "+cov[1].substring(1, cov[1].length()-1)+" ("+cov[2]+")"+"<br/>";
						claimType +=cov[1].substring(1, cov[1].length()-1)+",";
						
						sb.append("<input type=\"text\" class=\"form-control m-input\" readonly=\"readonly\"  id=\"claimNameCoverage\" name=\"claimNameCoverage\" value=\""+cov[1].substring(1, cov[1].length()-1)+"\">");
						sb.append("<input type=\"hidden\"   class=\"claimCoverageId\" name=\"claimCoverageId\" value=\""+cov[0]+"\">");
						sb.append("<select class=\"claimCoverageStatus\"  name=\"claimCoverageStatus\" value=\"\">");
						if(cov[2].equals("SUBMITTED")){
							sb.append("<option value=\"SUBMITTED\" selected >Submitted</option>");
						}else{
							sb.append("<option value=\"SUBMITTED\">Submitted</option>");
						}
						
						if(cov[2].equals("INREVIEW")){
							sb.append("<option value=\"INREVIEW\" selected >Inreview</option>");
						}else{
							sb.append("<option value=\"INREVIEW\">Inreview</option>");
						}
						
						if(cov[2].equals("APPROVED")){
							sb.append("<option value=\"APPROVED\" selected >Approved</option>");
						}else{
							sb.append("<option value=\"APPROVED\">Approved</option>");
						}
						
						if(cov[2].equals("DECLINED")){
							sb.append("<option value=\"DECLINED\" selected >Declined</option>");
						}else{
							sb.append("<option value=\"DECLINED\">Declined</option>");
						}
						
						if(cov[2].equals("PAID")){
							sb.append("<option value=\"PAID\" selected >Paid</option>");
						}else{
							sb.append("<option value=\"PAID\">Paid</option>");
						}
						
						sb.append("</select>");
						sb.append("<br/><br/>");
				
						
					}
				}
				
				//claimManagementDto.setCoveragePolicyClaimDto(coveragePolicyClaimDto);
				claimManagementDto.setCoveragePolicyClaims(sb.toString());
				claimManagementDto.setCoverage(coverages);
				claimManagementDto.setClaimType(claimType.substring(0, claimType.length() - 1));
				
				List<String> documentDetail = b2bService.getDocumentDetail(claimId);
				String documentInfo="";
				StringBuilder htmlBuilder = new StringBuilder();
				htmlBuilder.append("<ul>");
				if(documentDetail!=null || documentDetail.isEmpty()){
					for (int k = 0; k < documentDetail.size(); k++) {
						String[] doc = documentDetail.get(k).substring(1,documentDetail.get(k).length()-1).split(",");
						htmlBuilder.append("<li><a href="+doc[0].substring(1, doc[0].length()-1)+">"+
						doc[1].substring(1, doc[1].length()-1)+"</a></li>" );
						
					}
				htmlBuilder.append("</ul>");
				documentInfo = htmlBuilder.toString();
				claimManagementDto.setDocumentTitle(documentInfo);
					
				}
				
				
				dtoList.add(claimManagementDto);
			
		}
		
		return dtoList;
	}


	@SuppressWarnings("static-access")
	public List<B2bReportDto> getListB2BReportByDate(String startDate, String endDate) {
		
		List<B2bReportDto> dtoList =  new ArrayList<>();
		String start = dateTimeFormatUtil.getFormatStatDate(startDate);
		String end = dateTimeFormatUtil.getFormatStatDate(endDate);
		
		List<String> listB2BReport = b2bService.getListB2BReportByDate(start, end);
		for (int i = 0; i < listB2BReport.size(); i++) {
			String[] cov = listB2BReport.get(i).substring(1,listB2BReport.get(i).length()-1).split(",");
			B2bReportDto b2bReport =  new B2bReportDto();
			
			if(cov[0].isEmpty() ||cov[0]==null){
				b2bReport.setCorporateClientName("");
			}else{
				b2bReport.setCorporateClientName(cov[0].substring(1, cov[0].length()-1));
			}
			
			b2bReport.setB2bCode(cov[1]);
			b2bReport.setChannel(cov[2]==null?"":cov[2]);
			b2bReport.setOrder(cov[3]==null?"":cov[3]);
			b2bReport.setOrderConfirm(cov[4]==null?"":cov[4]);
			b2bReport.setActive(cov[5]==null?"":cov[5]);
			b2bReport.setExpired(cov[6]==null?"":cov[6]);
			
			dtoList.add(b2bReport);
			
		}
		
		return dtoList;
	}


	@SuppressWarnings({ "static-access", "null" })
	public List<ClaimManagementDto> getListClaimManagementByDate(String startDate, String endDate) {
		List<ClaimManagementDto> dtoList = new ArrayList<>();
		String start = dateTimeFormatUtil.getFormatStatDate(startDate);
		String end = dateTimeFormatUtil.getFormatStatDate(endDate);
		List<String> listClaimManagement =  b2bService.getListClaimManagementByDate(start, end);
		
		for (int i = 0; i < listClaimManagement.size(); i++) {
			String[] claim = listClaimManagement.get(i).substring(1,listClaimManagement.get(i).length()-1).split(",",-1);
			ClaimManagementDto claimManagementDto = new ClaimManagementDto();
				String claimId = claim[0].trim();
				claimManagementDto.setClaimId(claimId);
				claimManagementDto.setClaimUserName(claim[1]);
				claimManagementDto.setClaimUserEmail(claim[2]);
				claimManagementDto.setInsuranceType(claim[3].replace("\"",""));
				claimManagementDto.setClaimStatus(claim[4]);
				claimManagementDto.setClaimDate(claim[5]);
				claimManagementDto.setIncidentDateTime(claim[6]);
				
				String[] iDateTime = claim[6].replace("\"","").split(" ");
				claimManagementDto.setIncidentDate(iDateTime[0]);
				claimManagementDto.setIncidentTime(iDateTime[1]);
				
				claimManagementDto.setIncidentSummary(claim[7].replace("\"",""));
				claimManagementDto.setAccidentCountry(claim[8]);
				claimManagementDto.setAccidentCity(claim[9]);
				claimManagementDto.setAccountName(claim[10].replace("\"",""));
				claimManagementDto.setBankName(claim[11]);
				claimManagementDto.setBankCode(claim[12]);
				claimManagementDto.setBankAccountNumber(claim[13]);
				claimManagementDto.setPolicyNumber(claim[14]==null?"":claim[14]);
				
				
				String coverages="";
				String claimType="";
				List<String> listCoverage = b2bService.getListCoveragesByClaimId(claimId);
				
				if(listCoverage!=null){
					for (int j = 0; j < listCoverage.size(); j++) {
						String[] cov = listCoverage.get(j).substring(1,listCoverage.get(j).length()-1).split(",");
						coverages += "- "+cov[1].substring(1, cov[1].length()-1)+"<br/>";
						claimType +=cov[1].substring(1, cov[1].length()-1)+",";
					}
				}
				
				claimManagementDto.setCoverage(coverages);
				claimManagementDto.setClaimType(claimType.substring(0, claimType.length() - 1));
				List<String> documentDetail = b2bService.getDocumentDetail(claimId);
				String documentInfo="";
				
				if(documentDetail!=null || documentDetail.isEmpty()){
					for (int k = 0; k < documentDetail.size(); k++) {
						String[] doc = documentDetail.get(k).substring(1,documentDetail.get(k).length()-1).split(",");
						documentInfo += "<a href="+doc[0].replace("\"", "")+">"+doc[1].replace("\"", "")+"</a><br/>";
						
					}
					
					claimManagementDto.setDocumentTitle(documentInfo);
					
				}
				
				dtoList.add(claimManagementDto);
			
		}
		
		return dtoList;
	}


	@SuppressWarnings("rawtypes")
	public ClaimStatusDto updateClaimStatus(ClaimStatusDto claimStatusDto) {
		
		ClaimStatusDto rValue = new ClaimStatusDto();

		String claimId = claimStatusDto.getClaimId();
		String claimStatus = claimStatusDto.getClaimStatus();
		PolicyClaim policyClaim =  b2bService.selectPolicyClaimById(claimId);
		if(policyClaim!=null){
			if(!policyClaim.getStatus().name().equals(claimStatus)){
				ClaimStatus cs = ClaimStatus.valueOf(claimStatus);
				policyClaim.setStatus(cs);
				
				if(b2bService.updatePolicyClaimStatus(policyClaim)==1){
					rValue.setClaimStatus(claimStatus);
				}else{
					rValue.setErrMsgs("update claim status failed");
				}
				
			}
			
			List<PolicyCoverageItem> listPolicyCoverageItem = claimStatusDto.getPolicyCoverageItem();
			for (PolicyCoverageItem policyCoverageItem : listPolicyCoverageItem) {
				PolicyClaimCoverage policyClaimCoverage = 
						b2bService.getListPolicyCliemCoverageByClaimId(claimId, policyCoverageItem.getCoverageId());
				
				if(policyClaimCoverage!=null){
					if(!policyClaimCoverage.getStatus().name().equals(policyCoverageItem.getCoverageStatus())){
						ClaimCoverageStatus ccs =  ClaimCoverageStatus.valueOf(policyCoverageItem.getCoverageStatus());
						policyClaimCoverage.setStatus(ccs);
						if(b2bService.updatePolicyClaimCoverageStatus(policyClaimCoverage)!=1){
							rValue.setErrMsgs("update claim coverage status failed");
						}
						
					}
				}
				
			}
			
			
		}
		
		
		
		
		
		return rValue;
	}
	
	
	public List<TransactionReportInsuranceTypeDto> getListTransactionReportInsuranceType() {
		
		List<TransactionReportInsuranceTypeDto> dtoList =  new ArrayList<>();
		List<String> listTransaction = b2bService.getListTransactionByInsuranceType();
		for (int i = 0; i < listTransaction.size(); i++) {
			String[] cov = listTransaction.get(i).substring(1,listTransaction.get(i).length()-1).split(",");
			TransactionReportInsuranceTypeDto transactionByInsuranceType =  new TransactionReportInsuranceTypeDto();
			
			transactionByInsuranceType.setInsuranceTypeName(cov[0].replace("\"","")==null?"":cov[0].replace("\"",""));
			transactionByInsuranceType.setPaid(cov[1]==null?"":cov[1]);
			transactionByInsuranceType.setUnpaid(cov[2]==null?"":cov[2]);
			transactionByInsuranceType.setActive(cov[3]==null?"":cov[3]);
			transactionByInsuranceType.setExpired(cov[4]==null?"":cov[4]);
			dtoList.add(transactionByInsuranceType);
			
		}
		
		return dtoList;
	
	}
	
	@SuppressWarnings("static-access")
	public List<TransactionReportInsuranceTypeDto> 
		getListTransactionReportInsuranceTypeByDate(String startDate, String endDate) {
		
		List<TransactionReportInsuranceTypeDto> dtoList =  new ArrayList<>();
		String start = dateTimeFormatUtil.getFormatStatDate(startDate);
		String end = dateTimeFormatUtil.getFormatStatDate(endDate);
		
		List<String> listTransaction = b2bService.getListTransactionByInsuranceTypeByDate(end, start);
		for (int i = 0; i < listTransaction.size(); i++) {
			String[] cov = listTransaction.get(i).substring(1,listTransaction.get(i).length()-1).split(",");
			TransactionReportInsuranceTypeDto transactionByInsuranceType =  new TransactionReportInsuranceTypeDto();
			
			transactionByInsuranceType.setInsuranceTypeName(cov[0]==null?"":cov[0]);
			transactionByInsuranceType.setPaid(cov[1]==null?"":cov[1]);
			transactionByInsuranceType.setUnpaid(cov[2]==null?"":cov[2]);
			transactionByInsuranceType.setActive(cov[3]==null?"":cov[3]);
			transactionByInsuranceType.setExpired(cov[4]==null?"":cov[4]);
			dtoList.add(transactionByInsuranceType);
			
		}
		
		return dtoList;
	}


	@SuppressWarnings("static-access")
	public List<TransactionReportDatePeriodDto> getListTransactionReportDatePeriod() {
		
		List<TransactionReportDatePeriodDto> dtoList =  new ArrayList<>();
		List<String> listTransaction = b2bService.getListTransactionByDatePeriod();
		for (int i = 0; i < listTransaction.size(); i++) {
			String[] cov = listTransaction.get(i).substring(1,listTransaction.get(i).length()-1).split(",");
			TransactionReportDatePeriodDto transactionByDatePeriod =  new TransactionReportDatePeriodDto();
			
			String histDate = dateTimeFormatUtil.getFormatDate1(cov[0]==null?"":cov[0]);
			
			transactionByDatePeriod.setHistDate(histDate);
			transactionByDatePeriod.setPaid(cov[1]==null?"":cov[1]);
			transactionByDatePeriod.setUnpaid(cov[2]==null?"":cov[2]);
			transactionByDatePeriod.setActive(cov[3]==null?"":cov[3]);
			transactionByDatePeriod.setExpired(cov[4]==null?"":cov[4]);
			
			dtoList.add(transactionByDatePeriod);
			
		}
		
		return dtoList;
	}


	@SuppressWarnings("static-access")
	public List<TransactionReportDatePeriodDto> getListTransactionReportDatePeriodByDate(String startDate,
			String endDate) {
		
		List<TransactionReportDatePeriodDto> dtoList =  new ArrayList<>();
		String start = dateTimeFormatUtil.getFormatStatDate(startDate);
		String end = dateTimeFormatUtil.getFormatStatDate(endDate);
		
		List<String> listTransaction = b2bService.getListTransactionDatePeriodByDate(start, end);
		for (int i = 0; i < listTransaction.size(); i++) {
			String[] cov = listTransaction.get(i).substring(1,listTransaction.get(i).length()-1).split(",");
			TransactionReportDatePeriodDto transactionByDatePeriod =  new TransactionReportDatePeriodDto();
			
			String histDate = dateTimeFormatUtil.getFormatDate1(cov[0]==null?"":cov[0]);
			
			transactionByDatePeriod.setHistDate(histDate);
			transactionByDatePeriod.setPaid(cov[1]==null?"":cov[1]);
			transactionByDatePeriod.setUnpaid(cov[2]==null?"":cov[2]);
			transactionByDatePeriod.setActive(cov[3]==null?"":cov[3]);
			transactionByDatePeriod.setExpired(cov[4]==null?"":cov[4]);
			
			dtoList.add(transactionByDatePeriod);
			
		}
		
		return dtoList;
	}


	public Voucher fetchFreeVoucherByCode(String type,String codeVoucher) {
		return b2bService.selectFreeVoucherByCode(type ,codeVoucher);
	}


	public void insertFreeVoucher(FreeVoucherFormDto freeVoucherFormDto) {
		
		logger.info("Save Free Voucher :<{}>", freeVoucherFormDto.toString());
		LocalDateTime now = LocalDateTime.now();
		
		LocalDate useStartDate =dateTimeFormatUtil.getLocalDateFromString(freeVoucherFormDto.getPromoStartDate());
		LocalDate useEndDate= dateTimeFormatUtil.getLocalDateFromString(freeVoucherFormDto.getPromoEndDate());
		String voucherCode = freeVoucherFormDto.getPromoCode().trim();
		
		
		Voucher cc =  new Voucher();
			cc.setCode(voucherCode); 
			CoverageCategory covCategory =  b2bService.getCoverageCategoryById(freeVoucherFormDto.getCoverage());
			cc.setTitle(covCategory.getName()==null?"":covCategory.getName()); 
			cc.setSubtitle(covCategory.getName()==null?"":covCategory.getName()); 
			cc.setDescription(covCategory.getDescription()==null?"":covCategory.getDescription()); 
			cc.setUseStartDate(useStartDate);
			cc.setUseEndDate(useEndDate);
			cc.setTotalPremi(0);
			cc.setBasePremi(0);
			cc.setPeriodId(freeVoucherFormDto.getPeriodId());
	    	cc.setStatus("ACTIVE");
			cc.setVoucherType(VoucherType.FREE_PROMO_NEW_USER);
			cc.setCreatedDate(now);
			cc.setInviterRewardCount(null);
			cc.setMaxUse(Integer.valueOf(freeVoucherFormDto.getQuota()));
			cc.setApproveCnt(null);
		
		b2bService.insertVoucher(cc);
		
		
		List<Product> listProduct = b2bService.getProductByProductTypeAndStatus("FREE");
		
		//Insert to Voucher Product
		for (Product product : listProduct) {

			VoucherProduct voucherProduct =  new VoucherProduct();
			Voucher voucher = b2bService.selectFreeVoucherByCode("FREE_PROMO_NEW_USER",voucherCode);
			voucherProduct.setVoucherId(voucher.getId());
			voucherProduct.setProductId(product.getProductId());
			voucherProduct.setPremi(0);
			b2bService.insertVoucherProduct(voucherProduct);
		}
		
		
	}


	public List<PromoCodeDto> getListPromoCode() {
		List<PromoCodeDto> dtoList =  new ArrayList<>();
		
		String voucherType="FREE_PROMO_NEW_USER";
		
		List<String> listPromoCode = b2bService.fetchAllVoucherByVoucherType(voucherType);
		for (String voucher : listPromoCode) {
			String[] voc = voucher.substring(1,voucher.length()-1).split(",");
			PromoCodeDto dto =  new PromoCodeDto();
			dto.setPromoCode(voc[0]==null?"":voc[0]);
			dto.setCoverage(voc[1].replace("\"","")==null?"":voc[1].replace("\"",""));
			dto.setPeriodId(voc[2]==null?"":voc[2]);
			dto.setPeriod(voc[3].replace("\"","")==null?"":voc[3].replace("\"",""));
			dto.setPromoStartDate(dateTimeFormatUtil.getFormatDate1(voc[4]));
			dto.setPromoEndDate(dateTimeFormatUtil.getFormatDate1(voc[5]));
			dto.setQuota(voc[6]==null?"":voc[6]);
			dto.setUsed(voc[7]);
			
			dtoList.add(dto);
			
		}
		
		return dtoList;
	}


	@SuppressWarnings("static-access")
	public List<PromoCodeDto> getListPromoCodeByDate(String startDate, String endDate) {
		
		List<PromoCodeDto> dtoList =  new ArrayList<>();
		String voucherType="FREE_PROMO_NEW_USER";
		String start = dateTimeFormatUtil.getFormatStatDate(startDate);
		String end = dateTimeFormatUtil.getFormatStatDate(endDate);
		
		
		List<String> listPromoCode = b2bService.fetchAllVoucherByVoucherTypeAndDate(voucherType,start,end);
		for (String voucher : listPromoCode) {
			String[] voc = voucher.substring(1,voucher.length()-1).split(",");
			PromoCodeDto dto =  new PromoCodeDto();
			dto.setPromoCode(voc[0]==null?"":voc[0]);
			dto.setCoverage(voc[1].replace("\"","")==null?"":voc[1].replace("\"",""));
			dto.setPeriodId(voc[2]==null?"":voc[2]);
			dto.setPeriod(voc[3].replace("\"","")==null?"":voc[3].replace("\"",""));
			dto.setPromoStartDate(dateTimeFormatUtil.getFormatDate1(voc[4]));
			dto.setPromoEndDate(dateTimeFormatUtil.getFormatDate1(voc[5]));
			dto.setQuota(voc[6]==null?"":voc[6]);
			dto.setUsed(voc[7]);
			
			dtoList.add(dto);
			
		}
		
		return dtoList;
	}


	public List<FreeInsuranceReportDto> getListFreeInsuranceReportByVoucherType(String voucherType) {
		List<FreeInsuranceReportDto> dtoList =  new ArrayList<>();
		
		List<String> listB2BReport = b2bService.getListFreeInsuranceReportByVoucherType(voucherType);
		for (int i = 0; i < listB2BReport.size(); i++) {
			String[] cov = listB2BReport.get(i).substring(1,listB2BReport.get(i).length()-1).split(",");
			FreeInsuranceReportDto freeInsuranceDto =  new FreeInsuranceReportDto();
		
			freeInsuranceDto.setVoucherCode(cov[0]==null?"":cov[0]);
			freeInsuranceDto.setActive(cov[1]==null?"0":cov[1]);
			freeInsuranceDto.setExpired(cov[2]==null?"0":cov[2]);
	
			dtoList.add(freeInsuranceDto);
			
		}
		
		return dtoList;
	}


	public List<DataProviderGenderDto> getListGenderChart() {
		List<DataProviderGenderDto> dtoList =  new ArrayList<>();
		List<String> listGender = b2bService.getListGenderChart();
		for (int i = 0; i < listGender.size(); i++) {
			String[] cov = listGender.get(i).substring(1,listGender.get(i).length()-1).split(",");
			DataProviderGenderDto dto =  new DataProviderGenderDto();
			dto.setGender(cov[0].equals("F")?"FEMALE":"MALE");
			dto.setCount(cov[1]);
			dtoList.add(dto);
			
		}
		
		return dtoList;
	}


	public List<DataProviderAgeDto> getListAgeChart() {
		List<DataProviderAgeDto> dtoList =  new ArrayList<>();
		String age = b2bService.getListAgeChart();
		
		String[] cov = age.substring(1,age.length()-1).split(",");
		for (int i = 0; i < cov.length; i++) {
			DataProviderAgeDto dto =  new DataProviderAgeDto();
			
			dto.setAges(getAgeTitle(i));
			dto.setCount(cov[i]);
			
			dtoList.add(dto);
		
		}
		
		return dtoList;
	}


	private String getAgeTitle(int i) {
		String rValue = "";
		if(i==0){
			rValue ="<17";
		}
	    if(i==1){
			rValue ="17-20";
		}
		if(i==2){
			rValue ="21-30";
		}
		if(i==3){
			rValue ="31-40";
		}
		if(i==4){
			rValue ="41-50";
		}
		if(i==5){
			rValue ="51-60";
		}
		if(i==6){
			rValue =">61";
		}
		return rValue;
	}


	public List<DataProviderAgeDto> getListAgeNoRangeChart() {
		List<DataProviderAgeDto> dtoList =  new ArrayList<>();
		List<String> ListAges = b2bService.getListAgeNoRangeChart();
		
		for (int i = 0; i < ListAges.size(); i++) {
			String[] cov = ListAges.get(i).substring(1,ListAges.get(i).length()-1).split(",");
			
			if(!cov[0].equals("0")){
				DataProviderAgeDto dto =  new DataProviderAgeDto();
				dto.setAges(cov[0]);
				dto.setCount(cov[1]);
				dtoList.add(dto);
			}
			
		}
		
		return dtoList;
	}


	public StringDto getDemographicCount() {
		StringDto rValue = new StringDto();
		String value = b2bService.getDemographicCount();
		rValue.setValue(rValue==null?"":value);
		return rValue;
	}


	public List<B2bTransactionListDto> getListB2bTransaction() {
		List<B2bTransactionListDto> dtoList =  new ArrayList<>();
		List<String> listMainRow = b2bService.getListB2bTransaction();
		int rowNum =0;
		for (int i = 0; i < listMainRow.size(); i++) {
			rowNum++;
			String[] cov = listMainRow.get(i).substring(1,listMainRow.get(i).length()-1).split(",");
				String orderId = cov[0];
			
				B2bTransactionListDto dto =  new B2bTransactionListDto();
				dto.setRecordID(rowNum);
				dto.setOrderID(orderId);
				dto.setOrderDate(cov[1]);
				dto.setEmail(cov[2]==null?"":cov[2]);
				dto.setName(cov[3]==null?"":cov[3].replace("\"",""));
				dto.setCoverageType(cov[4]==null?"":cov[4].replace("\"",""));
				dto.setPeriod(cov[5]==null?"":cov[5].replace("\"",""));
				dto.setPolicyStartDate(cov[6]==null?"":cov[6]);
				dto.setPolicyEndDate(cov[7]==null?"":cov[7]);
				dto.setTotalPremi(cov[8]==null?"":cov[8]);
				dto.setProductCount(Integer.parseInt(cov[9]==null?"":cov[9]));
				dto.setStatus(cov[10]==null?"":cov[10]);
				dto.setPolicyNumber(cov[11]==null?"":cov[11]);
				dto.setProviderPolicyUrl(cov[12]==null?"":cov[12]);
					
				List<PolicyOrderProduct> listOrder = b2bService.getListOrderDetail(orderId);
				List<B2bTransactionDto> b2bTrxDto =  new ArrayList<>();
				
				for (PolicyOrderProduct policyOrderProduct : listOrder) {
					
					B2bTransactionDto detailDto = new  B2bTransactionDto();
					String premi = String.format("%,d", policyOrderProduct.getPremi());
					String coverageMax = String.format("%,d", policyOrderProduct.getCoverageMaxLimit());

					detailDto.setProductId(policyOrderProduct.getProductId());
					detailDto.setCoverageName(policyOrderProduct.getCoverageName());
					detailDto.setPremi(premi);
					detailDto.setCoverageMaxLimit(coverageMax);
					b2bTrxDto.add(detailDto);
					
				}
			
				dto.setOrders(b2bTrxDto);
				dtoList.add(dto);
		}
		
		return dtoList;
	
	}


	public List<B2bTransactionListDto> getListB2bTransactionByDate(String startDate, String endDate) {
		List<B2bTransactionListDto> dtoList =  new ArrayList<>();
		String start = dateTimeFormatUtil.getFormatStatDate(startDate);
		String end = dateTimeFormatUtil.getFormatStatDate(endDate);
		
		
		List<String> listMainRow = b2bService.getListB2bTransactionByDate(start, end);
		
		int rowNum =0;
		
		for (int i = 0; i < listMainRow.size(); i++) {
			rowNum++;
			String[] cov = listMainRow.get(i).substring(1,listMainRow.get(i).length()-1).split(",");
				String orderId = cov[0];
			
				B2bTransactionListDto dto =  new B2bTransactionListDto();
				dto.setRecordID(rowNum);
				dto.setOrderID(orderId);
				dto.setOrderDate(cov[1]);
				dto.setEmail(cov[2]==null?"":cov[2]);
				dto.setName(cov[3]==null?"":cov[3].replace("\"",""));
				dto.setCoverageType(cov[4]==null?"":cov[4].replace("\"",""));
				dto.setPeriod(cov[5]==null?"":cov[5].replace("\"",""));
				dto.setPolicyStartDate(cov[6]==null?"":cov[6]);
				dto.setPolicyEndDate(cov[7]==null?"":cov[7]);
				dto.setTotalPremi(cov[8]==null?"":cov[8]);
				dto.setProductCount(Integer.parseInt(cov[9]==null?"":cov[9]));
				dto.setStatus(cov[10]==null?"":cov[10]);
				dto.setPolicyNumber(cov[11]==null?"":cov[11]);
				dto.setProviderPolicyUrl(cov[12]==null?"":cov[12]);
					
				List<PolicyOrderProduct> listOrder = b2bService.getListOrderDetail(orderId);
				List<B2bTransactionDto> b2bTrxDto =  new ArrayList<>();
				
				for (PolicyOrderProduct policyOrderProduct : listOrder) {
					
					B2bTransactionDto detailDto = new  B2bTransactionDto();
					String premi = String.format("%,d", policyOrderProduct.getPremi());
					String coverageMax = String.format("%,d", policyOrderProduct.getCoverageMaxLimit());

					detailDto.setProductId(policyOrderProduct.getProductId());
					detailDto.setCoverageName(policyOrderProduct.getCoverageName());
					detailDto.setPremi(premi);
					detailDto.setCoverageMaxLimit(coverageMax);
					b2bTrxDto.add(detailDto);

					
				}
			
				dto.setOrders(b2bTrxDto);
				dtoList.add(dto);
		}
		
		return dtoList;
	}
	
	public List<B2bTransactionListDto> getListB2cTransaction() {
		List<B2bTransactionListDto> dtoList =  new ArrayList<>();
		List<String> listMainRow = b2bService.getListB2cTransaction();
		int rowNum =0;
		for (int i = 0; i < listMainRow.size(); i++) {
			rowNum++;
			String[] cov = listMainRow.get(i).substring(1,listMainRow.get(i).length()-1).split(",");
				String orderId = cov[0];
			
				B2bTransactionListDto dto =  new B2bTransactionListDto();
				dto.setRecordID(rowNum);
				dto.setOrderID(orderId);
				dto.setOrderDate(cov[1]);
				dto.setEmail(cov[2]==null?"":cov[2]);
				dto.setName(cov[3]==null?"":cov[3].replace("\"",""));
				dto.setCoverageType(cov[4]==null?"":cov[4].replace("\"",""));
				dto.setPeriod(cov[5]==null?"":cov[5].replace("\"",""));
				dto.setPolicyStartDate(cov[6]==null?"":cov[6]);
				dto.setPolicyEndDate(cov[7]==null?"":cov[7]);
				dto.setTotalPremi(cov[8]==null?"":cov[8]);
				dto.setProductCount(Integer.parseInt(cov[9]==null?"":cov[9]));
				dto.setStatus(cov[10]==null?"":cov[10]);
				dto.setPolicyNumber(cov[11]==null?"":cov[11]);
				dto.setProviderPolicyUrl(cov[12]==null?"":cov[12]);
					
				List<PolicyOrderProduct> listOrder = b2bService.getListOrderDetail(orderId);
				List<B2bTransactionDto> b2bTrxDto =  new ArrayList<>();
				
				for (PolicyOrderProduct policyOrderProduct : listOrder) {
					
					B2bTransactionDto detailDto = new  B2bTransactionDto();
					String premi = String.format("%,d", policyOrderProduct.getPremi());
					String coverageMax = String.format("%,d", policyOrderProduct.getCoverageMaxLimit());

					detailDto.setProductId(policyOrderProduct.getProductId());
					detailDto.setCoverageName(policyOrderProduct.getCoverageName());
					detailDto.setPremi(premi);
					detailDto.setCoverageMaxLimit(coverageMax);
					b2bTrxDto.add(detailDto);
					
				}
			
				dto.setOrders(b2bTrxDto);
				dtoList.add(dto);
		}
		
		return dtoList;
	
	}


	@SuppressWarnings("static-access")
	public List<B2bTransactionListDto> getListB2cTransactionByDate(String startDate, String endDate) {
		List<B2bTransactionListDto> dtoList =  new ArrayList<>();
		String start = dateTimeFormatUtil.getFormatStatDate(startDate);
		String end = dateTimeFormatUtil.getFormatStatDate(endDate);
		List<String> listMainRow = b2bService.getListB2cTransactionByDate(start, end);
		int rowNum =0;
		
		for (int i = 0; i < listMainRow.size(); i++) {
			rowNum++;
			String[] cov = listMainRow.get(i).substring(1,listMainRow.get(i).length()-1).split(",");
				String orderId = cov[0];
			
				B2bTransactionListDto dto =  new B2bTransactionListDto();
				dto.setRecordID(rowNum);
				dto.setOrderID(orderId);
				dto.setOrderDate(cov[1]);
				dto.setEmail(cov[2]==null?"":cov[2]);
				dto.setName(cov[3]==null?"":cov[3].replace("\"",""));
				dto.setCoverageType(cov[4]==null?"":cov[4].replace("\"",""));
				dto.setPeriod(cov[5]==null?"":cov[5].replace("\"",""));
				dto.setPolicyStartDate(cov[6]==null?"":cov[6]);
				dto.setPolicyEndDate(cov[7]==null?"":cov[7]);
				dto.setTotalPremi(cov[8]==null?"":cov[8]);
				dto.setProductCount(Integer.parseInt(cov[9]==null?"":cov[9]));
				dto.setStatus(cov[10]==null?"":cov[10]);
				dto.setPolicyNumber(cov[11]==null?"":cov[11]);
				dto.setProviderPolicyUrl(cov[12]==null?"":cov[12]);
					
				List<PolicyOrderProduct> listOrder = b2bService.getListOrderDetail(orderId);
				List<B2bTransactionDto> b2bTrxDto =  new ArrayList<>();
				
				for (PolicyOrderProduct policyOrderProduct : listOrder) {
					
					B2bTransactionDto detailDto = new  B2bTransactionDto();
					String premi = String.format("%,d", policyOrderProduct.getPremi());
					String coverageMax = String.format("%,d", policyOrderProduct.getCoverageMaxLimit());

					detailDto.setProductId(policyOrderProduct.getProductId());
					detailDto.setCoverageName(policyOrderProduct.getCoverageName());
					detailDto.setPremi(premi);
					detailDto.setCoverageMaxLimit(coverageMax);
					b2bTrxDto.add(detailDto);
					
				}
			
				dto.setOrders(b2bTrxDto);
				dtoList.add(dto);
		}
		
		return dtoList;
	}


	@SuppressWarnings("static-access")
	public List<UserManagementDto> getListUser() {
		List<UserManagementDto> dtoList =  new ArrayList<>();
		List<String> listUser = b2bService.getListUser();
		
		for (int i = 0; i < listUser.size(); i++) {
			String[] cov = listUser.get(i).substring(1,listUser.get(i).length()-1).split(",");
			UserManagementDto dto = new UserManagementDto();
			dto.setUserId(cov[0]);
			String userName ="";
			if(cov[1].contains("\"")){
				userName=cov[1].replace("\"","");
			}
			dto.setName(userName);
			dto.setEmail(cov[2]);
			dto.setGender(cov[3]);
			
			String bDate="";
			if(!cov[4].isEmpty()){
				 bDate = dateTimeFormatUtil.getFormatDate2(cov[4]);
			}
			dto.setBirthDate(bDate);
			dto.setBirthPlace(cov[5]);
			dto.setPhone(cov[6]);
			dto.setAddress(cov[7]==null?"":cov[7].replace("\"",""));
			dto.setIdCardNumber(cov[8]);
			dto.setStatus(cov[9]);
			
			dtoList.add(dto);
		}
		
		
		return dtoList;
	}


	public List<TransactionReportBenefitPeriodDto> getListTransactionReportBenefitPeriod() {
		List<TransactionReportBenefitPeriodDto> dtoList =  new ArrayList<>();
		List<String> listTransaction = b2bService.getListTransactionByBenefitPeriod();
		for (int i = 0; i < listTransaction.size(); i++) {
			String[] cov = listTransaction.get(i).substring(1,listTransaction.get(i).length()-1).split(",");
			TransactionReportBenefitPeriodDto transactionByBenefitPeriod =  new TransactionReportBenefitPeriodDto();
		
			transactionByBenefitPeriod.setInsuranceTypeName(cov[0]==null?"":cov[0].replace("\"",""));
			transactionByBenefitPeriod.setPeriod(cov[1]==null?"":cov[1].replace("\"",""));
			//transactionByBenefitPeriod.setBenefit(cov[2]==null?"":cov[2].replace("\"",""));
			transactionByBenefitPeriod.setUnpaid(cov[2]==null?"":cov[2]);
			transactionByBenefitPeriod.setPaid(cov[3]==null?"":cov[3]);
			transactionByBenefitPeriod.setNotActive(cov[4]==null?"":cov[4]);
			transactionByBenefitPeriod.setActive(cov[5]==null?"":cov[5]);
			transactionByBenefitPeriod.setExpired(cov[6]==null?"":cov[6]);
			
			dtoList.add(transactionByBenefitPeriod);
			
		}
		
		return dtoList;
	}


	public List<FormObjectDto> getListCoverage() {
		List<FormObjectDto> dtoList =  new ArrayList<>();
		List<String> listObj = b2bService.getListCoverageForForm();
		for (int i = 0; i < listObj.size(); i++) {
			String[] cov = listObj.get(i).substring(1,listObj.get(i).length()-1).split(",");
			FormObjectDto obj =  new FormObjectDto();
			obj.setCode(cov[0]==null?"":cov[0]);
			obj.setName(cov[1]==null?"":cov[1].replace("\"",""));
			dtoList.add(obj);
		}
		return dtoList;
	
	}
	
	public List<FormObjectDto> getListCoverageByInsuranceType(String insuranceType) {
		List<FormObjectDto> dtoList =  new ArrayList<>();
		List<String> listObj = b2bService.getListCoverageByInsuranceType(insuranceType);
		for (int i = 0; i < listObj.size(); i++) {
			String[] cov = listObj.get(i).substring(1,listObj.get(i).length()-1).split(",");
			FormObjectDto obj =  new FormObjectDto();
			obj.setCode(cov[0]==null?"":cov[0]);
			obj.setName(cov[1]==null?"":cov[1].replace("\"",""));
			dtoList.add(obj);
		}
		return dtoList;
	
	}


	public List<FormObjectDto> getListPeriod() {
		List<FormObjectDto> dtoList =  new ArrayList<>();
		List<String> listObj = b2bService.getListPeriodForForm();
		for (int i = 0; i < listObj.size(); i++) {
			String[] cov = listObj.get(i).substring(1,listObj.get(i).length()-1).split(",");
			FormObjectDto obj =  new FormObjectDto();
			obj.setCode(cov[0]==null?"":cov[0]);
			obj.setName(cov[1]==null?"":cov[1].replace("\"",""));
			dtoList.add(obj);
		}
		return dtoList;
	}


	public List<FormObjectDto> getListInsuranceType() {
		List<FormObjectDto> dtoList =  new ArrayList<>();
		List<String> listObj = b2bService.getListInsuranceTypeForForm();
		for (int i = 0; i < listObj.size(); i++) {
			String[] cov = listObj.get(i).substring(1,listObj.get(i).length()-1).split(",");
			FormObjectDto obj =  new FormObjectDto();
			obj.setCode(cov[0]==null?"":cov[0]);
			obj.setName(cov[1]==null?"":cov[1].replace("\"",""));
			dtoList.add(obj);
		}
		return dtoList;
	}


	public UserDetailDto updateUserDetail(UserDetailDto userDto) {
		
		UserDetailDto rValue = new UserDetailDto();
		
		String userId = userDto.getUserId();
		String name = userDto.getName();
		String email = userDto.getEmail();
		String gender = userDto.getGender();
		LocalDate ldBirthDate=null;
		
		if(!userDto.getBirthDate().isEmpty() || userDto.getBirthDate()!=null){
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			ldBirthDate = LocalDate.parse(userDto.getBirthDate(), formatter);
		}
		
		
		String birthPlace = userDto.getBirthPlace();
		String phone = userDto.getPhone();
		String address = userDto.getAddress();
		String idCardNumber = userDto.getIdCardNumber();
		String status =userDto.getStatus();
		
		User user =  userService.fetchByUserId(userId);
		if(user!=null){
			user.setName(name);
			user.setEmail(email);
			user.setGender(Gender.toEnum(gender));
			user.setBirthDate(ldBirthDate);
			user.setBirthPlace(birthPlace);
			user.setPhone(phone);
			user.setAddress(address);
			user.setIdCardNo(idCardNumber);
			user.setStatus(UserStatus.toEnum(status));
		
			
			if(userService.updateProfileInfo(user)==1){
				rValue.setName(name);
				rValue.setEmail(email);
			}
		}else{
			rValue.setErrMsgs("update user fail");
		}
		
		return rValue;
	
	}


	public List<UserB2bCodeDto> getListUserB2bByCode() {
		
		List<UserB2bCodeDto> dtoList =  new ArrayList<>();
		
		List<String> listPromoCode = b2bService.fetchUserB2bByVoucherCode();
		for (String voucher : listPromoCode) {
			String orderTime="";
			String[] voc = voucher.substring(1,voucher.length()-1).split(",");
			
			UserB2bCodeDto dto =  new UserB2bCodeDto();
			dto.setName(voc[0].isEmpty()?"":voc[0].replace("\"",""));
			dto.setGender(voc[1].isEmpty()?"":voc[1]);
			dto.setOrderDate(voc[2].isEmpty()?"":voc[2]);
			
			if(voc[3].isEmpty()){
				orderTime =voc[2];  
			}else{
				orderTime =voc[3].replace("\"","");
			}
			dto.setOrderTime(orderTime);
			dto.setUserId(voc[4].isEmpty()?"":voc[4]);
			dto.setEmail(voc[5].isEmpty()?"":voc[5]);
			dto.setOrderId(voc[6].isEmpty()?"":voc[6]);
			dto.setVoucherId(voc[7].isEmpty()?"":voc[7]);
			dto.setVoucherCode(voc[8].isEmpty()?"":voc[8]);
			dto.setPhone(voc[9].isEmpty()?"":voc[9]);
			dto.setBirthDate(voc[10].isEmpty()?"":voc[10]);
			dto.setOrderStatus(voc[11].isEmpty()?"":voc[11]);
			dtoList.add(dto);
			
		}
		
		return dtoList;
	}


	public List<UserB2bCodeDto> getListUserB2bByDate(String startDate, String endDate) {
		List<UserB2bCodeDto> dtoList = new ArrayList<>();
		String start = dateTimeFormatUtil.getFormatStatDate(startDate);
		String end = dateTimeFormatUtil.getFormatStatDate(endDate);
		
		List<String> listPromoCode = b2bService.fetchUserB2bByOrderDate(start, end);
		for (String voucher : listPromoCode) {
			String orderTime="";
			String[] voc = voucher.substring(1,voucher.length()-1).split(",");
			
			UserB2bCodeDto dto =  new UserB2bCodeDto();
			dto.setName(voc[0].isEmpty()?"":voc[0]);
			dto.setGender(voc[1].isEmpty()?"":voc[1]);
			dto.setOrderDate(voc[2].isEmpty()?"":voc[2]);
			
			if(voc[3].isEmpty()){
				orderTime =voc[2];  
			}else{
				orderTime =voc[3].replace("\"","");
			}
			dto.setOrderTime(orderTime);
			dto.setUserId(voc[4].isEmpty()?"":voc[4]);
			dto.setEmail(voc[5].isEmpty()?"":voc[5]);
			dto.setOrderId(voc[6].isEmpty()?"":voc[6]);
			dto.setVoucherId(voc[7].isEmpty()?"":voc[7]);
			dto.setVoucherCode(voc[8].isEmpty()?"":voc[8]);
			dto.setPhone(voc[9].isEmpty()?"":voc[9]);
			dto.setBirthDate(voc[10].isEmpty()?"":voc[10]);
			dto.setOrderStatus(voc[11].isEmpty()?"":voc[11]);
			dtoList.add(dto);
			
		}
		
		
		
		return dtoList;
	}


	public UserB2bOrderStatusDto updateUserB2bOrderStatus(UserB2bOrderStatusDto userB2bOrderStatusDto) {
		UserB2bOrderStatusDto rValue = new UserB2bOrderStatusDto();

		String orderId = userB2bOrderStatusDto.getOrderId();
		String orderStatus = userB2bOrderStatusDto.getOrderStatus();
		
		PolicyOrder policyOrder =  orderService.getPolicyOrderByOrderId(orderId);
		if(policyOrder!=null){
			if(!policyOrder.getStatus().name().equals(orderStatus)){
				try {
					if(orderService.paymentTerminated(policyOrder)==1){
						rValue.setOrderStatus(orderStatus);
					}else{
						rValue.setErrMsgs("update claim status failed");
					}
				} catch (AppException e) {
					rValue.setErrMsgs(e.getMessage());
				}
			}
		}else{
			rValue.setErrMsgs("update claim status failed, policy order not found");
		}
		return rValue;
	
	}


	public List<B2bOrderConfirmDto> getListB2bOrderConfirm() {
		List<B2bOrderConfirmDto> dtoList =  new ArrayList<>();
		
		List<String> listPromoCode = b2bService.fetchB2bOrderConfirm();
		for (String voucher : listPromoCode) {
			
			String[] voc = voucher.substring(1,voucher.length()-1).split(",");
			B2bOrderConfirmDto dto =  new B2bOrderConfirmDto();
			
			dto.setVoucherCode(voc[0].isEmpty()?"":voc[0].replace("\"",""));
			dto.setPaid(voc[1].isEmpty()?"":voc[1]);
			dto.setTerminated(voc[2].isEmpty()?"":voc[2]);
			dto.setApproved(voc[3].isEmpty()?"":voc[3]);
			dtoList.add(dto);
			
		}
		
		return dtoList;
	}
	
	
}
