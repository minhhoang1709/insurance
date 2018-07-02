package com.ninelives.insurance.api.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDto {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime paymentChargeDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime paymentExpiryDate;
	private String expiryDuration;
	
	public LocalDateTime getPaymentChargeDate() {
		return paymentChargeDate;
	}
	public void setPaymentChargeDate(LocalDateTime paymentChargeDate) {
		this.paymentChargeDate = paymentChargeDate;
	}
	public LocalDateTime getPaymentExpiryDate() {
		return paymentExpiryDate;
	}
	public void setPaymentExpiryDate(LocalDateTime paymentExpiryDate) {
		this.paymentExpiryDate = paymentExpiryDate;
	}
	public String getExpiryDuration() {
		return expiryDuration;
	}
	public void setExpiryDuration(String expiryDuration) {
		this.expiryDuration = expiryDuration;
	}
	
}
