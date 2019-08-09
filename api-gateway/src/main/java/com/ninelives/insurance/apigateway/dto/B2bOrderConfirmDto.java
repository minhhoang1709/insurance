package com.ninelives.insurance.apigateway.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class B2bOrderConfirmDto {
	
	
	private String voucherCode;
	
	private String paid;
	
	private String terminated;
	
	private String approved;

	

	public String getVoucherCode() {
		return voucherCode;
	}

	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}

	public String getPaid() {
		return paid;
	}

	public void setPaid(String paid) {
		this.paid = paid;
	}

	public String getTerminated() {
		return terminated;
	}

	public void setTerminated(String terminated) {
		this.terminated = terminated;
	}

	public String getApproved() {
		return approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	@Override
	public String toString() {
		return "B2bOrderConfirmDto [voucherCode=" + voucherCode + ", paid=" + paid
				+ ", terminated=" + terminated + ", approved=" + approved + "]";
	}
	
}
