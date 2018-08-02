package com.ninelives.insurance.apigateway.model;

public class ApiSessionData {
	String userId;
	String tokenCreatedDateTimeStr;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTokenCreatedDateTimeStr() {
		return tokenCreatedDateTimeStr;
	}
	public void setTokenCreatedDateTimeStr(String tokenCreatedDateTimeStr) {
		this.tokenCreatedDateTimeStr = tokenCreatedDateTimeStr;
	}
	@Override
	public String toString() {
		return "ApiSessionData [userId=" + userId + ", tokenCreatedDateTimeStr=" + tokenCreatedDateTimeStr + "]";
	}
	
}
