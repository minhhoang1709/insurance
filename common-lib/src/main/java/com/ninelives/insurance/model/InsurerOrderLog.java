package com.ninelives.insurance.model;

import java.time.LocalDateTime;

public class InsurerOrderLog {
    private Long id;

    private String coverageCategoryId;

    private String serviceCode;

    private String orderId;

    private LocalDateTime orderTime;

    private String userRefNo;

    private String providerCoverage;

    private Integer premi;

    private Integer responseStatus;

    private String responseCode;

    private String providerRequestTime;

    private String providerResponseTime;

    private LocalDateTime requestTime;

    private LocalDateTime responseTime;

    private String policyNumber;

    private String orderNumber;

    private String downloadUrl;
    
    private String packageType;

    private String productCode;

    private Integer travelType;

    private String isFamily;

    private String otherProperties;

    private LocalDateTime createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoverageCategoryId() {
        return coverageCategoryId;
    }

    public void setCoverageCategoryId(String coverageCategoryId) {
        this.coverageCategoryId = coverageCategoryId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public String getUserRefNo() {
        return userRefNo;
    }

    public void setUserRefNo(String userRefNo) {
        this.userRefNo = userRefNo;
    }

    public String getProviderCoverage() {
        return providerCoverage;
    }

    public void setProviderCoverage(String providerCoverage) {
        this.providerCoverage = providerCoverage;
    }

    public Integer getPremi() {
        return premi;
    }

    public void setPremi(Integer premi) {
        this.premi = premi;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getProviderRequestTime() {
        return providerRequestTime;
    }

    public void setProviderRequestTime(String providerRequestTime) {
        this.providerRequestTime = providerRequestTime;
    }

    public String getProviderResponseTime() {
        return providerResponseTime;
    }

    public void setProviderResponseTime(String providerResponseTime) {
        this.providerResponseTime = providerResponseTime;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public LocalDateTime getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(LocalDateTime responseTime) {
        this.responseTime = responseTime;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getPackageType() {
		return packageType;
	}

	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
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

	public String getOtherProperties() {
		return otherProperties;
	}

	public void setOtherProperties(String otherProperties) {
		this.otherProperties = otherProperties;
	}

	public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}