package com.ninelives.insurance.apigateway.model;

import com.ninelives.insurance.model.UserCms;

public class AuthTokenCms {
	
	String tokenId;
	Integer expiry;
	String createdDateTimeStr;
	
	UserCms userCms;

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

	public UserCms getUserCms() {
		return userCms;
	}

	public void setUserCms(UserCms userCms) {
		this.userCms = userCms;
	}
	
	
}
