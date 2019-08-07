package com.ninelives.insurance.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimBankAccount {
	
	private String accountBankName;
	
	private String accountBankSwiftCode;
	
	private String accountName;
	
	private String accountNumber;

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

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Override
	public String toString() {
		return "ClaimBankAccount [accountBankName=" + accountBankName + ", accountBankSwiftCode=" + accountBankSwiftCode
				+ ", accountName=" + accountName + ", accountNumber=" + accountNumber + "]";
	}

   
	
	
}
