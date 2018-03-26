package com.ninelives.insurance.api.dto;

public class RegistrationDto {
	String source;
	String deviceId;
	String googleId;
	String googleName;
	String googleEmail;
	String googleServerAuth;
	String googleToken;
	String fcmToken;
	String name;
	String password;
	Boolean isSyncGmailEnabled;
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getGoogleId() {
		return googleId;
	}
	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}
	public String getGoogleName() {
		return googleName;
	}
	public void setGoogleName(String googleName) {
		this.googleName = googleName;
	}
	public String getGoogleEmail() {
		return googleEmail;
	}
	public void setGoogleEmail(String googleEmail) {
		this.googleEmail = googleEmail;
	}
	public String getGoogleServerAuth() {
		return googleServerAuth;
	}
	public void setGoogleServerAuth(String googleServerAuth) {
		this.googleServerAuth = googleServerAuth;
	}
	public String getGoogleToken() {
		return googleToken;
	}
	public void setGoogleToken(String googleToken) {
		this.googleToken = googleToken;
	}	
	public String getFcmToken() {
		return fcmToken;
	}
	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getIsSyncGmailEnabled() {
		return isSyncGmailEnabled;
	}
	public void setIsSyncGmailEnabled(Boolean isSyncGmailEnabled) {
		this.isSyncGmailEnabled = isSyncGmailEnabled;
	}
	@Override
	public String toString() {
		return "RegistrationDto [source=" + source + ", deviceId=" + deviceId + ", googleId=" + googleId
				+ ", googleName=" + googleName + ", googleEmail=" + googleEmail + ", googleServerAuth="
				+ googleServerAuth + ", googleToken=" + googleToken + ", fcmToken=" + fcmToken + ", name=" + name
				+ ", password=***" + "" + ", isSyncGmailEnabled=" + isSyncGmailEnabled + "]";
	}
	
}
