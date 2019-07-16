package com.ninelives.insurance.apigateway.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserB2bCodeDto {
	
	private String name;
	
	private String gender;
	
	private String orderTime;
	
	private String orderDate;
	
	private String userId;
	
	private String email;
	
	private String orderId;
	
	private String voucherId;
	
	private String voucherCode;
	
	private String phone;
	
	private String birthDate;

	private String orderStatus;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getVoucherId() {
		return voucherId;
	}

	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
	}

	public String getVoucherCode() {
		return voucherCode;
	}

	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Override
	public String toString() {
		return "UserB2bCodeDto [name=" + name + ", gender=" + gender + ", orderTime=" + orderTime + ", orderDate="
				+ orderDate + ", userId=" + userId + ", email=" + email + ", orderId=" + orderId + ", voucherId="
				+ voucherId + ", voucherCode=" + voucherCode + ", phone=" + phone + ", birthDate=" + birthDate
				+ ", orderStatus=" + orderStatus + "]";
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	
	
}
