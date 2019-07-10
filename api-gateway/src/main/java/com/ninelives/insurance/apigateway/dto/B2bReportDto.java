package com.ninelives.insurance.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class B2bReportDto {
	
	
	private String corporateClientName;
	
	private String b2bCode;
	
	private String channel;
	
	private String order;
		
	private String orderConfirm;
	
	private String active;
	
	private String expired;

	public String getCorporateClientName() {
		return corporateClientName;
	}

	public void setCorporateClientName(String corporateClientName) {
		this.corporateClientName = corporateClientName;
	}

	public String getB2bCode() {
		return b2bCode;
	}

	public void setB2bCode(String b2bCode) {
		this.b2bCode = b2bCode;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrderConfirm() {
		return orderConfirm;
	}

	public void setOrderConfirm(String orderConfirm) {
		this.orderConfirm = orderConfirm;
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
		return "B2bReportDto [corporateClientName=" + corporateClientName + ", b2bCode=" + b2bCode + ", channel="
				+ channel + ", order=" + order + ", orderConfirm=" + orderConfirm + ", active=" + active + ", expired="
				+ expired + "]";
	}

	
    
}
