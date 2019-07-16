package com.ninelives.insurance.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserB2bOrderStatusDto {
    
	private String orderStatus;
	
	private String orderId;
	
	private String userId;
	
	private String userName;
	
	private String errMsgs;

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getErrMsgs() {
		return errMsgs;
	}

	public void setErrMsgs(String errMsgs) {
		this.errMsgs = errMsgs;
	}

	@Override
	public String toString() {
		return "UserB2bOrderStatusDto [orderStatus=" + orderStatus + ", orderId=" + orderId + ", userId=" + userId
				+ ", userName=" + userName + ", errMsgs=" + errMsgs + "]";
	}

	

    
}
