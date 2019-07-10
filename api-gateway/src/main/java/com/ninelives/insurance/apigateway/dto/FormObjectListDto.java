package com.ninelives.insurance.apigateway.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormObjectListDto {
	
	
	private List<FormObjectDto> listObj;

	public List<FormObjectDto> getListObj() {
		return listObj;
	}

	public void setListObj(List<FormObjectDto> listObj) {
		this.listObj = listObj;
	}

	@Override
	public String toString() {
		return "FormObjectListDto [listObj=" + listObj + "]";
	}
	
	
	
    
}
