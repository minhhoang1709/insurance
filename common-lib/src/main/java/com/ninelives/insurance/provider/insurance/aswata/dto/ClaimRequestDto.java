package com.ninelives.insurance.provider.insurance.aswata.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimRequestDto implements Serializable{
	private static final long serialVersionUID = 3587370850725408704L;
	
	@JsonProperty("service_code")
	private String serviceCode;
	@JsonProperty("user_ref_no")
	private String userRefNo;
	@JsonProperty("client_code")
	private String clientCode;
	@JsonProperty("request_time")
	private String requestTime;
	@JsonProperty("request_param")
	private RequestParam requestParam;
	@JsonProperty("auth_code")
	private String authCode;
	
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getUserRefNo() {
		return userRefNo;
	}
	public void setUserRefNo(String userRefNo) {
		this.userRefNo = userRefNo;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	public RequestParam getRequestParam() {
		return requestParam;
	}
	public void setRequestParam(RequestParam requestParam) {
		this.requestParam = requestParam;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class RequestParam implements Serializable{
		private static final long serialVersionUID = -2799257448017819428L;
		
		@JsonProperty("policy_number")
		private String policyNumber;
		@JsonProperty("reported_by")
		private String reportedBy;
		@JsonProperty("phone_no")
		private String phoneNo;
		@JsonProperty("date_of_loss")
		private String dateOfLoss;
		@JsonProperty("claim_reg_type")
		private String claimRegType;
		@JsonProperty("loss_description")
		private String lossDescription;
		@JsonProperty("cause_of_loss")
		private String causeOfLoss;
		@JsonProperty("loss_location")
		private String lossLocation;
		@JsonProperty("chronology")
		private String chronology;
		@JsonProperty("supporting_document")
		private List<SupportingDocument> supportingDocument = null;
		
		public String getPolicyNumber() {
			return policyNumber;
		}
		public void setPolicyNumber(String policyNumber) {
			this.policyNumber = policyNumber;
		}
		public String getReportedBy() {
			return reportedBy;
		}
		public void setReportedBy(String reportedBy) {
			this.reportedBy = reportedBy;
		}
		public String getPhoneNo() {
			return phoneNo;
		}
		public void setPhoneNo(String phoneNo) {
			this.phoneNo = phoneNo;
		}
		public String getDateOfLoss() {
			return dateOfLoss;
		}
		public void setDateOfLoss(String dateOfLoss) {
			this.dateOfLoss = dateOfLoss;
		}
		public String getClaimRegType() {
			return claimRegType;
		}
		public void setClaimRegType(String claimRegType) {
			this.claimRegType = claimRegType;
		}
		public String getLossDescription() {
			return lossDescription;
		}
		public void setLossDescription(String lossDescription) {
			this.lossDescription = lossDescription;
		}
		public String getCauseOfLoss() {
			return causeOfLoss;
		}
		public void setCauseOfLoss(String causeOfLoss) {
			this.causeOfLoss = causeOfLoss;
		}
		public String getLossLocation() {
			return lossLocation;
		}
		public void setLossLocation(String lossLocation) {
			this.lossLocation = lossLocation;
		}
		public String getChronology() {
			return chronology;
		}
		public void setChronology(String chronology) {
			this.chronology = chronology;
		}
		public List<SupportingDocument> getSupportingDocument() {
			return supportingDocument;
		}
		public void setSupportingDocument(List<SupportingDocument> supportingDocument) {
			this.supportingDocument = supportingDocument;
		}
	}
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class SupportingDocument implements Serializable{
		private static final long serialVersionUID = 1242646709166933009L;
		
		@JsonProperty("document_name")
		private String documentName;
		@JsonProperty("mime_type")
		private String mimeType;
		@JsonProperty("file_content")
		private String fileContent;
		public String getDocumentName() {
			return documentName;
		}
		public void setDocumentName(String documentName) {
			this.documentName = documentName;
		}
		public String getMimeType() {
			return mimeType;
		}
		public void setMimeType(String mimeType) {
			this.mimeType = mimeType;
		}
		public String getFileContent() {
			return fileContent;
		}
		public void setFileContent(String fileContent) {
			this.fileContent = fileContent;
		}
		
	}
}
