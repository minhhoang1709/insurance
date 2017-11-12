package com.ninelives.insurance.api.model;

import java.util.Date;

public class Users {
    private String userId;

    private String password;

    private String email;

    private String googleRefreshToken;

    private String googleAuthCode;

    private String googleAccessToken;

    private String fcmToken;

    private String deviceId;

    private String name;

    private String gender;

    private Date birthDate;

    private String birthPlace;

    private String phone;

    private String address;

    private Long idCardFileId;

    private String status;

    private Date createdDate;

    private Date updateDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGoogleRefreshToken() {
        return googleRefreshToken;
    }

    public void setGoogleRefreshToken(String googleRefreshToken) {
        this.googleRefreshToken = googleRefreshToken;
    }

    public String getGoogleAuthCode() {
        return googleAuthCode;
    }

    public void setGoogleAuthCode(String googleAuthCode) {
        this.googleAuthCode = googleAuthCode;
    }

    public String getGoogleAccessToken() {
        return googleAccessToken;
    }

    public void setGoogleAccessToken(String googleAccessToken) {
        this.googleAccessToken = googleAccessToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getIdCardFileId() {
        return idCardFileId;
    }

    public void setIdCardFileId(Long idCardFileId) {
        this.idCardFileId = idCardFileId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}