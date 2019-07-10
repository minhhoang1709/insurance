package com.ninelives.insurance.apigateway.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoucherRegisterDto {
	
	private String corporateClientId;
	
	private String corporateClientName;
	
	private String period;
	
	private String periodName;
	  
	private String b2bCode;
	  
	private String insuranceType;
	
	private String insuranceTypeName;
	  
	private String title;
	  
	private String subtitle;
	  
	private String description;
	
	private String coverage;
	
	private String policyStartDate;
	
	private String useStartDate;
	
	private String useEndDate;
	
	private String modifiedDate;
	
	private String voucherId;
	
    private String policyStartDateForm;
	
	private String useStartDateForm;
	
	private String useEndDateForm;
	
	private String enableEdit;
	
	private List<String> listCoverageId;

	public String getCorporateClientId() {
		return corporateClientId;
	}

	public void setCorporateClientId(String corporateClientId) {
		this.corporateClientId = corporateClientId;
	}

	public String getB2bCode() {
		return b2bCode;
	}

	public void setB2bCode(String b2bCode) {
		this.b2bCode = b2bCode;
	}

	public String getInsuranceType() {
		return insuranceType;
	}

	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public String getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(String policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public String getUseStartDate() {
		return useStartDate;
	}

	public void setUseStartDate(String useStartDate) {
		this.useStartDate = useStartDate;
	}

	public String getUseEndDate() {
		return useEndDate;
	}

	public void setUseEndDate(String useEndDate) {
		this.useEndDate = useEndDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Override
	public String toString() {
		return "VoucherRegisterDto [corporateClientId=" + corporateClientId + ", corporateClientName="
				+ corporateClientName + ", period=" + period + ", periodName=" + periodName + ", b2bCode=" + b2bCode
				+ ", insuranceType=" + insuranceType + ", insuranceTypeName=" + insuranceTypeName + ", title=" + title
				+ ", subtitle=" + subtitle + ", description=" + description + ", coverage=" + coverage
				+ ", policyStartDate=" + policyStartDate + ", useStartDate=" + useStartDate + ", useEndDate="
				+ useEndDate + ", modifiedDate=" + modifiedDate + ", voucherId=" + voucherId + ", policyStartDateForm="
				+ policyStartDateForm + ", useStartDateForm=" + useStartDateForm + ", useEndDateForm=" + useEndDateForm
				+ ", enableEdit=" + enableEdit + ", listCoverageId=" + listCoverageId + "]";
	}

	public String getCorporateClientName() {
		return corporateClientName;
	}

	public void setCorporateClientName(String corporateClientName) {
		this.corporateClientName = corporateClientName;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public List<String> getListCoverageId() {
		return listCoverageId;
	}

	public void setListCoverageId(List<String> listCoverageId) {
		this.listCoverageId = listCoverageId;
	}

	public String getVoucherId() {
		return voucherId;
	}

	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
	}

	public String getPolicyStartDateForm() {
		return policyStartDateForm;
	}

	public void setPolicyStartDateForm(String policyStartDateForm) {
		this.policyStartDateForm = policyStartDateForm;
	}

	public String getUseStartDateForm() {
		return useStartDateForm;
	}

	public void setUseStartDateForm(String useStartDateForm) {
		this.useStartDateForm = useStartDateForm;
	}

	public String getUseEndDateForm() {
		return useEndDateForm;
	}

	public void setUseEndDateForm(String useEndDateForm) {
		this.useEndDateForm = useEndDateForm;
	}

	public String getEnableEdit() {
		return enableEdit;
	}

	public void setEnableEdit(String enableEdit) {
		this.enableEdit = enableEdit;
	}

	public String getPeriodName() {
		return periodName;
	}

	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}

	public String getInsuranceTypeName() {
		return insuranceTypeName;
	}

	public void setInsuranceTypeName(String insuranceTypeName) {
		this.insuranceTypeName = insuranceTypeName;
	}
	
	
}
