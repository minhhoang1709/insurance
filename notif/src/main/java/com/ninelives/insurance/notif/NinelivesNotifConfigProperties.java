package com.ninelives.insurance.notif;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("ninelives-notif")
@Validated
public class NinelivesNotifConfigProperties {
	private Fcm fcm;
	
	public Fcm getFcm() {
		return fcm;
	}

	public void setFcm(Fcm fcm) {
		this.fcm = fcm;
	}

	public static class Fcm{
		private int connectionPoolSize = 16;
		private int poolTimeout = 5000;
		private int connectTimeout = 5000;
		private int socketTimeout = 20000;
		
		public int getConnectionPoolSize() {
			return connectionPoolSize;
		}
		public void setConnectionPoolSize(int connectionPoolSize) {
			this.connectionPoolSize = connectionPoolSize;
		}
		public int getPoolTimeout() {
			return poolTimeout;
		}
		public void setPoolTimeout(int poolTimeout) {
			this.poolTimeout = poolTimeout;
		}
		public int getConnectTimeout() {
			return connectTimeout;
		}
		public void setConnectTimeout(int connectTimeout) {
			this.connectTimeout = connectTimeout;
		}
		public int getSocketTimeout() {
			return socketTimeout;
		}
		public void setSocketTimeout(int socketTimeout) {
			this.socketTimeout = socketTimeout;
		}
		
		
	}
	
}
