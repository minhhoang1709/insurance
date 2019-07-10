package com.ninelives.insurance.apigateway.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimManagementListDto {
	
    private int iTotalRecords;
    
    private int iTotalDisplayRecords;
    
    private int sEcho;
    
    private String sColumns;
    
    private List<ClaimManagementDto> aaData;

	public int getiTotalRecords() {
		return iTotalRecords;
	}

	public void setiTotalRecords(int iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	public int getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	public int getsEcho() {
		return sEcho;
	}

	public void setsEcho(int sEcho) {
		this.sEcho = sEcho;
	}

	public String getsColumns() {
		return sColumns;
	}

	public void setsColumns(String sColumns) {
		this.sColumns = sColumns;
	}

	public List<ClaimManagementDto> getAaData() {
		return aaData;
	}

	public void setAaData(List<ClaimManagementDto> aaData) {
		this.aaData = aaData;
	}

	@Override
	public String toString() {
		return "ClaimManagementListDto [iTotalRecords=" + iTotalRecords + ", iTotalDisplayRecords="
				+ iTotalDisplayRecords + ", sEcho=" + sEcho + ", sColumns=" + sColumns + ", aaData=" + aaData + "]";
	}

	
	

}
