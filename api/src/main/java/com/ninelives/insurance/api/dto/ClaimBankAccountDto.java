package com.ninelives.insurance.api.dto;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimBankAccountDto {
	@Size(max=255)
	private String name;
	@Size(max=255)
	private String account;
	@Size(max=255)
	private String bankName;
	//private String bankSwitt;
	@Size(max=255)
	private String bankSwiftCode;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
//	public String getBankSwitt() {
//		return bankSwitt;
//	}
//	public void setBankSwitt(String bankSwitt) {
//		this.bankSwitt = bankSwitt;
//	}
	public String getBankSwiftCode() {
		return bankSwiftCode;
	}
	public void setBankSwiftCode(String bankSwiftCode) {
		this.bankSwiftCode = bankSwiftCode;
	}
	@Override
	public String toString() {
		return "ClaimBankAccountDto [name=" + name + ", account=" + account + ", bankName=" + bankName
				+ ", bankSwiftCode=" + bankSwiftCode + "]";
	}
	
}
