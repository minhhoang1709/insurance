package com.ninelives.insurance.api.adapter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ninelives.insurance.api.dto.AccidentClaimDto;
import com.ninelives.insurance.api.dto.ClaimBankAccountDto;
import com.ninelives.insurance.api.dto.ClaimCoverageDto;
import com.ninelives.insurance.api.dto.ClaimDetailAccidentAddressDto;
import com.ninelives.insurance.api.dto.ClaimDocTypeDto;
import com.ninelives.insurance.api.dto.ClaimDocumentDto;
import com.ninelives.insurance.api.dto.CoverageCategoryDto;
import com.ninelives.insurance.api.dto.CoverageClaimDocTypeDto;
import com.ninelives.insurance.api.dto.CoverageDto;
import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.PeriodDto;
import com.ninelives.insurance.api.dto.PolicyOrderBeneficiaryDto;
import com.ninelives.insurance.api.dto.ProductDto;
import com.ninelives.insurance.api.dto.UserDto;
import com.ninelives.insurance.api.dto.UserFileDto;
import com.ninelives.insurance.model.ClaimDocType;
import com.ninelives.insurance.model.Coverage;
import com.ninelives.insurance.model.CoverageCategory;
import com.ninelives.insurance.model.CoverageClaimDocType;
import com.ninelives.insurance.model.Period;
import com.ninelives.insurance.model.PolicyClaim;
import com.ninelives.insurance.model.PolicyClaimBankAccount;
import com.ninelives.insurance.model.PolicyClaimCoverage;
import com.ninelives.insurance.model.PolicyClaimDetailAccident;
import com.ninelives.insurance.model.PolicyClaimDocument;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderBeneficiary;
import com.ninelives.insurance.model.PolicyOrderProduct;
import com.ninelives.insurance.model.PolicyOrderUsers;
import com.ninelives.insurance.model.Product;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.model.UserFile;

@Component
public class ModelMapperAdapter {
	private static final Logger logger = LoggerFactory.getLogger(ModelMapperAdapter.class);
	
	//TODO: replace the hardcoded imgurl and title
	@Value("${ninelives.order.policy-imgUrl}")
	String policyImgUrl;
	@Value("${ninelives.order.policy-title}")
	String policyTitle;
	
