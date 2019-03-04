package com.ninelives.insurance.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginDto {
	private String accessToken;
	private UserDto user;
	private Boolean requirePasswordChange;
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public UserDto getUser() {
		return user;
	}
	public void setUser(UserDto user) {
		this.user = user;
	}
	public Boolean getRequirePasswordChange() {
		return requirePasswordChange;
	}
	public void setRequirePasswordChange(Boolean requirePasswordChange) {
		this.requirePasswordChange = requirePasswordChange;
	}
	
}
