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
import com.ninelives.insurance.core.provider.insurance.InsuranceProviderFactory;
import com.ninelives.insurance.core.provider.insurance.OrderConfirmResult;
import com.ninelives.insurance.core.provider.insurance.OrderResult;
import com.ninelives.insurance.core.provider.insurance.PaymentConfirmResult;
import com.ninelives.insurance.core.provider.insurance.PtiInsuranceProvider;
import com.ninelives.insurance.core.provider.storage.StorageException;
import com.ninelives.insurance.core.support.pdf.PdfCreator;
import com.ninelives.insurance.model.CoverageCategory;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.provider.insurance.aswata.dto.OrderConfirmResponseDto;
import com.ninelives.insurance.provider.insurance.aswata.dto.OrderResponseDto;
import com.ninelives.insurance.provider.insurance.aswata.dto.PaymentConfirmResponseDto;
import com.ninelives.insurance.provider.insurance.aswata.dto.ResponseDto;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.PolicyStatus;

@Service
public class InsuranceService {
	private static final Logger logger = LoggerFactory.getLogger(InsuranceService.class);
	
	//@Autowired InsuranceProvider insuranceProvider;
	@Autowired InsuranceProviderFactory insuranceProviderFactory;
	@Autowired ProductService productService;
	
	
	public void orderPolicy(PolicyOrder order) throws AppInternalServerErrorException {
		try {
			InsuranceProvider provider = null;
			if (order.getCoverageCategory() != null && order.getCoverageCategory().getInsurer() != null) {
				provider = insuranceProviderFactory
						.getInsuranceProvider(order.getCoverageCategory().getInsurer().getCode());
			} else {
				CoverageCategory cov = productService
						.fetchCoverageCategoryByCoverageCategoryId(order.getCoverageCategoryId());
				provider = insuranceProviderFactory.getInsuranceProvider(cov.getInsurer().getCode());
			}

			OrderResult result = provider.orderPolicy(order);
			if (result != null && result.isSuccess()) {
				order.setPolicyNumber(result.getPolicyNumber());
				order.setProviderOrderNumber(result.getProviderOrderNumber());
				if (result.getProviderDownloadUrl() != null) {
					order.setProviderDownloadUrl(result.getProviderDownloadUrl());
				}
			} else {
				throw new AppInternalServerErrorException(ErrorCode.ERR4201_ORDER_PROVIDER_FAIL,
						"Permintaan tidak dapat diproses, terjadi error pada sistem");
			}
		} catch (IOException | StorageException e) {
			throw new AppInternalServerErrorException(ErrorCode.ERR4202_ORDER_PROVIDER_FILE_ERROR,
					"Permintaan tidak dapat diproses, terjadi error pada sistem");
		} catch (InsuranceProviderConnectDisabledException e) {
			throw new AppInternalServerErrorException(ErrorCode.ERR4203_ORDER_PROVIDER_CONNECT_DISABLED,
					"Permintaan tidak dapat diproses, terjadi error pada sistem");
		} catch (InsuranceProviderException e) {
			throw new AppInternalServerErrorException(ErrorCode.ERR4201_ORDER_PROVIDER_FAIL,
					"Permintaan tidak dapat diproses, terjadi error pada sistem");
		}
	}
	
	public void orderConfirm(PolicyOrder order) throws AppInternalServerErrorException{
		try {
			InsuranceProvider provider = null;
			if (order.getCoverageCategory() != null && order.getCoverageCategory().getInsurer() != null) {
				provider = insuranceProviderFactory
						.getInsuranceProvider(order.getCoverageCategory().getInsurer().getCode());
			} else {
				CoverageCategory cov = productService
						.fetchCoverageCategoryByCoverageCategoryId(order.getCoverageCategoryId());
				provider = insuranceProviderFactory.getInsuranceProvider(cov.getInsurer().getCode());
			}
			OrderConfirmResult result  = provider.orderConfirm(order);
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
	
	public void paymentConfirm(PolicyOrder order) throws AppInternalServerErrorException{
		try {
			InsuranceProvider provider = null;
			if (order.getCoverageCategory() != null && order.getCoverageCategory().getInsurer() != null) {
				provider = insuranceProviderFactory
						.getInsuranceProvider(order.getCoverageCategory().getInsurer().getCode());
			} else {
				CoverageCategory cov = productService
						.fetchCoverageCategoryByCoverageCategoryId(order.getCoverageCategoryId());
				provider = insuranceProviderFactory.getInsuranceProvider(cov.getInsurer().getCode());
			}
			PaymentConfirmResult result  = provider.paymentConfirm(order);
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
	
}
