package com.ninelives.insurance.api.adapter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.ninelives.insurance.api.dto.AccidentClaimDto;
import com.ninelives.insurance.api.dto.ClaimBankAccountDto;
import com.ninelives.insurance.api.dto.ClaimCoverageDto;
import com.ninelives.insurance.api.dto.ClaimDetailAccidentAddressDto;
import com.ninelives.insurance.api.dto.ClaimDocTypeDto;
import com.ninelives.insurance.api.dto.ClaimDocumentDto;
import com.ninelives.insurance.api.dto.ClaimDocumentExtraDto;
import com.ninelives.insurance.api.dto.CoverageCategoryDto;
import com.ninelives.insurance.api.dto.CoverageClaimDocTypeDto;
import com.ninelives.insurance.api.dto.CoverageDto;
import com.ninelives.insurance.api.dto.CoverageOptionDto;
import com.ninelives.insurance.api.dto.LoginDto;
import com.ninelives.insurance.api.dto.OrderDocTypeDto;
import com.ninelives.insurance.api.dto.OrderDocumentDto;
import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.PaymentDto;
import com.ninelives.insurance.api.dto.PeriodDto;
import com.ninelives.insurance.api.dto.PolicyClaimFamilyDto;
import com.ninelives.insurance.api.dto.PolicyOrderBeneficiaryDto;
import com.ninelives.insurance.api.dto.PolicyOrderFamilyDto;
import com.ninelives.insurance.api.dto.ProductDto;
import com.ninelives.insurance.api.dto.UserDto;
import com.ninelives.insurance.api.dto.UserFileDto;
import com.ninelives.insurance.api.dto.UserNotificationDto;
import com.ninelives.insurance.api.dto.VoucherDto;
import com.ninelives.insurance.api.util.DateTimeFormatUtil;
import com.ninelives.insurance.core.service.TranslationService;
import com.ninelives.insurance.model.ClaimDocType;
import com.ninelives.insurance.model.Coverage;
import com.ninelives.insurance.model.CoverageCategory;
import com.ninelives.insurance.model.CoverageClaimDocType;
import com.ninelives.insurance.model.CoverageOption;
import com.ninelives.insurance.model.OrderDocType;
import com.ninelives.insurance.model.Period;
import com.ninelives.insurance.model.PolicyClaim;
import com.ninelives.insurance.model.PolicyClaimBankAccount;
import com.ninelives.insurance.model.PolicyClaimCoverage;
import com.ninelives.insurance.model.PolicyClaimDetailAccident;
import com.ninelives.insurance.model.PolicyClaimDocument;
import com.ninelives.insurance.model.PolicyClaimDocumentExtra;
import com.ninelives.insurance.model.PolicyClaimFamily;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderBeneficiary;
import com.ninelives.insurance.model.PolicyOrderDocument;
import com.ninelives.insurance.model.PolicyOrderFamily;
import com.ninelives.insurance.model.PolicyOrderProduct;
import com.ninelives.insurance.model.PolicyOrderUsers;
import com.ninelives.insurance.model.PolicyPayment;
import com.ninelives.insurance.model.Product;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.model.UserFile;
import com.ninelives.insurance.model.UserLogin;
import com.ninelives.insurance.model.UserNotification;
import com.ninelives.insurance.model.Voucher;
import com.ninelives.insurance.ref.PolicyStatus;

@Component
public class ModelMapperAdapter {
	private static final Logger logger = LoggerFactory.getLogger(ModelMapperAdapter.class);
	
	@Value("${ninelives-api.coverage-img-url-path}")
	String coverageImgUrlPath;
//	@Value("${ninelives.order.policy-title}")
//	String policyTitle;
	
	@Autowired private TranslationService translationService;
		
	
	public TranslationService getTranslationService() {
		return translationService;
	}

	public void setTranslationService(TranslationService translationService) {
		this.translationService = translationService;
	}

