package com.ninelives.insurance.insurer;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


@ConfigurationProperties("ninelives-insurer")
@Validated
public class NinelivesInsurerConfigProperties {
	@Valid
	private Insurance insurance = new Insurance();
	
	public Insurance getInsurance() {
		return insurance;
	}

	public void setInsurance(Insurance insurance) {
		this.insurance = insurance;
	}

	public static class Insurance {
		@NotEmpty
		private String aswataClientCode;
		@NotEmpty
		private String aswataClientKey;
		@NotEmpty
		private String aswataProductCode;
		@NotEmpty
		private String aswataPackageType;
		@NotEmpty
		private String aswataUrl;
		
		private int aswataConnectionPoolSize = 32;
		private int aswataPoolTimeout = 5000;
		private int aswataConnectTimeout = 5000;
		private int aswataSocketTimeout = 30000;
		
		public String getAswataClientCode() {
			return aswataClientCode;
		}
		public void setAswataClientCode(String aswataClientCode) {
			this.aswataClientCode = aswataClientCode;
		}
		public String getAswataClientKey() {
			return aswataClientKey;
		}
		public void setAswataClientKey(String aswataClientKey) {
			this.aswataClientKey = aswataClientKey;
		}		
		public String getAswataProductCode() {
			return aswataProductCode;
		}
		public void setAswataProductCode(String aswataProductCode) {
			this.aswataProductCode = aswataProductCode;
		}
		public String getAswataPackageType() {
			return aswataPackageType;
		}
		public void setAswataPackageType(String aswataPackageType) {
			this.aswataPackageType = aswataPackageType;
		}		
		public String getAswataUrl() {
			return aswataUrl;
		}
		public void setAswataUrl(String aswataUrl) {
			this.aswataUrl = aswataUrl;
		}
		public int getAswataConnectionPoolSize() {
			return aswataConnectionPoolSize;
		}
		public void setAswataConnectionPoolSize(int aswataConnectionPoolSize) {
			this.aswataConnectionPoolSize = aswataConnectionPoolSize;
		}
		public int getAswataPoolTimeout() {
			return aswataPoolTimeout;
		}
		public void setAswataPoolTimeout(int aswataPoolTimeout) {
			this.aswataPoolTimeout = aswataPoolTimeout;
		}
		public int getAswataConnectTimeout() {
			return aswataConnectTimeout;
		}
		public void setAswataConnectTimeout(int aswataConnectTimeout) {
			this.aswataConnectTimeout = aswataConnectTimeout;
		}
		public int getAswataSocketTimeout() {
			return aswataSocketTimeout;
		}
		public void setAswataSocketTimeout(int aswataSocketTimeout) {
			this.aswataSocketTimeout = aswataSocketTimeout;
		}
	}
}
