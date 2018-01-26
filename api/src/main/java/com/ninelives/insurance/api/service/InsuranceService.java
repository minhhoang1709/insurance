package com.ninelives.insurance.api.service;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.api.exception.ApiInternalServerErrorException;
import com.ninelives.insurance.api.provider.insurance.InsuranceProvider;
import com.ninelives.insurance.api.provider.storage.StorageException;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.provider.insurance.aswata.dto.OrderResponseDto;
import com.ninelives.insurance.provider.insurance.aswata.dto.ResponseDto;
import com.ninelives.insurance.ref.ErrorCode;

@Service
public class InsuranceService {
	private static final Logger logger = LoggerFactory.getLogger(InsuranceService.class);
	
	@Autowired InsuranceProvider insuranceProvider;
	@Autowired Environment env;
	
	private boolean isAswataEnabled = true;
	
	public void orderPolicy(PolicyOrder order) throws ApiInternalServerErrorException{
		if(isAswataEnabled){
			try {
				ResponseDto<OrderResponseDto> result = insuranceProvider.orderPolicy(order);
				if(insuranceProvider.isSuccess(result)){
					order.setPolicyNumber(result.getResponse().getResponseParam().getPolicyNumber());
					order.setProviderOrderNumber(result.getResponse().getResponseParam().getOrderNumber());
					if(result.getResponse().getResponseParam().getDownloadUrl()!=null){
						order.setProviderDownloadUrl(result.getResponse().getResponseParam().getDownloadUrl());
					}
				}else{
					throw new ApiInternalServerErrorException(ErrorCode.ERR4201_ORDER_PROVIDER_FAIL, "Permintaan tidak dapat diproses, terjadi error pada sistem");
				}
			} catch (IOException | StorageException e) {
				throw new ApiInternalServerErrorException(ErrorCode.ERR4201_ORDER_PROVIDER_FILE_ERROR,
						"Permintaan tidak dapat diproses, terjadi error pada sistem");
			}
		}		
	}
	
	@PostConstruct
	public void init(){
		isAswataEnabled = isAswataEnabled();
	}
	
	/**
	 * Check if we should enabled aswata integration, this is because aswata dev server is turned off after 10 pm
	 * 
	 * Always return true for non dev build
	 * 
	 * @return
	 */
	private boolean isAswataEnabled(){
		if(env.acceptsProfiles("noaswata")){
			return false;
		}
		return true;
	}
}
