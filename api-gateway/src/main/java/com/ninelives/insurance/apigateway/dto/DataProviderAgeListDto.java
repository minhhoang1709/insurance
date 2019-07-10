package com.ninelives.insurance.apigateway.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataProviderAgeListDto {
	
	
	private List<DataProviderAgeDto> dataProvider;

	public List<DataProviderAgeDto> getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(List<DataProviderAgeDto> dataProvider) {
		this.dataProvider = dataProvider;
	}

	@Override
	public String toString() {
		return "DataProviderAgeListDto [dataProvider=" + dataProvider + "]";
	}

	
    
}