	public PolicyOrderFamilyDto toDto(PolicyOrderFamily m){
		PolicyOrderFamilyDto dto = null;
		if(m!=null){
			dto = new PolicyOrderFamilyDto();
			dto.setSubId(m.getSubId());
			dto.setName(m.getName());
			dto.setBirthDate(m.getBirthDate()!=null?m.getBirthDate().atStartOfDay():null);
			dto.setRelationship(m.getRelationship());
			dto.setGender(m.getGender());
		}
		return dto;
	}
	
	public PolicyClaimFamilyDto toDto(PolicyClaimFamily m){
		PolicyClaimFamilyDto dto = null;
		if(m!=null){
			dto = new PolicyClaimFamilyDto();
			dto.setSubId(m.getSubId());
			dto.setName(m.getName());
			dto.setBirthDate(m.getBirthDate()!=null?m.getBirthDate().atStartOfDay():null);
			dto.setRelationship(m.getRelationship());
			dto.setGender(m.getGender());
		}
		return dto;
	}
	
	public PolicyClaimFamilyDto toPolicyClaimFamilyDto(PolicyOrderFamily m){
		PolicyClaimFamilyDto dto = null;
		if(m!=null){
			dto = new PolicyClaimFamilyDto();
			dto.setSubId(m.getSubId());
			dto.setName(m.getName());
			dto.setBirthDate(m.getBirthDate()!=null?m.getBirthDate().atStartOfDay():null);
			dto.setRelationship(m.getRelationship());
			dto.setGender(m.getGender());
		}
		return dto;
	}
	
	public CoverageOptionDto toDto(CoverageOption m){
		return toDto(m, LocaleContextHolder.getLocale().getLanguage());
	}
	
	public CoverageOptionDto toDto(CoverageOption m, String languageCode){
		CoverageOptionDto dto = null;
		if(m!=null){
			dto = new CoverageOptionDto();
			dto.setCoverageOptionId(m.getId());
			dto.setCoverageOptionName(translationService.translate(m.getCoverageOptionNameTranslationId(), languageCode,
					m.getCoverageOptionName()));
			dto.setCoverageOptionGroupId(m.getCoverageOptionGroupId());
		}
		return dto;
	}
	
	public UserNotificationDto toDto (UserNotification m){
		UserNotificationDto dto = null;
		if(m!=null){
			dto = new UserNotificationDto();
			dto.setId(m.getId());
			dto.setTitle(m.getTitle());
			dto.setBody(m.getBody());
			dto.setAction(m.getAction());
			dto.setActionData(m.getActionData());
			dto.setCreatedDate(m.getCreatedDate());
		}
		
		return dto;
	}
	public VoucherDto toDto(Voucher m) {
		VoucherDto dto = null;
		if(m!=null){
			dto = new VoucherDto();
			dto.setCode(m.getCode());
			dto.setHasBeneficiary(m.getHasBeneficiary());
			dto.setPeriod(toDto(m.getPeriod()));			
			dto.setPolicyStartDate(m.getPolicyStartDate()!=null?m.getPolicyStartDate().atStartOfDay():null);
			dto.setPolicyEndDate(m.getPolicyEndDate()!=null?m.getPolicyEndDate().atTime(LocalTime.MAX):null);
			dto.setProductCount(m.getProductCount());
			if(!CollectionUtils.isEmpty(m.getProducts())){
				List<ProductDto> productDtos = new ArrayList<>();
				for(Product p: m.getProducts()){
					productDtos.add(toDto(p));					
				}
				dto.setProducts(productDtos);
			}
			dto.setVoucherType(m.getVoucherType());
			dto.setTitle(m.getTitle());
			dto.setSubtitle(m.getSubtitle());
			dto.setDescription(m.getDescription());
			dto.setTotalPremi(m.getTotalPremi());
		}
		return dto;
	}
	public PaymentDto toDto(PolicyPayment m){
		PaymentDto dto = null;
		if(m!=null){
			dto = new PaymentDto();
			dto.setPaymentChargeDate(m.getChargeTime());
			dto.setPaymentExpiryDate(m.getChargeExpiryTime());
			try{
				dto.setExpiryDuration(DateTimeFormatUtil.timeBetween(LocalDateTime.now(),m.getChargeExpiryTime()));
			}catch(Exception e){
				logger.error("error convert duration <{}>", m);
			}			
		}
		return dto;
	}
	
