package com.ninelives.insurance.api.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.ninelives.insurance.api.dto.AccidentClaimDto;
import com.ninelives.insurance.api.dto.ClaimBankAccountDto;
import com.ninelives.insurance.api.dto.ClaimCoverageDto;
import com.ninelives.insurance.api.dto.ClaimDetailAccidentAddressDto;
import com.ninelives.insurance.api.dto.ClaimDocTypeDto;
import com.ninelives.insurance.api.dto.ClaimDocumentDto;
import com.ninelives.insurance.api.dto.CoverageDto;
import com.ninelives.insurance.api.dto.UserFileDto;
import com.ninelives.insurance.api.model.ClaimDocType;
import com.ninelives.insurance.api.model.Coverage;
import com.ninelives.insurance.api.model.PolicyClaim;
import com.ninelives.insurance.api.model.PolicyClaimBankAccount;
import com.ninelives.insurance.api.model.PolicyClaimCoverage;
import com.ninelives.insurance.api.model.PolicyClaimDetailAccident;
import com.ninelives.insurance.api.model.PolicyClaimDocument;
import com.ninelives.insurance.api.model.UserFile;

@Component
public class ModelMapperAdapter {
	public AccidentClaimDto toDto(PolicyClaim<PolicyClaimDetailAccident> m){
		AccidentClaimDto dto = null;
		if(m!=null){
			dto = new AccidentClaimDto();
			dto.setClaimId(m.getClaimId());
			dto.setClaimDate(m.getCreatedDate());
			dto.setAccidentSummary(m.getIncidentSummary());
			dto.setStatus(m.getStatus());
			dto.setAccidentAddress(toDto(m.getPolicyClaimDetail()));
			dto.setClaimBankAccount(toDto(m.getPolicyClaimBankAccount()));
			if(!CollectionUtils.isEmpty(m.getPolicyClaimCoverages())){
				List<ClaimCoverageDto> covDtos = new ArrayList<>();
				for(PolicyClaimCoverage cov : m.getPolicyClaimCoverages()){
					covDtos.add(toDto(cov));
				}
				dto.setClaimCoverages(covDtos);
			}			
			
			if(!CollectionUtils.isEmpty(m.getPolicyClaimDocuments())){
				List<ClaimDocumentDto> docDtos = new ArrayList<>();
				for(PolicyClaimDocument doc : m.getPolicyClaimDocuments()){
					docDtos.add(toDto(doc));
				}
				dto.setClaimDocuments(docDtos);
			}
		}
		return dto;
	}
	
	public ClaimDocumentDto toDto(PolicyClaimDocument m){
		ClaimDocumentDto dto = null;
		if(m!=null){
			dto = new ClaimDocumentDto();
			dto.setClaimDocumentId(m.getClaimDocumentId());
			dto.setClaimDocType(toDto(m.getClaimDocType()));
			dto.setFile(toUserFileDto(m.getFileId()));			
		}
		return dto;
	}
	public UserFileDto toUserFileDto(Long fileId) {
		UserFileDto dto = null;
		if(fileId != null){
			dto = new UserFileDto();
			dto.setFileId(fileId);
		}
		return dto;
	}
	public UserFileDto toDto(UserFile m) {
		UserFileDto dto = null;
		if(m!=null){
			dto = new UserFileDto();
			dto.setFileId(m.getFileId());
		}
		return dto;
	}
	public ClaimBankAccountDto toDto(PolicyClaimBankAccount m){
		ClaimBankAccountDto dto = null;
		if(m!=null){
			dto = new ClaimBankAccountDto();
			dto.setName(m.getAccountName());
			dto.setAccount(m.getAccountNumber());
			dto.setBankName(m.getAccountBankName());
			dto.setBankSwitt(m.getAccountBankSwift());
			dto.setBankSwiftCode(m.getAccountBankSwiftCode());
		}
		return dto;
	}
	public ClaimDetailAccidentAddressDto toDto(PolicyClaimDetailAccident m){
		ClaimDetailAccidentAddressDto dto = null;
		if(m!=null){
			dto = new ClaimDetailAccidentAddressDto();
			dto.setAddress(m.getAccidentAddress());
			dto.setCity(m.getAccidentAddressCity());
			dto.setCountry(m.getAccidentAddressCountry());
			dto.setProvince(m.getAccidentAddressProvince());
		}
		return dto;
	}
	public ClaimCoverageDto toDto(PolicyClaimCoverage m){
		ClaimCoverageDto dto = null;
		if(m!=null){
			dto = new ClaimCoverageDto();
			dto.setStatus(m.getStatus());
			dto.setCoverage(toDto(m.getCoverage()));
		}
		return dto;
	}
	public CoverageDto toDto(Coverage m){
		CoverageDto dto = null;
		if(m!=null){
			dto = new CoverageDto();
			dto.setCoverageId(m.getCoverageId());
			dto.setName(m.getName());
			dto.setRecommendation(m.getRecommendation());
			dto.setIsRecommended(m.getIsRecommended());
			dto.setHasBeneficiary(m.getHasBeneficiary());
			dto.setMaxLimit(m.getMaxLimit());
			if(!CollectionUtils.isEmpty(m.getClaimDocTypes())){
				List<ClaimDocTypeDto> docTypeDtos = new ArrayList<>();
				for(ClaimDocType docType: m.getClaimDocTypes()){
					docTypeDtos.add(toDto(docType));
				}
			}
		}
		return dto;
	}
	public ClaimDocTypeDto toDto(ClaimDocType m) {
		ClaimDocTypeDto dto = null;
		if(m!=null){
			dto = new ClaimDocTypeDto();
			dto.setClaimDocTypeId(m.getClaimDocTypeId());
			dto.setName(m.getName());
		}
		return dto;
	}
}
