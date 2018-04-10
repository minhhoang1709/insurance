package com.ninelives.insurance.core.provider.insurance;

import java.io.IOException;

import com.ninelives.insurance.core.provider.storage.StorageException;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.provider.insurance.aswata.dto.IAswataResponsePayload;
import com.ninelives.insurance.provider.insurance.aswata.dto.OrderConfirmResponseDto;
import com.ninelives.insurance.provider.insurance.aswata.dto.OrderResponseDto;
import com.ninelives.insurance.provider.insurance.aswata.dto.ResponseDto;

public interface InsuranceProvider {
	public ResponseDto<OrderResponseDto> orderPolicy(PolicyOrder order) throws InsuranceProviderException, IOException, StorageException;
	public ResponseDto<OrderConfirmResponseDto> orderConfirm(PolicyOrder order) throws InsuranceProviderException;
	//public boolean isSuccess(ResponseDto<IAswataResponsePayload> result);
	//public boolean isSuccess(ResponseDto<OrderConfirmResponseDto> result);
}
