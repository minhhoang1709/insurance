package com.ninelives.insurance.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataProviderAgeDto {
	
	
	private String ages;
	
	private String count;

	public String getAges() {
		return ages;
	}

	public void setAges(String ages) {
		this.ages = ages;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "DataProviderAgeDto [ages=" + ages + ", count=" + count + "]";
	}

	
	
    
}
