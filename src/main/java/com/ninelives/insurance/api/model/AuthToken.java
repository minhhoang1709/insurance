package com.ninelives.insurance.api.model;

import java.time.LocalDateTime;
import java.util.Date;

public class AuthToken {
	
	String tokenId;
	String userId;
	String createdDateTimeStr;
	
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCreatedDateTimeStr() {
		return createdDateTimeStr;
	}
	public void setCreatedDateTimeStr(String createdDateTimeStr) {
		this.createdDateTimeStr = createdDateTimeStr;
	}
	
}
