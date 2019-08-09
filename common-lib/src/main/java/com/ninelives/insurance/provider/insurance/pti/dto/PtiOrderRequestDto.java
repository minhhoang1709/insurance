package com.ninelives.insurance.provider.insurance.pti.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PtiOrderRequestDto implements Serializable {

	private static final long serialVersionUID = -3189597979729682895L;

	@JsonProperty("serviceId")
	private int serviceId;

	@JsonProperty("type")
	private int insuranceType;

	@JsonProperty("info")
	private String requestInfo;

	@JsonProperty("hash")
	private String sha1Hash;

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public int getInsuranceType() {
		return insuranceType;
	}

	public void setInsuranceType(int insuranceType) {
		this.insuranceType = insuranceType;
	}

	public String getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(String requestInfo) {
		this.requestInfo = requestInfo;
	}

	public String getSha1Hash() {
		return sha1Hash;
	}

	public void setSha1Hash(String sha1Hash) {
		this.sha1Hash = sha1Hash;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
