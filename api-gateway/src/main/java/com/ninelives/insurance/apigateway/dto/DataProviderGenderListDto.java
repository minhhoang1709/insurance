package com.ninelives.insurance.apigateway.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataProviderGenderListDto {
	
	
	private List<DataProviderGenderDto> dataProvider;

	public List<DataProviderGenderDto> getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(List<DataProviderGenderDto> dataProvider) {
		this.dataProvider = dataProvider;
	}

	@Override
	public String toString() {
		return "DataProviderGenderListDto [dataProvider=" + dataProvider + "]";
	}
	
	
	
	
    
}
