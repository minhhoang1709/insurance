package com.ninelives.insurance.apigateway.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class B2bReportListDto {
	
    private int iTotalRecords;
    
    private int iTotalDisplayRecords;
    
    private int sEcho;
    
    private String sColumns;
    
    private List<B2bReportDto> aaData;

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

	public List<B2bReportDto> getAaData() {
		return aaData;
	}

	public void setAaData(List<B2bReportDto> aaData) {
		this.aaData = aaData;
	}

	@Override
	public String toString() {
		return "B2bReportListDto [iTotalRecords=" + iTotalRecords + ", iTotalDisplayRecords=" + iTotalDisplayRecords
				+ ", sEcho=" + sEcho + ", sColumns=" + sColumns + ", aaData=" + aaData + "]";
	}

	

}
