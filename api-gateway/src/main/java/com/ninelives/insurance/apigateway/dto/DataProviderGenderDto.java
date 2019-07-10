package com.ninelives.insurance.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataProviderGenderDto {
	
	
	private String gender;
	
	private String count;

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "DataProviderGenderDto [gender=" + gender + ", count=" + count + "]";
	}
	
	
	
    
}
