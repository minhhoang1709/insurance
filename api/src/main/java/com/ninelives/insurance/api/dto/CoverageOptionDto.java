package com.ninelives.insurance.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoverageOptionDto {
	private String coverageOptionId;
	private String coverageOptionName;
	private String coverageOptionGroupId;
	public String getCoverageOptionId() {
		return coverageOptionId;
	}
	public void setCoverageOptionId(String coverageOptionId) {
		this.coverageOptionId = coverageOptionId;
	}
	public String getCoverageOptionName() {
		return coverageOptionName;
	}
	public void setCoverageOptionName(String coverageOptionName) {
		this.coverageOptionName = coverageOptionName;
	}
	public String getCoverageOptionGroupId() {
		return coverageOptionGroupId;
	}
	public void setCoverageOptionGroupId(String coverageOptionGroupId) {
		this.coverageOptionGroupId = coverageOptionGroupId;
	}
	
}
