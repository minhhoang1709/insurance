package com.ninelives.insurance.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FreeInsuranceReportDto {
	
	
	private String voucherCode;
	
	private String active;
	
	private String expired;

	public String getVoucherCode() {
		return voucherCode;
	}

	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getExpired() {
		return expired;
	}

	public void setExpired(String expired) {
		this.expired = expired;
	}

	@Override
	public String toString() {
		return "FreeInsuranceReportDto [voucherCode=" + voucherCode + ", active=" + active + ", expired=" + expired
				+ "]";
	}

	
}
