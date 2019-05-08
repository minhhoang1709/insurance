package com.ninelives.insurance.core.provider.insurance;

import java.io.IOException;

import com.ninelives.insurance.core.provider.storage.StorageException;
import com.ninelives.insurance.model.PolicyOrder;

public interface InsuranceProvider {
	public OrderResult orderPolicy(PolicyOrder order) throws InsuranceProviderException, IOException, StorageException;
	public OrderConfirmResult orderConfirm(PolicyOrder order) throws InsuranceProviderException, IOException, StorageException;
	public PaymentConfirmResult paymentConfirm(PolicyOrder order) throws InsuranceProviderException, IOException, StorageException;
}
