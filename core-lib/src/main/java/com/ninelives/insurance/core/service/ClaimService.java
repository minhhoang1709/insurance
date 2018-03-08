package com.ninelives.insurance.core.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.mybatis.mapper.PolicyClaimMapper;
import com.ninelives.insurance.model.PolicyClaim;
import com.ninelives.insurance.model.PolicyClaimCoverage;
import com.ninelives.insurance.model.PolicyClaimDetailAccident;
import com.ninelives.insurance.model.PolicyClaimDocument;
import com.ninelives.insurance.ref.FileUseType;

@Service
public class ClaimService {
	private static final Logger logger = LoggerFactory.getLogger(ClaimService.class);

	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@Autowired FileUploadService fileUploadService;
	@Autowired ProductService productService;
	@Autowired OrderService orderService;
	@Autowired PolicyClaimMapper policyClaimMapper;
	
	@Value("${ninelives.claim.filter-limit:100}")
	int defaultFilterLimit;
	
	@Value("${ninelives.claim.filter-max-limit:100}")
	int maxFilterLimit;
	
	@Value("${ninelives.claim.filter-offset:0}")
	int defaultFilterOffset;
	
	public PolicyClaim<PolicyClaimDetailAccident> fetchClaimByClaimId(String userId, String claimId){		
		PolicyClaim<PolicyClaimDetailAccident> policyClaim = policyClaimMapper.selectByUserIdAndClaimId(userId, claimId);
		postRetrieval(policyClaim, LocalDate.now());		
		return policyClaim;
	}
	
	public List<PolicyClaim<PolicyClaimDetailAccident>> fetchClaimsByOrderId(String userId, String orderId, Set<String> statusSet, int limit, int offset){
		if (limit > this.maxFilterLimit){
			limit = this.maxFilterLimit;
		}
		LocalDate today = LocalDate.now();
		List<PolicyClaim<PolicyClaimDetailAccident>> policyClaims = policyClaimMapper.selectByUserIdAndOrderIdAndStatusSet(userId, orderId, statusSet, limit, offset);
		for(PolicyClaim<PolicyClaimDetailAccident> p: policyClaims){
			postRetrieval(p, today);
		}
		return policyClaims;
	}
	public List<PolicyClaim<PolicyClaimDetailAccident>> fetchClaims(String userId, Set<String> statusSet, int limit, int offset){
		if (limit > this.maxFilterLimit){
			limit = this.maxFilterLimit;
		}
		
		LocalDate today = LocalDate.now();
		List<PolicyClaim<PolicyClaimDetailAccident>> policyClaims = policyClaimMapper.selectByUserIdAndStatusSet(userId, statusSet, limit, offset);
		for(PolicyClaim<PolicyClaimDetailAccident> p: policyClaims){
			postRetrieval(p, today);
		}
		return policyClaims;
	}
	
	protected void postRetrieval(PolicyClaim<?> policyClaim, LocalDate retrieveDate){
		if(policyClaim != null ){
			for(PolicyClaimCoverage c: policyClaim.getPolicyClaimCoverages()){
				c.setCoverage(productService.fetchCoverageByCoverageId(c.getCoverageId()));				
			}
			List<PolicyClaimCoverage> sortedList = policyClaim.getPolicyClaimCoverages().stream().sorted(
					(o1, o2) -> Integer.compare(o1.getCoverage().getDisplayRank(), o2.getCoverage().getDisplayRank()))
					.collect(Collectors.toList());
			policyClaim.setPolicyClaimCoverages(sortedList);
			if(policyClaim.getPolicyOrder()!=null){
				orderService.mapPolicyOrderStatus(policyClaim.getPolicyOrder(), retrieveDate);
			}
			
		}
	}
	
	public void updateClaimFiles(List<PolicyClaimDocument> docs) throws AppException{
		for(PolicyClaimDocument doc: docs){
			fileUploadService.moveTemp(doc.getUserFile(), FileUseType.CLAIM);
		}
	}	
	
	public String generateClaimId(){
		return UUID.randomUUID().toString();
	}

	public int getDefaultFilterLimit() {
		return defaultFilterLimit;
	}

	public int getMaxFilterLimit() {
		return maxFilterLimit;
	}

	public int getDefaultFilterOffset() {
		return defaultFilterOffset;
	}

	
}
