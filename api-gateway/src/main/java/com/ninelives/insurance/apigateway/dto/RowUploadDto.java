package com.ninelives.insurance.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class RowUploadDto {
    
	//private String fileName;
	
	private String email;
	
	private String nama;
	
	private String jenisKelamin;
	
	private String tanggalLahir;
	
	private String tempatLahir;
	
	private String noTelpon;
	
	private String ktpNumber;
	
	private String validationStatus;
	
	private String errorCode;
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	private String responseMessage;

	/*public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
*/
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

	public String getValidationStatus() {
		return validationStatus;
	}

	public void setValidationStatus(String validationStatus) {
		this.validationStatus = validationStatus;
	}

	@Override
	public String toString() {
		return "RowUploadDto [email=" + email + ", nama=" + nama + ", jenisKelamin=" + jenisKelamin + ", tanggalLahir="
				+ tanggalLahir + ", tempatLahir=" + tempatLahir + ", noTelpon=" + noTelpon + ", ktpNumber=" + ktpNumber
				+ ", validationStatus=" + validationStatus + ", errorCode=" + errorCode + ", responseMessage="
				+ responseMessage + "]";
	}

	
	
	
}
