package com.ninelives.insurance.api.model;

public class AuthToken {
	
	String tokenId;
	Integer expiry;
	String createdDateTimeStr;
	
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}	
	public Integer getExpiry() {
		return expiry;
	}
	public void setExpiry(Integer expiry) {
		this.expiry = expiry;
	}
	public String getCreatedDateTimeStr() {
		return createdDateTimeStr;
	}
	public void setCreatedDateTimeStr(String createdDateTimeStr) {
		this.createdDateTimeStr = createdDateTimeStr;
	}
	
}
