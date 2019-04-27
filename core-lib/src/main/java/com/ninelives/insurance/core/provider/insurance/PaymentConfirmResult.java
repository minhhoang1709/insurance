package com.ninelives.insurance.core.provider.insurance;

public class PaymentConfirmResult {
	boolean isSuccess;
	String policyNumber;
	String providerOrderNumber;
	String providerDownloadUrl;
	
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getProviderOrderNumber() {
		return providerOrderNumber;
	}
	public void setProviderOrderNumber(String providerOrderNumber) {
		this.providerOrderNumber = providerOrderNumber;
	}
	public String getProviderDownloadUrl() {
		return providerDownloadUrl;
	}
	public void setProviderDownloadUrl(String providerDownloadUrl) {
		this.providerDownloadUrl = providerDownloadUrl;
	}
}
