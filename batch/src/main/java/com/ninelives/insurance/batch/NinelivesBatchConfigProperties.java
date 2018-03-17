package com.ninelives.insurance.batch;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("ninelives-batch")
@Validated
public class NinelivesBatchConfigProperties {
	private String baseDir;

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}
	
}
