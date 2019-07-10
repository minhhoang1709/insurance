package com.ninelives.insurance.apigateway.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class B2bTransactionData {
	
    private List<B2bTransactionListDto> data;

	public List<B2bTransactionListDto> getData() {
		return data;
	}

	public void setData(List<B2bTransactionListDto> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "B2bTransactionData [data=" + data + "]";
	}

	
	
}
