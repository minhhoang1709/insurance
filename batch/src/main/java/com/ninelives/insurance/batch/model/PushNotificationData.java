package com.ninelives.insurance.batch.model;

import com.ninelives.insurance.batch.ref.PushNotificationType;

public class PushNotificationData {
	private String userId;
	private String email;
	private String fcmToken;
	private String orderId;
	private String coverageCategoryId;
	private String coverageCategoryName;
	
	private PushNotificationType pushNotificationType;
		
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
	public String getFcmToken() {
		return fcmToken;
	}
	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public String getCoverageCategoryId() {
		return coverageCategoryId;
	}
	public void setCoverageCategoryId(String coverageCategoryId) {
		this.coverageCategoryId = coverageCategoryId;
	}
	public String getCoverageCategoryName() {
		return coverageCategoryName;
	}
	public void setCoverageCategoryName(String coverageCategoryName) {
		this.coverageCategoryName = coverageCategoryName;
	}
	public PushNotificationType getPushNotificationType() {
		return pushNotificationType;
	}
	public void setPushNotificationType(PushNotificationType pushNotificationType) {
		this.pushNotificationType = pushNotificationType;
	}
	
	@Override
	public String toString() {
		return "PushNotificationData [userId=" + userId + ", email=" + email + ", fcmToken=" + fcmToken + ", orderId="
				+ orderId + ", coverageCategoryId=" + coverageCategoryId + ", coverageCategoryName="
				+ coverageCategoryName + ", pushNotificationType=" + pushNotificationType + "]";
	}
	
}
