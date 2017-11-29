package com.ninelives.insurance.api.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ninelives.insurance.api.dto.AccidentClaimDto;
import com.ninelives.insurance.api.dto.ClaimBankAccountDto;
import com.ninelives.insurance.api.dto.ClaimCoverageDto;
import com.ninelives.insurance.api.dto.ClaimDetailAccidentAddressDto;
import com.ninelives.insurance.api.dto.ClaimDocTypeDto;
import com.ninelives.insurance.api.dto.ClaimDocumentDto;
import com.ninelives.insurance.api.dto.CoverageCategoryDto;
import com.ninelives.insurance.api.dto.CoverageDto;
import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.PeriodDto;
import com.ninelives.insurance.api.dto.ProductDto;
import com.ninelives.insurance.api.dto.UserDto;
import com.ninelives.insurance.api.dto.UserFileDto;
import com.ninelives.insurance.api.model.ClaimDocType;
import com.ninelives.insurance.api.model.Coverage;
import com.ninelives.insurance.api.model.CoverageCategory;
import com.ninelives.insurance.api.model.Period;
import com.ninelives.insurance.api.model.PolicyClaim;
import com.ninelives.insurance.api.model.PolicyClaimBankAccount;
import com.ninelives.insurance.api.model.PolicyClaimCoverage;
import com.ninelives.insurance.api.model.PolicyClaimDetailAccident;
import com.ninelives.insurance.api.model.PolicyClaimDocument;
import com.ninelives.insurance.api.model.PolicyOrder;
import com.ninelives.insurance.api.model.PolicyOrderProduct;
import com.ninelives.insurance.api.model.PolicyOrderUsers;
import com.ninelives.insurance.api.model.UserFile;

@Component
public class ModelMapperAdapter {
	//TODO: replace the hardcoded imgurl and title
	@Value("${ninelives.order.policy-imgUrl}")
	String policyImgUrl;
	@Value("${ninelives.order.policy-title}")
	String policyTitle;
	
	
	public OrderDto toDto(PolicyOrder m){
		OrderDto dto = null;
		if(m!=null){
			dto = new OrderDto();
			dto.setOrderId(m.getOrderId());
			dto.setOrderDate(m.getOrderDate());
			dto.setPolicyNumber(m.getPolicyNumber());
			dto.setPolicyStartDate(m.getPolicyStartDate());
			dto.setPolicyEndDate(m.getPolicyEndDate());
			dto.setTotalPremi(m.getTotalPremi());
			dto.setHasBeneficiary(m.getHasBeneficiary());
			dto.setProductCount(m.getProductCount());
			dto.setStatus(m.getStatus());
			dto.setCreatedDate(m.getCreatedDate());
			dto.setTitle(this.policyTitle);
			dto.setImgUrl(this.policyImgUrl);
			
			PeriodDto periodDto = toDto(m.getPeriod()); 
			dto.setPeriod(periodDto);
			
			if(!CollectionUtils.isEmpty(m.getPolicyOrderProducts())){
				int rank = 99;
				List<ProductDto> productDtos = new ArrayList<>();
				for(PolicyOrderProduct p: m.getPolicyOrderProducts()){
					productDtos.add(toDto(p, periodDto));					
					if(p.getCoverageDisplayRank() < rank){
						dto.setSubtitle(p.getCoverageName());
						rank = p.getCoverageDisplayRank();
					}
				}
				dto.setProducts(productDtos);
			}
			dto.setUser(toDto(m.getPolicyOrderUsers(), m.getUserId()));
		}
		return dto;
	}
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
			dto.setCoverageCategory(toDto(m.getCoverageCategory()));
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
			dto.setOrder(toDto(m.getPolicyOrder()));
		}
		return dto;
	}
	public CoverageCategoryDto toDto(CoverageCategory m){
		CoverageCategoryDto dto = null;
		if(m!=null){
			dto = new CoverageCategoryDto();
			//dto.setCoverageCategoryId(m.getCoverageCategoryId());
			dto.setName(m.getName());
			dto.setImageUrl(this.policyImgUrl);
		}
		return dto;
	}
	public ClaimDocumentDto toDto(PolicyClaimDocument m){
		ClaimDocumentDto dto = null;
		if(m!=null){
			dto = new ClaimDocumentDto();
			//dto.setClaimDocumentId(m.getClaimDocumentId());
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
	public ProductDto toDto(PolicyOrderProduct m, PeriodDto periodDto){
		ProductDto dto = null;
		if(m!=null){
			dto = new ProductDto();
			dto.setProductId(m.getProductId());
			dto.setName(m.getCoverageName());
			dto.setPremi(m.getPremi());			
			dto.setPeriod(periodDto);
			
			CoverageDto covDto = new CoverageDto();
			covDto.setCoverageId(m.getCoverageId());
			covDto.setName(m.getCoverageName());
			covDto.setMaxLimit(m.getCoverageMaxLimit());
			covDto.setHasBeneficiary(m.getCoverageHasBeneficiary());
			if(!CollectionUtils.isEmpty(m.getClaimDocTypes())){
				List<ClaimDocTypeDto> docTypeDtos = new ArrayList<>();
				for(ClaimDocType docType: m.getClaimDocTypes()){
					ClaimDocTypeDto docTypeDto = toDto(docType);
					docTypeDtos.add(docTypeDto);
				}
				covDto.setClaimDocTypes(docTypeDtos);
			}
			dto.setCoverage(covDto);
		}
		return dto;
	}
	public UserDto toDto(PolicyOrderUsers m, String userId){
		UserDto dto = null;
		if (m!=null){
			dto = new UserDto();
			dto.setUserId(userId);
			dto.setName(m.getName());
			dto.setBirthDate(m.getBirthDate());
			dto.setBirthPlace(m.getBirthPlace());
			dto.setEmail(m.getEmail());
			dto.setGender(m.getGender());
			dto.setIdCardFile(toUserFileDto(m.getIdCardFileId()));				
			dto.setPhone(m.getPhone());
			dto.setAddress(m.getAddress());						
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
	public PeriodDto toDto(Period m){
		PeriodDto dto = null;
		if(m!=null){
			dto = new PeriodDto();
			dto.setName(m.getName());
			dto.setPeriodId(m.getPeriodId());
			dto.setUnit(m.getUnit());
			dto.setValue(m.getValue());
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
