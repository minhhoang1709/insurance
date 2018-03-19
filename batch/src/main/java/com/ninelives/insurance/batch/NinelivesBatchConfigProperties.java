package com.ninelives.insurance.batch;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("ninelives-batch")
@Validated
public class NinelivesBatchConfigProperties {
	private String baseDir;
	
	private Boolean enablePushNotificationActiveOrderSchedule = true;
	
	private Boolean enablePushNotificationExpireOrderSchedule = true;
	
	private Boolean enablePushNotificationToBeExpireOrderSchedule = true;

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public Boolean getEnablePushNotificationActiveOrderSchedule() {
		return enablePushNotificationActiveOrderSchedule;
	}

	public void setEnablePushNotificationActiveOrderSchedule(Boolean enablePushNotificationActiveOrderSchedule) {
		this.enablePushNotificationActiveOrderSchedule = enablePushNotificationActiveOrderSchedule;
	}

	public Boolean getEnablePushNotificationExpireOrderSchedule() {
		return enablePushNotificationExpireOrderSchedule;
	}

	public void setEnablePushNotificationExpireOrderSchedule(Boolean enablePushNotificationExpireOrderSchedule) {
		this.enablePushNotificationExpireOrderSchedule = enablePushNotificationExpireOrderSchedule;
	}

	public Boolean getEnablePushNotificationToBeExpireOrderSchedule() {
		return enablePushNotificationToBeExpireOrderSchedule;
	}

	public void setEnablePushNotificationToBeExpireOrderSchedule(Boolean enablePushNotificationToBeExpireOrderSchedule) {
		this.enablePushNotificationToBeExpireOrderSchedule = enablePushNotificationToBeExpireOrderSchedule;
	}


}
