package com.ninelives.insurance.api.provider.insurance;

import java.io.IOException;

import com.ninelives.insurance.core.provider.storage.StorageException;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.provider.insurance.aswata.dto.OrderResponseDto;
import com.ninelives.insurance.provider.insurance.aswata.dto.ResponseDto;

public interface InsuranceProvider {
	public ResponseDto<OrderResponseDto> orderPolicy(PolicyOrder order) throws IOException, StorageException;
	public boolean isSuccess(ResponseDto<OrderResponseDto> result);
}
