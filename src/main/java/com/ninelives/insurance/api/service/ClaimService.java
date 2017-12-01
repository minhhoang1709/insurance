package com.ninelives.insurance.api.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.api.adapter.ModelMapperAdapter;
import com.ninelives.insurance.api.dto.AccidentClaimDto;
import com.ninelives.insurance.api.dto.ClaimCoverageDto;
import com.ninelives.insurance.api.dto.ClaimDocumentDto;
import com.ninelives.insurance.api.dto.FilterDto;
import com.ninelives.insurance.api.exception.ApiBadRequestException;
import com.ninelives.insurance.api.model.PolicyClaim;
import com.ninelives.insurance.api.model.PolicyClaimBankAccount;
import com.ninelives.insurance.api.model.PolicyClaimCoverage;
import com.ninelives.insurance.api.model.PolicyClaimDetailAccident;
import com.ninelives.insurance.api.model.PolicyClaimDocument;
import com.ninelives.insurance.api.model.PolicyOrder;
import com.ninelives.insurance.api.mybatis.mapper.PolicyClaimMapper;
import com.ninelives.insurance.api.ref.ClaimCoverageStatus;
import com.ninelives.insurance.api.ref.ClaimStatus;
import com.ninelives.insurance.api.ref.ErrorCode;
import com.ninelives.insurance.api.service.trx.PolicyClaimTrxService;

@Service
public class ClaimService {
	private static final Logger logger = LoggerFactory.getLogger(ClaimService.class);

	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@Autowired PolicyClaimTrxService policyClaimTrxService;
	@Autowired ProductService productService;
	@Autowired OrderService orderService;
	@Autowired PolicyClaimMapper policyClaimMapper;
	
	@Autowired ModelMapperAdapter modelMapperAdapter;
	
	@Value("${ninelives.claim.filter-limit:100}")
	int defaultFilterLimit;
	
	@Value("${ninelives.claim.filter-max-limit:100}")
	int maxFilterLimit;
	
