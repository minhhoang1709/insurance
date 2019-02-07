package com.ninelives.insurance.provider.insurance.aswata.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ninelives.insurance.model.UserFile;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequestDto implements Serializable{
	private static final long serialVersionUID = -7747133873124996290L;
	
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
	
	@Override
	public String toString() {
		return "OrderRequestDto [serviceCode=" + serviceCode + ", userRefNo=" + userRefNo + ", clientCode=" + clientCode
				+ ", requestTime=" + requestTime + ", requestParam=" + requestParam + ", authCode=" + authCode + "]";
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class RequestParam implements Serializable{
		private static final long serialVersionUID = 8448888873315841176L;
		
		@JsonProperty("product_code")
		private String productCode;
		@JsonProperty("package_type")
		private String packageType;
		@JsonProperty("insured_name")
		private String insuredName;
		@JsonProperty("date_of_birth")
		private String dateOfBirth;
		@JsonProperty("gender")
		private String gender;
		@JsonProperty("insured_address")
		private String insuredAddress;
		@JsonProperty("insurance_start_date")
		private String insuranceStartDate;
		@JsonProperty("insurance_end_date")
		private String insuranceEndDate;
		@JsonProperty("coverages")
		private String coverages;
		@JsonProperty("premium")
		private Integer premium;
		@JsonProperty("id_card")
		private String idCard;
		@JsonProperty("id_card_number")
		private String idCardNumber;
		@JsonProperty("beneficiary")
		private String beneficiary;
		@JsonProperty("beneficiary_relation")
		private String beneficiaryRelation;
		@JsonProperty("mobile_number")
		private String mobileNumber;
		@JsonProperty("email_address")
		private String emailAddress;
		@JsonProperty("industry")
		private String industry;
		@JsonProperty("client_id")
		private String clientId;
		
		/*
		 * Selfie insurance support
		 */
		@JsonProperty("face_photo_front")
		private UserFile facePhotoFront;
		@JsonProperty("face_photo_left")
		private UserFile facePhotoLeft;
		@JsonProperty("face_photo_right")
		private UserFile facePhotoRight;
				
		/*
		 * Travel insurance support
		 */
		@JsonProperty("travel_type")
		private Integer travelType;
		@JsonProperty("is_family")
		private String isFamily;
		@JsonProperty("family_list")
		private List<RequestParamFamily> familyList;
		
		public String getProductCode() {
			return productCode;
		}
		public void setProductCode(String productCode) {
			this.productCode = productCode;
		}
		public String getClientId() {
			return clientId;
		}
		public void setClientId(String clientId) {
			this.clientId = clientId;
		}
		public String getPackageType() {
			return packageType;
		}
		public void setPackageType(String packageType) {
			this.packageType = packageType;
		}
		public String getInsuredName() {
			return insuredName;
		}
		public void setInsuredName(String insuredName) {
			this.insuredName = insuredName;
		}
		public String getDateOfBirth() {
			return dateOfBirth;
		}
		public void setDateOfBirth(String dateOfBirth) {
			this.dateOfBirth = dateOfBirth;
		}
		public String getGender() {
			return gender;
		}
		public void setGender(String gender) {
			this.gender = gender;
		}
		public String getInsuredAddress() {
			return insuredAddress;
		}
		public void setInsuredAddress(String insuredAddress) {
			this.insuredAddress = insuredAddress;
		}
		public String getInsuranceStartDate() {
			return insuranceStartDate;
		}
		public void setInsuranceStartDate(String insuranceStartDate) {
			this.insuranceStartDate = insuranceStartDate;
		}		
		public String getInsuranceEndDate() {
			return insuranceEndDate;
		}
		public void setInsuranceEndDate(String insuranceEndDate) {
			this.insuranceEndDate = insuranceEndDate;
		}
		public String getCoverages() {
			return coverages;
		}
		public void setCoverages(String coverages) {
			this.coverages = coverages;
		}		
		public Integer getPremium() {
			return premium;
		}
		public void setPremium(Integer premium) {
			this.premium = premium;
		}
		public String getIdCard() {
			return idCard;
		}
		public void setIdCard(String idCard) {
			this.idCard = idCard;
		}		
		public String getIdCardNumber() {
			return idCardNumber;
		}
		public void setIdCardNumber(String idCardNumber) {
			this.idCardNumber = idCardNumber;
		}
		public String getBeneficiary() {
			return beneficiary;
		}
		public void setBeneficiary(String beneficiary) {
			this.beneficiary = beneficiary;
		}
		public String getBeneficiaryRelation() {
			return beneficiaryRelation;
		}
		public void setBeneficiaryRelation(String beneficiaryRelation) {
			this.beneficiaryRelation = beneficiaryRelation;
		}
		public String getMobileNumber() {
			return mobileNumber;
		}
		public void setMobileNumber(String mobileNumber) {
			this.mobileNumber = mobileNumber;
		}
		public String getEmailAddress() {
			return emailAddress;
		}
		public void setEmailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
		}
		public String getIndustry() {
			return industry;
		}
		public void setIndustry(String industry) {
			this.industry = industry;
		}
		public Integer getTravelType() {
			return travelType;
		}
		public void setTravelType(Integer travelType) {
			this.travelType = travelType;
		}
		public String getIsFamily() {
			return isFamily;
		}
		public void setIsFamily(String isFamily) {
			this.isFamily = isFamily;
		}
		public List<RequestParamFamily> getFamilyList() {
			return familyList;
		}
		public void setFamilyList(List<RequestParamFamily> familyList) {
			this.familyList = familyList;
		}		
		public UserFile getFacePhotoFront() {
			return facePhotoFront;
		}
		public void setFacePhotoFront(UserFile facePhotoFront) {
			this.facePhotoFront = facePhotoFront;
		}
		public UserFile getFacePhotoLeft() {
			return facePhotoLeft;
		}
		public void setFacePhotoLeft(UserFile facePhotoLeft) {
			this.facePhotoLeft = facePhotoLeft;
		}
		public UserFile getFacePhotoRight() {
			return facePhotoRight;
		}
		public void setFacePhotoRight(UserFile facePhotoRight) {
			this.facePhotoRight = facePhotoRight;
		}
		@Override
		public String toString() {
			return "RequestParam [productCode=" + productCode + ", packageType=" + packageType + ", insuredName="
					+ insuredName + ", dateOfBirth=" + dateOfBirth + ", gender=" + gender + ", insuredAddress="
					+ insuredAddress + ", insuranceStartDate=" + insuranceStartDate + ", insuranceEndDate="
					+ insuranceEndDate + ", coverages=" + coverages + ", premium=" + premium + ", idCard=" + (idCard == null ? "" : "***")
					+ ", idCardNumber=" + idCardNumber + ", beneficiary=" + beneficiary + ", beneficiaryRelation="
					+ beneficiaryRelation + ", mobileNumber=" + mobileNumber + ", emailAddress=" + emailAddress
					+ ", industry=" + industry + ", clientId=" + clientId + ", facePhotoFront=" + facePhotoFront
					+ ", facePhotoLeft=" + facePhotoLeft + ", facePhotoRight=" + facePhotoRight + ", travelType="
					+ travelType + ", isFamily=" + isFamily + ", familyList=" + familyList + "]";
		}

//		@Override
//		public String toString() {
//			return "RequestParam [productCode=" + productCode + ", packageType=" + packageType + ", insuredName="
//					+ insuredName + ", dateOfBirth=" + dateOfBirth + ", gender=" + gender + ", insuredAddress="
//					+ insuredAddress + ", insuranceStartDate=" + insuranceStartDate + ", insuranceEndDate="
//					+ insuranceEndDate + ", coverages=" + coverages + ", premium=" + premium + ", idCardNumber="
//					+ idCardNumber + ", beneficiary=" + beneficiary + ", beneficiaryRelation=" + beneficiaryRelation
//					+ ", mobileNumber=" + mobileNumber + ", emailAddress=" + emailAddress + ", industry=" + industry
//					+ ", clientId=" + clientId + ", travelType=" + travelType + ", isFamily=" + isFamily
//					+ ", familyList=" + familyList + ", idCard=" + (idCard == null ? "" : "***") + ", facePhotoFront="
//					+ (facePhotoFront == null ? "" : "***") + ", facePhotoLeft=" + (facePhotoLeft == null ? "" : "***")
//					+ ", facePhotoRight=" + (facePhotoRight == null ? "" : "***") + "]";
//		}
		
	}
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class RequestParamFamily implements Serializable{
		private static final long serialVersionUID = 7044761969219806251L;
		
		@JsonProperty("family_name")
		private String familyName;
		@JsonProperty("date_of_birth")
		private String dateOfBirth;
		@JsonProperty("relation")
		private String relation;
		@JsonProperty("beneficiary")
		private String beneficiary;
		@JsonProperty("id_card_number")
		private String idCardNumber;
		
		public String getFamilyName() {
			return familyName;
		}
		public void setFamilyName(String familyName) {
			this.familyName = familyName;
		}
		public String getDateOfBirth() {
			return dateOfBirth;
		}
		public void setDateOfBirth(String dateOfBirth) {
			this.dateOfBirth = dateOfBirth;
		}
		public String getRelation() {
			return relation;
		}
		public void setRelation(String relation) {
			this.relation = relation;
		}
		public String getBeneficiary() {
			return beneficiary;
		}
		public void setBeneficiary(String beneficiary) {
			this.beneficiary = beneficiary;
		}
		public String getIdCardNumber() {
			return idCardNumber;
		}
		public void setIdCardNumber(String idCardNumber) {
			this.idCardNumber = idCardNumber;
		}
		@Override
		public String toString() {
			return "RequestParamFamily [familyName=" + familyName + ", dateOfBirth=" + dateOfBirth + ", relation="
					+ relation + ", beneficiary=" + beneficiary + ", idCardNumber=" + idCardNumber + "]";
		}
		
		
	}
}
