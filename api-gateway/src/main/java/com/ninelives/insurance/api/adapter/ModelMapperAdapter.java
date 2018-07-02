package com.ninelives.insurance.api.adapter;

import java.time.LocalDateTime;
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

import com.ninelives.insurance.api.dto.ClaimDocTypeDto;
import com.ninelives.insurance.api.dto.CoverageCategoryDto;
import com.ninelives.insurance.api.dto.CoverageClaimDocTypeDto;
import com.ninelives.insurance.api.dto.CoverageDto;
import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.PaymentDto;
import com.ninelives.insurance.api.dto.PeriodDto;
import com.ninelives.insurance.api.dto.ProductDto;
import com.ninelives.insurance.api.dto.RowUploadDto;
import com.ninelives.insurance.api.dto.UserDto;
import com.ninelives.insurance.api.dto.UserFileDto;
import com.ninelives.insurance.api.dto.UserNotificationDto;
import com.ninelives.insurance.api.dto.VoucherDto;
import com.ninelives.insurance.api.util.DateTimeFormatUtil;
import com.ninelives.insurance.model.BatchFileUpload;
import com.ninelives.insurance.model.ClaimDocType;
import com.ninelives.insurance.model.Coverage;
import com.ninelives.insurance.model.CoverageCategory;
import com.ninelives.insurance.model.CoverageClaimDocType;
import com.ninelives.insurance.model.Period;
import com.ninelives.insurance.model.PolicyClaimBankAccount;
import com.ninelives.insurance.model.PolicyClaimCoverage;
import com.ninelives.insurance.model.PolicyClaimDetailAccident;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderProduct;
import com.ninelives.insurance.model.PolicyOrderUsers;
import com.ninelives.insurance.model.PolicyPayment;
import com.ninelives.insurance.model.Product;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.model.UserFile;
import com.ninelives.insurance.model.UserNotification;
import com.ninelives.insurance.model.Voucher;
import com.ninelives.insurance.ref.PolicyStatus;

@Component
public class ModelMapperAdapter {
	private static final Logger logger = LoggerFactory.getLogger(ModelMapperAdapter.class);
	
	@Value("${ninelives.order.policy-imgUrl}")
	String policyImgUrl;
	
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
			dto.setIdCardNo(m.getIdCardNo());
			dto.setPhone(m.getPhone());
			dto.setAddress(m.getAddress());
			
			Map<String, Object> configMap = new HashMap<>();
			configMap.put(UserDto.CONFIG_KEY_IS_NOTIFICATION_ENABLED, m.getIsNotificationEnabled());
			configMap.put(UserDto.CONFIG_KEY_IS_SYNC_GMAIL_ENABLED, m.getIsSyncGmailEnabled());
			
			dto.setConfig(configMap);
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
			
			if(m.getPolicyOrderVoucher()!=null){
				dto.setVoucher(toDto(m.getPolicyOrderVoucher().getVoucher()));
			}
			
			if(m.getStatus()!=null && m.getStatus().equals(PolicyStatus.INPAYMENT)){
				dto.setPayment(toDto(m.getPayment()));
			}
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
	
	public RowUploadDto toDto(BatchFileUpload m) {
		RowUploadDto dto = null;
		if(m!=null){
			dto = new RowUploadDto();
			dto.setEmail(m.getEmail());
			dto.setNama(m.getNama());
			dto.setJenisKelamin(m.getJenisKelamin());
			dto.setTanggalLahir(m.getTanggalLahir());
			dto.setTempatLahir(m.getTempatLahir());
			dto.setNoTelpon(m.getNoTelpon());
			dto.setKtpNumber(m.getKtpNumber());
			dto.setValidationStatus(m.getValidationStatus());
			dto.setErrorCode(m.getErrorCode());
			dto.setResponseMessage(m.getResponseMessage());
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
