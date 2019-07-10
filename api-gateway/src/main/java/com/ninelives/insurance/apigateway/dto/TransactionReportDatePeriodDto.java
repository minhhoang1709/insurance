package com.ninelives.insurance.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionReportDatePeriodDto {
	
	
	private String histDate;
	
	private String unpaid;
	
	private String paid;
	
	private String active;
	
	private String expired;

	public String getHistDate() {
		return histDate;
	}

	public void setHistDate(String histDate) {
		this.histDate = histDate;
	}

	public String getUnpaid() {
		return unpaid;
	}

	public void setUnpaid(String unpaid) {
		this.unpaid = unpaid;
	}

	public String getPaid() {
		return paid;
	}

	public void setPaid(String paid) {
		this.paid = paid;
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
		return "TransactionReportDatePeriodDto [histDate=" + histDate + ", unpaid=" + unpaid + ", paid=" + paid
				+ ", active=" + active + ", expired=" + expired + "]";
	}
	
    
}
