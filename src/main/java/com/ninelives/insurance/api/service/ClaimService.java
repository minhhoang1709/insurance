package com.ninelives.insurance.api.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.api.dto.AccidentClaimDto;
import com.ninelives.insurance.api.dto.ClaimCoverageDto;
import com.ninelives.insurance.api.dto.ClaimDocumentDto;
import com.ninelives.insurance.api.model.PolicyClaim;
import com.ninelives.insurance.api.model.PolicyClaimBankAccount;
import com.ninelives.insurance.api.model.PolicyClaimCoverage;
import com.ninelives.insurance.api.model.PolicyClaimDetailAccident;
import com.ninelives.insurance.api.model.PolicyClaimDocument;
import com.ninelives.insurance.api.ref.ClaimCoverageStatus;
import com.ninelives.insurance.api.ref.ClaimStatus;
import com.ninelives.insurance.api.service.trx.PolicyClaimTrxService;

@Service
public class ClaimService {
	private static final Logger logger = LoggerFactory.getLogger(ClaimService.class);
	
	@Autowired PolicyClaimTrxService policyClaimTrxService;
	
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public PolicyClaim<PolicyClaimDetailAccident> submitAccidentalClaim(final String userId, final AccidentClaimDto claimDto){
		return registerAccidentalClaim(userId, claimDto);
	}
	
	protected PolicyClaim<PolicyClaimDetailAccident> registerAccidentalClaim(final String userId, final AccidentClaimDto claimDto){
		
		LocalDate today = LocalDate.now();
		
		PolicyClaim<PolicyClaimDetailAccident>  claim = new PolicyClaim<>();
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
