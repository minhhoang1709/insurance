package com.ninelives.insurance.core.provider.payment;

import com.ninelives.insurance.provider.payment.midtrans.dto.ChargeDto;
import com.ninelives.insurance.provider.payment.midtrans.dto.ChargeResponseDto;

public interface PaymentProvider {
	public ChargeResponseDto charge(ChargeDto chargeDto);
}