	public LoginDto toDto(UserLogin m) {
		LoginDto dto = null;
		if(m!=null) {
			dto = new LoginDto();
			dto.setAccessToken(m.getTokenId());
			dto.setUser(toDto(m.getUser()));
			dto.setRequirePasswordChange(m.getRequirePasswordChange());
		}
		return dto;
	}
	
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
			dto.setPassportFile(toUserFileDto((m.getPassportFileId())));
			dto.setPhotoFile(toUserFileDto(m.getPhotoFileId()));
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
		return toDto(m, LocaleContextHolder.getLocale().getLanguage());
	}
	public OrderDto toDto(PolicyOrder m, String languageCode){
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
			dto.setIsFamily(m.getIsFamily());
			dto.setCreatedDate(m.getCreatedDate());
			dto.setOrderIdMap(m.getOrderIdMap());
			//dto.setTitle(this.policyTitle);
			//dto.setImgUrl(this.policyImgUrl);
			
			PeriodDto periodDto = toDto(m.getPeriod(), languageCode); 
			dto.setPeriod(periodDto);
			
			dto.setCoverageCategory(toDto(m.getCoverageCategory(), languageCode));
			if(dto.getCoverageCategory()!=null){
				dto.setTitle(dto.getCoverageCategory().getName());
				dto.setImgUrl(dto.getCoverageCategory().getImageUrl());
			}
						
			if(!CollectionUtils.isEmpty(m.getPolicyOrderProducts())){
				int rank = 99;
				List<ProductDto> productDtos = new ArrayList<>();
				for(PolicyOrderProduct p: m.getPolicyOrderProducts()){
					ProductDto productDto = toDto(p, periodDto, languageCode);
					productDtos.add(productDto);					
					if(p.getCoverageDisplayRank() < rank){
						dto.setSubtitle(productDto.getCoverage().getName());
						rank = p.getCoverageDisplayRank();
					}
				}
				dto.setProducts(productDtos);
			}
			dto.setUser(toDto(m.getPolicyOrderUsers(), m.getUserId()));
			
			if(m.getPolicyOrderVoucher()!=null){
				dto.setVoucher(toDto(m.getPolicyOrderVoucher().getVoucher()));
			}
			
			if(!CollectionUtils.isEmpty(m.getPolicyOrderFamilies())){
				List<PolicyOrderFamilyDto> familyDtos = new ArrayList<>();
				for(PolicyOrderFamily f:m.getPolicyOrderFamilies()){
					familyDtos.add(toDto(f));
				}
				dto.setFamilies(familyDtos);
			}
			if(m.getStatus()!=null && m.getStatus().equals(PolicyStatus.INPAYMENT)){
				dto.setPayment(toDto(m.getPayment()));
			}
			
			if(!CollectionUtils.isEmpty(m.getPolicyOrderDocuments())){
				List<OrderDocumentDto> docDtos = new ArrayList<>();
				for(PolicyOrderDocument doc : m.getPolicyOrderDocuments()){
					docDtos.add(toDto(doc));
				}
				dto.setOrderDocuments(docDtos);
			}
			
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
			dto.setAccidentDate(m.getIncidentDateTime());
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
			if(!CollectionUtils.isEmpty(m.getPolicyClaimFamilies())){
				List<PolicyClaimFamilyDto> famDtos = new ArrayList<>();
				for(PolicyClaimFamily fam : m.getPolicyClaimFamilies()){
					famDtos.add(toDto(fam));
				}
				dto.setFamilies(famDtos);
			}			
			dto.setOrder(toDto(m.getPolicyOrder()));
		}
		return dto;
	}
	public CoverageCategoryDto toDto(CoverageCategory m){
		return toDto(m, LocaleContextHolder.getLocale().getLanguage());
	}
	public CoverageCategoryDto toDto(CoverageCategory m, String languageCode){
		CoverageCategoryDto dto = null;
		if(m!=null){
			dto = new CoverageCategoryDto();
			dto.setCoverageCategoryId(m.getCoverageCategoryId());
			dto.setName(translationService.translate(m.getNameTranslationId(), languageCode, m.getName()));
			dto.setRecommendation(translationService.translate(m.getRecommendationTranslationId(), languageCode, m.getRecommendation()));
			dto.setImageUrl(this.coverageImgUrlPath + "cat" + m.getCoverageCategoryId() + ".jpg");
			dto.setRecommendationImageUrl(this.coverageImgUrlPath+"recommend"+m.getCoverageCategoryId()+".jpg");
			dto.setType(m.getType());
			if(m.getInsurer()!=null) {
				dto.setProviderCode(m.getInsurer().getCode());
			}
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
			dto.setExtra(toDto(m.getExtra()));
		}
		return dto;
	}
	public ClaimDocumentExtraDto toDto(PolicyClaimDocumentExtra m){
		ClaimDocumentExtraDto dto = null;
		if(m!=null){
			dto = new ClaimDocumentExtraDto();
			dto.setFamily(toDto(m.getFamily()));
		}
		return dto;
	}
	public OrderDocumentDto toDto(PolicyOrderDocument m){
		OrderDocumentDto dto = null;
		if(m!=null){
			dto = new OrderDocumentDto();
			//dto.setClaimDocumentId(m.getClaimDocumentId());
			dto.setOrderDocType(toDto(m.getOrderDocType()));
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
		return toDto(m, periodDto, LocaleContextHolder.getLocale().getLanguage());
	}
	public ProductDto toDto(PolicyOrderProduct m, PeriodDto periodDto, String languageCode){
		ProductDto dto = null;
		if(m!=null){
			dto = new ProductDto();
			dto.setProductId(m.getProductId());
			dto.setName(translationService.translate(m.getProduct().getNameTranslationId(), languageCode, m.getProduct().getName()));
			dto.setPremi(m.getPremi());
			dto.setPeriod(periodDto);
			
			CoverageDto covDto = new CoverageDto();
			covDto.setCoverageId(m.getCoverageId());
			covDto.setName(translationService.translate(m.getProduct().getCoverage().getNameTranslationId(), languageCode, m.getCoverageName()));
			covDto.setMaxLimit(m.getCoverageMaxLimit());			
			covDto.setHasBeneficiary(m.getCoverageHasBeneficiary());
			covDto.setIsLumpSum(m.getIsLumpSum());
			covDto.setCoverageOption(toDto(m.getCoverageOption(), languageCode));
			covDto.setCoverageCategory(toDto(m.getCoverageCategory(), languageCode));
			if(!CollectionUtils.isEmpty(m.getCoverageClaimDocTypes())){
				List<CoverageClaimDocTypeDto> covDocTypeDtos = new ArrayList<>();
				for(CoverageClaimDocType covDocType: m.getCoverageClaimDocTypes()){
					covDocTypeDtos.add(toDto(covDocType, languageCode));
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
			dto.setPassportFile(toUserFileDto(m.getPassportFileId()));
			dto.setPhone(m.getPhone());
			dto.setAddress(m.getAddress());
		}
		return dto;
	}
	public ProductDto toDto(Product m){
		return toDto(m, LocaleContextHolder.getLocale().getLanguage());
	}
	public ProductDto toDto(Product m, String languageCode){
		ProductDto dto = null;
		if(m!=null){
			dto = new ProductDto();
			dto.setProductId(m.getProductId());		
			dto.setName(translationService.translate(m.getNameTranslationId(), languageCode, m.getName()));
			dto.setPremi(m.getPremi());
			dto.setFamilyPremi(m.getFamilyPremi());
			dto.setPeriod(toDto(m.getPeriod(), languageCode));
			dto.setCoverage(toDto(m.getCoverage(), languageCode));
		}
		return dto;
	}
	public CoverageDto toDto(Coverage m){
		return toDto(m, LocaleContextHolder.getLocale().getLanguage());
	}
	public CoverageDto toDto(Coverage m, String languageCode){
		CoverageDto dto = null;
		if(m!=null){
			dto = new CoverageDto();
			dto.setCoverageId(m.getCoverageId());
			dto.setName(translationService.translate(m.getNameTranslationId(), languageCode, m.getName()));
			dto.setRecommendation(translationService.translate(m.getRecommendationTranslationId(), languageCode, m.getRecommendation()));
			dto.setIsRecommended(m.getIsRecommended());
			dto.setIsIntroRecommended(m.getIsIntroRecommended());
			dto.setIsLumpSum(m.getIsLumpSum());
			dto.setHasBeneficiary(m.getHasBeneficiary());
			dto.setMaxLimit(m.getMaxLimit());
			dto.setFamilyMaxLimit(m.getFamilyMaxLimit());
			if(!CollectionUtils.isEmpty(m.getCoverageClaimDocTypes())){
				List<CoverageClaimDocTypeDto> covDocTypeDtos = new ArrayList<>();
				for(CoverageClaimDocType covDocType: m.getCoverageClaimDocTypes()){
					covDocTypeDtos.add(toDto(covDocType,languageCode));
				}
				dto.setCoverageClaimDocTypes(covDocTypeDtos);
			}
			dto.setCoverageCategory(toDto(m.getCoverageCategory(), languageCode));
			dto.setCoverageOption(toDto(m.getCoverageOption(), languageCode));
			
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
		return toDto(m, LocaleContextHolder.getLocale().getLanguage());
	}
	
	public PeriodDto toDto(Period m, String languageCode){
		PeriodDto dto = null;
		if(m!=null){
			dto = new PeriodDto();
			dto.setName(translationService.translate(m.getNameTranslationId(), languageCode, m.getName()));
			dto.setPeriodId(m.getPeriodId());
			dto.setUnit(m.getUnit());
			dto.setValue(m.getValue());
			dto.setStartValue(m.getStartValue());
			dto.setEndValue(m.getEndValue());
		}
		return dto;
	}
	
	public CoverageClaimDocTypeDto toDto(CoverageClaimDocType m){
		return toDto(m, LocaleContextHolder.getLocale().getLanguage());
	}
	
	public CoverageClaimDocTypeDto toDto(CoverageClaimDocType m, String languageCode){
		CoverageClaimDocTypeDto dto = null;
		if(m!=null){
			dto = new CoverageClaimDocTypeDto();
			dto.setIsMandatory(m.getIsMandatory());
			dto.setClaimDocType(toDto(m.getClaimDocType(), languageCode));
		}
		return dto;
	}
	
	public ClaimDocTypeDto toDto(ClaimDocType m) {
		return toDto(m, LocaleContextHolder.getLocale().getLanguage());
	}
	
	public ClaimDocTypeDto toDto(ClaimDocType m, String languageCode) {
		ClaimDocTypeDto dto = null;
		if(m!=null){
			dto = new ClaimDocTypeDto();
			dto.setClaimDocTypeId(m.getClaimDocTypeId());
			dto.setName(translationService.translate(m.getNameTranslationId(), languageCode, m.getName()));
			dto.setUsageType(m.getUsageType());
		}
		return dto;
	}

	public OrderDocTypeDto toDto(OrderDocType m) {
		OrderDocTypeDto dto = null;
		if(m!=null){
			dto = new OrderDocTypeDto();
			dto.setOrderDocTypeId(m.getOrderDocTypeId());
			dto.setName(m.getName());
			dto.setUsageType(m.getUsageType());
			dto.setDisplayRank(m.getDisplayRank());
		}
		return dto;
	}
}