	public UserDto toDto(User m){
		UserDto dto = null;
		if(m!=null){
			dto = new UserDto();
			dto.setUserId(m.getUserId());
			dto.setName(m.getName());
			dto.setBirthDate(m.getBirthDate()!=null?m.getBirthDate().atStartOfDay():null);
			dto.setBirthPlace(m.getBirthPlace());
			dto.setEmail(m.getEmail());
			dto.setGender(m.getGender());
			dto.setIdCardFile(toUserFileDto((m.getIdCardFileId())));
			dto.setPhone(m.getPhone());
			dto.setAddress(m.getAddress());
			
			Map<String, Object> configMap = new HashMap<>();
			configMap.put(UserDto.CONFIG_KEY_IS_NOTIFICATION_ENABLED, m.getIsNotificationEnabled());
			configMap.put(UserDto.CONFIG_KEY_IS_SYNC_GMAIL_ENABLED, m.getIsSyncGmailEnabled());
			
			dto.setConfig(configMap);
		}
		return dto;
	}
	public OrderDto toDto(PolicyOrder m){
		OrderDto dto = null;
		if(m!=null){
			dto = new OrderDto();
			dto.setOrderId(m.getOrderId());
			dto.setOrderDate(m.getOrderDate()!=null?m.getOrderDate().atStartOfDay():null);
			dto.setPolicyNumber(m.getPolicyNumber());
			dto.setPolicyStartDate(m.getPolicyStartDate()!=null?m.getPolicyStartDate().atStartOfDay():null);
			dto.setPolicyEndDate(m.getPolicyEndDate()!=null?m.getPolicyEndDate().atTime(LocalTime.MAX):null);
			dto.setTotalPremi(m.getTotalPremi());
			dto.setHasBeneficiary(m.getHasBeneficiary());
			dto.setProductCount(m.getProductCount());
			dto.setStatus(m.getStatus());
			dto.setCreatedDate(m.getCreatedDate());
			//dto.setTitle(this.policyTitle);
			//dto.setImgUrl(this.policyImgUrl);
			
			PeriodDto periodDto = toDto(m.getPeriod()); 
			dto.setPeriod(periodDto);
			
			dto.setCoverageCategory(toDto(m.getCoverageCategory()));
			if(dto.getCoverageCategory()!=null){
				dto.setTitle(dto.getCoverageCategory().getName());
				dto.setImgUrl(dto.getCoverageCategory().getImageUrl());
			}
						
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
	public PolicyOrderBeneficiaryDto toDto(PolicyOrderBeneficiary m){
		PolicyOrderBeneficiaryDto dto = null;
		if(m!=null){
			dto = new PolicyOrderBeneficiaryDto();
			dto.setOrderId(m.getOrderId());
			dto.setName(m.getName());
			dto.setEmail(m.getEmail());
			dto.setPhone(m.getPhone());
			dto.setRelationship(m.getRelationship());			
		}
		return dto;
	}

	public AccidentClaimDto toDto(PolicyClaim<PolicyClaimDetailAccident> m){
		AccidentClaimDto dto = null;
		if(m!=null){
			dto = new AccidentClaimDto();
			dto.setClaimId(m.getClaimId());
			dto.setClaimDate(m.getClaimDate()!=null?m.getClaimDate().atStartOfDay():null);
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
			dto.setCoverageCategoryId(m.getCoverageCategoryId());
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
			//dto.setBankSwitt(m.getAccountBankSwift());
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
			if(!CollectionUtils.isEmpty(m.getCoverageClaimDocTypes())){
				List<CoverageClaimDocTypeDto> covDocTypeDtos = new ArrayList<>();
				for(CoverageClaimDocType covDocType: m.getCoverageClaimDocTypes()){
					covDocTypeDtos.add(toDto(covDocType));
				}
				covDto.setCoverageClaimDocTypes(covDocTypeDtos);
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
			dto.setBirthDate(m.getBirthDate()!=null?m.getBirthDate().atStartOfDay():null);
			dto.setBirthPlace(m.getBirthPlace());
			dto.setEmail(m.getEmail());
			dto.setGender(m.getGender());
			dto.setIdCardFile(toUserFileDto(m.getIdCardFileId()));				
			dto.setPhone(m.getPhone());
			dto.setAddress(m.getAddress());						
		}
		return dto;
	}
	public ProductDto toDto(Product m){
		ProductDto dto = null;
		if(m!=null){
			dto = new ProductDto();
			dto.setProductId(m.getProductId());			
			dto.setName(m.getName());
			dto.setPremi(m.getPremi());
			dto.setPeriod(toDto(m.getPeriod()));
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
			if(!CollectionUtils.isEmpty(m.getCoverageClaimDocTypes())){
				List<CoverageClaimDocTypeDto> covDocTypeDtos = new ArrayList<>();
				for(CoverageClaimDocType covDocType: m.getCoverageClaimDocTypes()){
					covDocTypeDtos.add(toDto(covDocType));
				}
				dto.setCoverageClaimDocTypes(covDocTypeDtos);
			}
			dto.setCoverageCategory(toDto(m.getCoverageCategory()));
//			if(!CollectionUtils.isEmpty(m.getClaimDocTypes())){
//				List<ClaimDocTypeDto> docTypeDtos = new ArrayList<>();
//				for(ClaimDocType docType: m.getClaimDocTypes()){
//					docTypeDtos.add(toDto(docType));
//				}
//				dto.setClaimDocTypes(docTypeDtos);
//				//dto.setClaimDocTypes(m.getClaimDocTypes().stream().map(this::toDto).collect(Collectors.toList()));
//			}
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
	
	public CoverageClaimDocTypeDto toDto(CoverageClaimDocType m){
		CoverageClaimDocTypeDto dto = null;
		if(m!=null){
			dto = new CoverageClaimDocTypeDto();
			dto.setIsMandatory(m.getIsMandatory());
			dto.setClaimDocType(toDto(m.getClaimDocType()));
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
