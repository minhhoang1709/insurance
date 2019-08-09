package com.ninelives.insurance.provider.insurance.pti.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PtiResponeResultDto implements Serializable {

	private static final long serialVersionUID = -3160176755370949480L;

	@JsonProperty("c")
	private int resultCode;

	@JsonProperty("d")
	private String resultContent;

	@JsonProperty("m")
	private String resultDescription;

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultContent() {
		return resultContent;
	}

	public void setResultContent(String resultContent) {
		this.resultContent = resultContent;
	}

	public String getResultDescription() {
		return resultDescription;
	}

	public void setResultDescription(String resultDescription) {
		this.resultDescription = resultDescription;
	}

	@Override
	public String toString() {
		return "PtiResponeResultDto [resultCode=" + resultCode + ", resultContent=" + resultContent
				+ ", resultDescription=" + resultDescription + "]";
	}

}
