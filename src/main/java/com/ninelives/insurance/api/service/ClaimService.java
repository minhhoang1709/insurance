package com.ninelives.insurance.api.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.api.adapter.ModelMapperAdapter;
import com.ninelives.insurance.api.dto.AccidentClaimDto;
import com.ninelives.insurance.api.dto.ClaimCoverageDto;
import com.ninelives.insurance.api.dto.ClaimDetailAccidentAddressDto;
import com.ninelives.insurance.api.dto.ClaimDocumentDto;
import com.ninelives.insurance.api.dto.FilterDto;
import com.ninelives.insurance.api.model.PolicyClaim;
import com.ninelives.insurance.api.model.PolicyClaimBankAccount;
import com.ninelives.insurance.api.model.PolicyClaimCoverage;
import com.ninelives.insurance.api.model.PolicyClaimDetailAccident;
import com.ninelives.insurance.api.model.PolicyClaimDocument;
import com.ninelives.insurance.api.mybatis.mapper.PolicyClaimMapper;
import com.ninelives.insurance.api.ref.ClaimCoverageStatus;
import com.ninelives.insurance.api.ref.ClaimStatus;
import com.ninelives.insurance.api.ref.OrderDtoFilterStatus;
import com.ninelives.insurance.api.ref.PolicyStatus;
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
	
	protected PolicyClaim<PolicyClaimDetailAccident> fetchClaimByClaimId(String userId, String claimId){		
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
	
	protected List<PolicyClaim<PolicyClaimDetailAccident>> fetchClaimsByOrderId(String userId, String orderId, FilterDto filterDto){
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
	
	protected List<PolicyClaim<PolicyClaimDetailAccident>> fetchClaims(String userId, FilterDto filterDto){
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
	
	private PolicyClaim<PolicyClaimDetailAccident> registerAccidentalClaim(final String userId, final AccidentClaimDto claimDto){
		//check if coverage require such doctype
		
		LocalDate today = LocalDate.now();
		
		PolicyClaim<PolicyClaimDetailAccident>  claim = new PolicyClaim<>();
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
