package com.ninelives.insurance.model;

import java.time.LocalDateTime;

public class PolicyClaimBankAccount {
    private String claimId;

    private String accountName;

    private String accountBankName;

    private String accountBankSwiftCode;

    private String accountBankSwift;

    private String accountNumber;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountBankName() {
        return accountBankName;
    }

    public void setAccountBankName(String accountBankName) {
        this.accountBankName = accountBankName;
    }

    public String getAccountBankSwiftCode() {
        return accountBankSwiftCode;
    }

    public void setAccountBankSwiftCode(String accountBankSwiftCode) {
        this.accountBankSwiftCode = accountBankSwiftCode;
    }

    public String getAccountBankSwift() {
        return accountBankSwift;
    }

    public void setAccountBankSwift(String accountBankSwift) {
        this.accountBankSwift = accountBankSwift;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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
}