	@Value("${ninelives.claim.filter-offset:0}")
	int defaultFilterOffset;

	
	public AccidentClaimDto submitAccidentalClaim(final String userId, final AccidentClaimDto claimDto){
		PolicyClaim<PolicyClaimDetailAccident> claim = registerAccidentalClaim(userId, claimDto);		
		return modelMapperAdapter.toDto(claim);
	}	
	public AccidentClaimDto fetchClaimDtoByClaimId(String userId, String claimId){
		PolicyClaim<PolicyClaimDetailAccident> policyClaim = fetchClaimByClaimId(userId, claimId);
		return modelMapperAdapter.toDto(policyClaim);
	}
	public List<AccidentClaimDto> fetchClaimDtosByOrderId(String userId, String orderId, FilterDto filterDto) {
		List<PolicyClaim<PolicyClaimDetailAccident>> policyClaims = fetchClaimsByOrderId(userId, orderId, filterDto);
		List<AccidentClaimDto> dtoList = new ArrayList<>();
		if(policyClaims!=null){
			for(PolicyClaim<PolicyClaimDetailAccident> c: policyClaims){
				AccidentClaimDto dto = modelMapperAdapter.toDto(c);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
	public List<AccidentClaimDto> fetchClaimDtos(String userId, FilterDto filterDto){
		List<PolicyClaim<PolicyClaimDetailAccident>> policyClaims = fetchClaims(userId, filterDto);
		List<AccidentClaimDto> dtoList = new ArrayList<>();
		if(policyClaims!=null){
			for(PolicyClaim<PolicyClaimDetailAccident> c: policyClaims){
				AccidentClaimDto dto = modelMapperAdapter.toDto(c);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
	
	public PolicyClaim<PolicyClaimDetailAccident> fetchClaimByClaimId(String userId, String claimId){		
		PolicyClaim<PolicyClaimDetailAccident> policyClaim = policyClaimMapper.selectByUserIdAndClaimId(userId, claimId);
		if(policyClaim!=null){
			for(PolicyClaimCoverage c: policyClaim.getPolicyClaimCoverages()){
				c.setCoverage(productService.fetchCoverageByCoverageId(c.getCoverageId()));		
			}
			if(policyClaim.getPolicyOrder()!=null){
				orderService.mapPolicyOrderStatus(policyClaim.getPolicyOrder(), LocalDate.now());
			}
		}
		
		return policyClaim;
	}
	
	public List<PolicyClaim<PolicyClaimDetailAccident>> fetchClaimsByOrderId(String userId, String orderId, FilterDto filterDto){
		int offset = this.defaultFilterOffset;
		int limit = this.defaultFilterLimit;
		Set<String> statusSet = null;
		if(filterDto!=null){
			offset = filterDto.getOffset();
			if(filterDto.getLimit() > this.maxFilterLimit){
				limit = this.maxFilterLimit;
			}else{
				limit = filterDto.getLimit();
			}
			if(filterDto.getStatus()!=null && filterDto.getStatus().length > 0){
				statusSet = Arrays.stream(filterDto.getStatus()).collect(Collectors.toSet());				
			}
		}
		
		LocalDate today = LocalDate.now();
		List<PolicyClaim<PolicyClaimDetailAccident>> policyClaims = policyClaimMapper.selectByUserIdAndOrderIdAndStatusSet(userId, orderId, statusSet, limit, offset);
		for(PolicyClaim<PolicyClaimDetailAccident> p: policyClaims){
			for(PolicyClaimCoverage c: p.getPolicyClaimCoverages()){
				c.setCoverage(productService.fetchCoverageByCoverageId(c.getCoverageId()));				
			}
			if(p.getPolicyOrder()!=null){
				orderService.mapPolicyOrderStatus(p.getPolicyOrder(), today);
			}
		}
		return policyClaims;
	}
	
	public List<PolicyClaim<PolicyClaimDetailAccident>> fetchClaims(String userId, FilterDto filterDto){
		int offset = this.defaultFilterOffset;
		int limit = this.defaultFilterLimit;
		Set<String> statusSet = null;
		if(filterDto!=null){
			offset = filterDto.getOffset();
			if(filterDto.getLimit() > this.maxFilterLimit){
				limit = this.maxFilterLimit;
			}else{
				limit = filterDto.getLimit();
			}
			if(filterDto.getStatus()!=null && filterDto.getStatus().length > 0){
				statusSet = Arrays.stream(filterDto.getStatus()).collect(Collectors.toSet());				
			}
		}
		
		LocalDate today = LocalDate.now();
		List<PolicyClaim<PolicyClaimDetailAccident>> policyClaims = policyClaimMapper.selectByUserIdAndStatusSet(userId, statusSet, limit, offset);
		for(PolicyClaim<PolicyClaimDetailAccident> p: policyClaims){
			for(PolicyClaimCoverage c: p.getPolicyClaimCoverages()){
				c.setCoverage(productService.fetchCoverageByCoverageId(c.getCoverageId()));				
			}
			if(p.getPolicyOrder()!=null){
				orderService.mapPolicyOrderStatus(p.getPolicyOrder(), today);
			}
		}
		return policyClaims;
	}
	
	private PolicyClaim<PolicyClaimDetailAccident> registerAccidentalClaim(final String userId, final AccidentClaimDto claimDto, final boolean isValidateOnly) throws ApiBadRequestException{
		//check that order is exists and valid (not submitted, not inpayment)
		//check that the coverage is same like order
		//check that the document is complete(mandatory/nonmandatory)? and file status is uploaded
		//check current outstanding claim
		//check field is ok for address? or check for non-empty only
		//check field is ok for bank account? or check for non-empty only
		//
		logger.debug("Process isvalidationonly {} claim for user {} with claim {}", isValidateOnly, userId, claimDto);
		
		LocalDate today = LocalDate.now();
		
		if(claimDto==null || claimDto.getOrder() == null || StringUtils.isEmpty(claimDto.getOrder().getOrderId())){
			throw new ApiBadRequestException(ErrorCode.ERR7000_CLAIM_INVALID, "Permintaan tidak dapat diproses, silahkan cek kembali data klaim");
		}
		
		PolicyOrder order = orderService.fetchOrderByOrderId(userId, claimDto.getOrder().getOrderId());
		
//		if(order.getStatus())
//		
		return null;
	}
	private PolicyClaim<PolicyClaimDetailAccident> registerAccidentalClaim(final String userId, final AccidentClaimDto claimDto){
		//check if coverage require such doctype
		//check if doc is complete for mandatory case
		//check if order status is valid (not submitted or inpayment)
		
		LocalDate today = LocalDate.now();
		
		
		
		PolicyClaim<PolicyClaimDetailAccident> claim = new PolicyClaim<>();
		claim.setCoverageCategoryId("101"); //TODO: remove hardcoded
		claim.setClaimId(generateClaimId());
		claim.setOrderId(claimDto.getOrder().getOrderId());
		claim.setUserId(userId);
		claim.setClaimDate(today);
		claim.setIncidentDateTime(claimDto.getAccidentDate());
		claim.setIncidentSummary(claimDto.getAccidentSummary());
		claim.setStatus(ClaimStatus.SUBMITTED);
		
		PolicyClaimDetailAccident claimDetail = new PolicyClaimDetailAccident();
		claimDetail.setClaimId(claim.getClaimId());
		claimDetail.setAccidentAddress(claimDto.getAccidentAddress().getAddress());
		claimDetail.setAccidentAddressCity(claimDto.getAccidentAddress().getCity());
		claimDetail.setAccidentAddressProvince(claimDto.getAccidentAddress().getProvince());
		claimDetail.setAccidentAddressCountry(claimDto.getAccidentAddress().getCountry());
		claim.setPolicyClaimDetail(claimDetail);
		
		PolicyClaimBankAccount claimAccount = new PolicyClaimBankAccount();
		claimAccount.setClaimId(claim.getClaimId());
		claimAccount.setAccountName(claimDto.getClaimBankAccount().getName());
		claimAccount.setAccountBankName(claimDto.getClaimBankAccount().getBankName());
		claimAccount.setAccountBankSwift(claimDto.getClaimBankAccount().getBankSwitt());
		claimAccount.setAccountBankSwiftCode(claimDto.getClaimBankAccount().getBankSwiftCode());
		claimAccount.setAccountNumber(claimDto.getClaimBankAccount().getAccount());
		claim.setPolicyClaimBankAccount(claimAccount);
		
		List<PolicyClaimDocument> claimDocs = new ArrayList<>();
		for(ClaimDocumentDto c: claimDto.getClaimDocuments()){
			PolicyClaimDocument doc = new PolicyClaimDocument();
			doc.setClaimId(claim.getClaimId());
			doc.setClaimDocTypeId(c.getClaimDocType().getClaimDocTypeId());
			doc.setClaimDocType(productService.fetchClaimDocTypeByClaimDocTypeId(c.getClaimDocType().getClaimDocTypeId()));
			doc.setFileId(c.getFile().getFileId());
			claimDocs.add(doc);
		}
		claim.setPolicyClaimDocuments(claimDocs);		
		
		List<PolicyClaimCoverage> claimCovs = new ArrayList<>();
		for(ClaimCoverageDto c: claimDto.getClaimCoverages()){
			PolicyClaimCoverage cov = new PolicyClaimCoverage();
			cov.setClaimId(claim.getClaimId());
			cov.setCoverageId(c.getCoverage().getCoverageId());			
			cov.setStatus(ClaimCoverageStatus.SUBMITTED);
			cov.setCoverage(productService.fetchCoverageByCoverageId(c.getCoverage().getCoverageId()));
			claimCovs.add(cov);
		}
		claim.setPolicyClaimCoverages(claimCovs);		
		
		policyClaimTrxService.registerPolicyClaim(claim);
		
		
		return claim;
	}
	
	
	
	private String generateClaimId(){
		return UUID.randomUUID().toString();
	}
	

}
