package com.ninelives.insurance.core.service;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.core.exception.AppInternalServerErrorException;
import com.ninelives.insurance.core.provider.insurance.InsuranceProvider;
import com.ninelives.insurance.core.provider.insurance.InsuranceProviderConnectDisabledException;
import com.ninelives.insurance.core.provider.insurance.InsuranceProviderException;
import com.ninelives.insurance.core.provider.insurance.OrderConfirmResult;
import com.ninelives.insurance.core.provider.insurance.OrderResult;
import com.ninelives.insurance.core.provider.insurance.PtiInsuranceProvider;
import com.ninelives.insurance.core.provider.storage.StorageException;
import com.ninelives.insurance.core.support.pdf.PdfCreator;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.provider.insurance.aswata.dto.OrderConfirmResponseDto;
import com.ninelives.insurance.provider.insurance.aswata.dto.OrderResponseDto;
import com.ninelives.insurance.provider.insurance.aswata.dto.ResponseDto;
import com.ninelives.insurance.ref.ErrorCode;

@Service
public class InsuranceService {
	private static final Logger logger = LoggerFactory.getLogger(InsuranceService.class);
	
	@Autowired InsuranceProvider insuranceProvider;
	@Autowired Environment env;
	
	private boolean isAswataEnabled = true;
	
	
	public void orderPolicy(PolicyOrder order) throws AppInternalServerErrorException{
		if(isAswataEnabled){
			try {
				OrderResult result = insuranceProvider.orderPolicy(order);
				if(result !=null && result.isSuccess()){
					order.setPolicyNumber(result.getPolicyNumber());
					order.setProviderOrderNumber(result.getProviderOrderNumber());
					if(order.getProviderDownloadUrl()!=null){
						order.setProviderDownloadUrl(result.getProviderDownloadUrl());
					}
				}else{
					throw new AppInternalServerErrorException(ErrorCode.ERR4201_ORDER_PROVIDER_FAIL, "Permintaan tidak dapat diproses, terjadi error pada sistem");
				}
			} catch (IOException | StorageException e) {
				throw new AppInternalServerErrorException(ErrorCode.ERR4202_ORDER_PROVIDER_FILE_ERROR,
						"Permintaan tidak dapat diproses, terjadi error pada sistem");
			} catch (InsuranceProviderConnectDisabledException e) {
				throw new AppInternalServerErrorException(ErrorCode.ERR4203_ORDER_PROVIDER_CONNECT_DISABLED, "Permintaan tidak dapat diproses, terjadi error pada sistem");
			} catch (InsuranceProviderException e) {
				throw new AppInternalServerErrorException(ErrorCode.ERR4201_ORDER_PROVIDER_FAIL, "Permintaan tidak dapat diproses, terjadi error pada sistem");
			}
		}
	}
	
	public void orderConfirm(PolicyOrder order) throws AppInternalServerErrorException{
		//logger.info("Process order confirm, order:<{}>", order);
		
		try {
			OrderConfirmResult result  = insuranceProvider.orderConfirm(order);
			if(result !=null && result.isSuccess()){
				if(result.getProviderDownloadUrl()!=null){
					order.setProviderDownloadUrl(result.getProviderDownloadUrl());
				}
				if(result.getPolicyNumber()!=null){
					order.setPolicyNumber(result.getPolicyNumber());
				}
				if(result.getProviderOrderNumber()!=null){
					order.setProviderOrderNumber(result.getProviderOrderNumber());
				}
			}else{
				throw new AppInternalServerErrorException(ErrorCode.ERR4201_ORDER_PROVIDER_FAIL, "Permintaan tidak dapat diproses, terjadi error pada sistem");
			}	
		} catch (InsuranceProviderConnectDisabledException e) {
			throw new AppInternalServerErrorException(ErrorCode.ERR4203_ORDER_PROVIDER_CONNECT_DISABLED, "Permintaan tidak dapat diproses, terjadi error pada sistem");
		} catch (InsuranceProviderException e) {			
			throw new AppInternalServerErrorException(ErrorCode.ERR4201_ORDER_PROVIDER_FAIL, "Permintaan tidak dapat diproses, terjadi error pada sistem");
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
