package com.ninelives.insurance.apigateway.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimStatusDto {
    
	private String claimId;
	
	private String claimStatus;
	
	private List<PolicyCoverageItem> policyCoverageItem;
	
	private String userName;
	
	private String errMsgs;

	public String getClaimId() {
		return claimId;
	}

	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getErrMsgs() {
		return errMsgs;
	}

	public void setErrMsgs(String errMsgs) {
		this.errMsgs = errMsgs;
	}

	public List<PolicyCoverageItem> getPolicyCoverageItem() {
		return policyCoverageItem;
	}

	public void setPolicyCoverageItem(List<PolicyCoverageItem> policyCoverageItem) {
		this.policyCoverageItem = policyCoverageItem;
	}

	@Override
	public String toString() {
		return "ClaimStatusDto [claimId=" + claimId + ", claimStatus=" + claimStatus + ", policyCoverageItem="
				+ policyCoverageItem + ", userName=" + userName + ", errMsgs=" + errMsgs + "]";
	}

	

    
}
