package com.ninelives.insurance.provider.payment.midtrans.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Payment2c2pNotificationDto {
	
	
	@JsonProperty("version")
	public String version;
	
	@JsonProperty("request_timestamp")
	public String requestTimestamp;
	
	@JsonProperty("merchant_id")
	public String merchantId;
	
	@JsonProperty("order_id")
	public String orderId;
	
	@JsonProperty("invoice_no")
	public String invoiceNo;
	
	@JsonProperty("currency")
	public String currency;
	
	@JsonProperty("amount")
	public String amount;
	
	@JsonProperty("transaction_ref")
	public String transactionRef;

	@JsonProperty("approval_code")
	public String approvalCode;

	@JsonProperty("eci")
	public String eci;
	
	@JsonProperty("browser_info")
	public String browserInfo;

	@JsonProperty("transaction_datetime")
	public String transactionDatetime;

	@JsonProperty("payment_channel")
	public String paymentChannel;

	@JsonProperty("payment_status")
	public String paymentStatus;

	@JsonProperty("channel_response_code")
	public String channelResponseCode;
	
	@JsonProperty("channel_response_desc")
	public String channelResponseDesc;

	@JsonProperty("masked_pan")
	public String maskedPan;

	
	@JsonProperty("stored_card_unique_id")
	public String storedCardUniqueId;
	
	@JsonProperty("backend_invoice")
	public String backendInvoice;
	
	@JsonProperty("paid_channel")
	public String paidChannel;
	
	@JsonProperty("paid_agent")
	public String paidAgent;
	
	@JsonProperty("recurring_unique_id")
	public String recurringUniqueId;
	
	@JsonProperty("user_defined_1")
	public String userDefined1;
	
	@JsonProperty("user_defined_2")
	public String userDefined2;
	
	@JsonProperty("user_defined_3")
	public String userDefined3;
	
	@JsonProperty("user_defined_4")
	public String userDefined4;
	
	@JsonProperty("user_defined_5")
	public String userDefined5;
	
	@JsonProperty("ippPeriod")
	public String ippPeriod;
	
	@JsonProperty("ippInterestType")
	public String ippInterestType;
	
	@JsonProperty("ippInterestRate")
	public String ippInterestRate;
	
	@JsonProperty("ippMerchantAbsorbRate")
	public String ippMerchantAbsorbRate;
	
	@JsonProperty("payment_scheme")
	public String paymentScheme;
	
	@JsonProperty("process_by")
	public String processBy;
	
	@JsonProperty("hash_value")
	public String hashValue;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getRequestTimestamp() {
		return requestTimestamp;
	}

	public void setRequestTimestamp(String requestTimestamp) {
		this.requestTimestamp = requestTimestamp;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTransactionRef() {
		return transactionRef;
	}

	public void setTransactionRef(String transactionRef) {
		this.transactionRef = transactionRef;
	}

	public String getApprovalCode() {
		return approvalCode;
	}

	public void setApprovalCode(String approvalCode) {
		this.approvalCode = approvalCode;
	}

	public String getEci() {
		return eci;
	}

	public void setEci(String eci) {
		this.eci = eci;
	}

	public String getTransactionDatetime() {
		return transactionDatetime;
	}

	public void setTransactionDatetime(String transactionDatetime) {
		this.transactionDatetime = transactionDatetime;
	}

	public String getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getChannelResponseCode() {
		return channelResponseCode;
	}

	public void setChannelResponseCode(String channelResponseCode) {
		this.channelResponseCode = channelResponseCode;
	}

	public String getChannelResponseDesc() {
		return channelResponseDesc;
	}

	public void setChannelResponseDesc(String channelResponseDesc) {
		this.channelResponseDesc = channelResponseDesc;
	}

	public String getMaskedPan() {
		return maskedPan;
	}

	public void setMaskedPan(String maskedPan) {
		this.maskedPan = maskedPan;
	}

	public String getStoredCardUniqueId() {
		return storedCardUniqueId;
	}

	public void setStoredCardUniqueId(String storedCardUniqueId) {
		this.storedCardUniqueId = storedCardUniqueId;
	}

	public String getBackendInvoice() {
		return backendInvoice;
	}

	public void setBackendInvoice(String backendInvoice) {
		this.backendInvoice = backendInvoice;
	}

	public String getPaidChannel() {
		return paidChannel;
	}

	public void setPaidChannel(String paidChannel) {
		this.paidChannel = paidChannel;
	}

	public String getPaidAgent() {
		return paidAgent;
	}

	public void setPaidAgent(String paidAgent) {
		this.paidAgent = paidAgent;
	}

	public String getRecurringUniqueId() {
		return recurringUniqueId;
	}

	public void setRecurringUniqueId(String recurringUniqueId) {
		this.recurringUniqueId = recurringUniqueId;
	}

	public String getUserDefined1() {
		return userDefined1;
	}

	public void setUserDefined1(String userDefined1) {
		this.userDefined1 = userDefined1;
	}

	public String getUserDefined2() {
		return userDefined2;
	}

	public void setUserDefined2(String userDefined2) {
		this.userDefined2 = userDefined2;
	}

	public String getUserDefined3() {
		return userDefined3;
	}

	public void setUserDefined3(String userDefined3) {
		this.userDefined3 = userDefined3;
	}

	public String getUserDefined4() {
		return userDefined4;
	}

	public void setUserDefined4(String userDefined4) {
		this.userDefined4 = userDefined4;
	}

	public String getUserDefined5() {
		return userDefined5;
	}

	public void setUserDefined5(String userDefined5) {
		this.userDefined5 = userDefined5;
	}

	public String getIppPeriod() {
		return ippPeriod;
	}

	public void setIppPeriod(String ippPeriod) {
		this.ippPeriod = ippPeriod;
	}

	public String getIppInterestType() {
		return ippInterestType;
	}

	public void setIppInterestType(String ippInterestType) {
		this.ippInterestType = ippInterestType;
	}

	public String getIppInterestRate() {
		return ippInterestRate;
	}

	public void setIppInterestRate(String ippInterestRate) {
		this.ippInterestRate = ippInterestRate;
	}

	public String getIppMerchantAbsorbRate() {
		return ippMerchantAbsorbRate;
	}

	public void setIppMerchantAbsorbRate(String ippMerchantAbsorbRate) {
		this.ippMerchantAbsorbRate = ippMerchantAbsorbRate;
	}

	public String getPaymentScheme() {
		return paymentScheme;
	}

	public void setPaymentScheme(String paymentScheme) {
		this.paymentScheme = paymentScheme;
	}

	public String getProcessBy() {
		return processBy;
	}

	public void setProcessBy(String processBy) {
		this.processBy = processBy;
	}

	public String getHashValue() {
		return hashValue;
	}

	public void setHashValue(String hashValue) {
		this.hashValue = hashValue;
	}

	@Override
	public String toString() {
		return "Payment2c2pNotificationDto [version=" + version + ", requestTimestamp=" + requestTimestamp
				+ ", merchantId=" + merchantId + ", orderId=" + orderId + ", invoiceNo=" + invoiceNo + ", currency="
				+ currency + ", amount=" + amount + ", transactionRef=" + transactionRef + ", approvalCode="
				+ approvalCode + ", eci=" + eci + ", browserInfo=" + browserInfo + ", transactionDatetime="
				+ transactionDatetime + ", paymentChannel=" + paymentChannel + ", paymentStatus=" + paymentStatus
				+ ", channelResponseCode=" + channelResponseCode + ", channelResponseDesc=" + channelResponseDesc
				+ ", maskedPan=" + maskedPan + ", storedCardUniqueId=" + storedCardUniqueId + ", backendInvoice="
				+ backendInvoice + ", paidChannel=" + paidChannel + ", paidAgent=" + paidAgent + ", recurringUniqueId="
				+ recurringUniqueId + ", userDefined1=" + userDefined1 + ", userDefined2=" + userDefined2
				+ ", userDefined3=" + userDefined3 + ", userDefined4=" + userDefined4 + ", userDefined5=" + userDefined5
				+ ", ippPeriod=" + ippPeriod + ", ippInterestType=" + ippInterestType + ", ippInterestRate="
				+ ippInterestRate + ", ippMerchantAbsorbRate=" + ippMerchantAbsorbRate + ", paymentScheme="
				+ paymentScheme + ", processBy=" + processBy + ", hashValue=" + hashValue + "]";
	}

	public String getBrowserInfo() {
		return browserInfo;
	}

	public void setBrowserInfo(String browserInfo) {
		this.browserInfo = browserInfo;
	}

	

}
