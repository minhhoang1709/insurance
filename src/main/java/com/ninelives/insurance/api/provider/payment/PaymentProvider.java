package com.ninelives.insurance.api.provider.payment;

import com.ninelives.insurance.api.dto.ChargeDto;
import com.ninelives.insurance.api.dto.ChargeResponseDto;
import com.ninelives.insurance.api.exception.ApiException;

public interface PaymentProvider {
	public ChargeResponseDto charge(ChargeDto chargeDto) throws ApiException;
}
