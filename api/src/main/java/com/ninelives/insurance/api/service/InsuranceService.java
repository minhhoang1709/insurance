package com.ninelives.insurance.api.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	public void orderPolicy(PolicyOrder order) throws ApiInternalServerErrorException{
		try {
			ResponseDto<OrderResponseDto> result = insuranceProvider.orderPolicy(order);
			if(insuranceProvider.isSuccess(result)){
				order.setPolicyNumber(result.getResponse().getResponseParam().getPolicyNumber());
				order.setProviderOrderNumber(result.getResponse().getResponseParam().getOderNumber());
			}else{
				throw new ApiInternalServerErrorException(ErrorCode.ERR4201_ORDER_PROVIDER_FAIL, "Permintaan tidak dapat diproses, terjadi error pada sistem");
			}
		} catch (IOException | StorageException e) {
			throw new ApiInternalServerErrorException(ErrorCode.ERR4201_ORDER_PROVIDER_FILE_ERROR,
					"Permintaan tidak dapat diproses, terjadi error pada sistem");
		}
	}
}
