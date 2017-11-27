package com.ninelives.insurance.api.model;

import java.time.LocalDateTime;

public class PolicyOrderClaim {
    private String claimId;

    private String orderId;

    private LocalDateTime claimDateTime;

    private LocalDateTime accidentDateTime;

    private String accidentSummary;

    private String accidentAddressCountry;

    private String accidentAddressProvince;

    private String accidentAddressCity;

    private String accidentAddress;

    private String claimAccountName;

    private String claimAccountBankName;

    private String claimAccountBankSwiftCode;

    private String claimAccountBankSwift;

    private String claimAccountNumber;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    private String status;

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getClaimDateTime() {
        return claimDateTime;
    }

    public void setClaimDateTime(LocalDateTime claimDateTime) {
        this.claimDateTime = claimDateTime;
    }

    public LocalDateTime getAccidentDateTime() {
        return accidentDateTime;
    }

    public void setAccidentDateTime(LocalDateTime accidentDateTime) {
        this.accidentDateTime = accidentDateTime;
    }

    public String getAccidentSummary() {
        return accidentSummary;
    }

    public void setAccidentSummary(String accidentSummary) {
        this.accidentSummary = accidentSummary;
    }

    public String getAccidentAddressCountry() {
        return accidentAddressCountry;
    }

    public void setAccidentAddressCountry(String accidentAddressCountry) {
        this.accidentAddressCountry = accidentAddressCountry;
    }

    public String getAccidentAddressProvince() {
        return accidentAddressProvince;
    }

    public void setAccidentAddressProvince(String accidentAddressProvince) {
        this.accidentAddressProvince = accidentAddressProvince;
    }

    public String getAccidentAddressCity() {
        return accidentAddressCity;
    }

    public void setAccidentAddressCity(String accidentAddressCity) {
        this.accidentAddressCity = accidentAddressCity;
    }

    public String getAccidentAddress() {
        return accidentAddress;
    }

    public void setAccidentAddress(String accidentAddress) {
        this.accidentAddress = accidentAddress;
    }

    public String getClaimAccountName() {
        return claimAccountName;
    }

    public void setClaimAccountName(String claimAccountName) {
        this.claimAccountName = claimAccountName;
    }

    public String getClaimAccountBankName() {
        return claimAccountBankName;
    }

    public void setClaimAccountBankName(String claimAccountBankName) {
        this.claimAccountBankName = claimAccountBankName;
    }

    public String getClaimAccountBankSwiftCode() {
        return claimAccountBankSwiftCode;
    }

    public void setClaimAccountBankSwiftCode(String claimAccountBankSwiftCode) {
        this.claimAccountBankSwiftCode = claimAccountBankSwiftCode;
    }

    public String getClaimAccountBankSwift() {
        return claimAccountBankSwift;
    }

    public void setClaimAccountBankSwift(String claimAccountBankSwift) {
        this.claimAccountBankSwift = claimAccountBankSwift;
    }

    public String getClaimAccountNumber() {
        return claimAccountNumber;
    }

    public void setClaimAccountNumber(String claimAccountNumber) {
        this.claimAccountNumber = claimAccountNumber;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}