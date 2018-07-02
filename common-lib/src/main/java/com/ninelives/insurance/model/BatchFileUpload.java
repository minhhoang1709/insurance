package com.ninelives.insurance.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class BatchFileUpload implements Serializable{
	private static final long serialVersionUID = -1479117497582010023L;

	private Long fileId;

    private String batchNumber;
    
    //private String fileName;
    
    private String email;
    
    private String nama;
    
    private String jenisKelamin;
    
    private String tanggalLahir;
    
    private String tempatLahir;
    
    private String noTelpon;
    
    private String ktpNumber;
    
    private String validationStatus;
    
    private String responseMessage;
    
    private Timestamp createdDate;

    private String createdBy;
    
    private Timestamp modifiedDate;
    
    private String errorCode;
    
    
    public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	private String isValidate;

    public String getIsValidate() {
		return isValidate;
	}

	public void setIsValidate(String isValidate) {
		this.isValidate = isValidate;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	private String modifiedBy;

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	/*public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}*/

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getJenisKelamin() {
		return jenisKelamin;
	}

	public void setJenisKelamin(String jenisKelamin) {
		this.jenisKelamin = jenisKelamin;
	}

	public String getTanggalLahir() {
		return tanggalLahir;
	}

	public void setTanggalLahir(String tanggalLahir) {
		this.tanggalLahir = tanggalLahir;
	}

	public String getTempatLahir() {
		return tempatLahir;
	}

	public void setTempatLahir(String tempatLahir) {
		this.tempatLahir = tempatLahir;
	}

	public String getNoTelpon() {
		return noTelpon;
	}

	public void setNoTelpon(String noTelpon) {
		this.noTelpon = noTelpon;
	}

	public String getKtpNumber() {
		return ktpNumber;
	}

	public void setKtpNumber(String ktpNumber) {
		this.ktpNumber = ktpNumber;
	}

	

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getValidationStatus() {
		return validationStatus;
	}

	public void setValidationStatus(String validationStatus) {
		this.validationStatus = validationStatus;
	}

	@Override
	public String toString() {
		return "BatchFileUpload [fileId=" + fileId + ", batchNumber=" + batchNumber + ", email=" + email + ", nama="
				+ nama + ", jenisKelamin=" + jenisKelamin + ", tanggalLahir=" + tanggalLahir + ", tempatLahir="
				+ tempatLahir + ", noTelpon=" + noTelpon + ", ktpNumber=" + ktpNumber + ", validationStatus="
				+ validationStatus + ", responseMessage=" + responseMessage + ", createdDate=" + createdDate
				+ ", createdBy=" + createdBy + ", modifiedDate=" + modifiedDate + ", errorCode=" + errorCode
				+ ", isValidate=" + isValidate + ", modifiedBy=" + modifiedBy + "]";
	}

	

   
}