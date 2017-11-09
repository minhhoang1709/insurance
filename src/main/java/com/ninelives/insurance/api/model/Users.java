package com.ninelives.insurance.api.model;

import java.util.Date;

public class Users {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.users.user_id
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private Long userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.users.password
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private String password;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.users.email
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private String email;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.users.google_refresh_token
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private String googleRefreshToken;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.users.google_auth_code
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private String googleAuthCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.users.google_access_token
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private String googleAccessToken;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.users.name
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.users.gender
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private String gender;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.users.birth_date
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private Date birthDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.users.birth_place
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private String birthPlace;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.users.phone
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private String phone;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.users.address
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private String address;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.users.id_card_file_id
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private Long idCardFileId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.users.created_date
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private Date createdDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.users.update_date
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private Date updateDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.users.device_id
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private String deviceId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.users.user_id
     *
     * @return the value of public.users.user_id
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.users.user_id
     *
     * @param userId the value for public.users.user_id
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.users.password
     *
     * @return the value of public.users.password
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.users.password
     *
     * @param password the value for public.users.password
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.users.email
     *
     * @return the value of public.users.email
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.users.email
     *
     * @param email the value for public.users.email
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.users.google_refresh_token
     *
     * @return the value of public.users.google_refresh_token
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public String getGoogleRefreshToken() {
        return googleRefreshToken;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.users.google_refresh_token
     *
     * @param googleRefreshToken the value for public.users.google_refresh_token
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setGoogleRefreshToken(String googleRefreshToken) {
        this.googleRefreshToken = googleRefreshToken;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.users.google_auth_code
     *
     * @return the value of public.users.google_auth_code
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public String getGoogleAuthCode() {
        return googleAuthCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.users.google_auth_code
     *
     * @param googleAuthCode the value for public.users.google_auth_code
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setGoogleAuthCode(String googleAuthCode) {
        this.googleAuthCode = googleAuthCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.users.google_access_token
     *
     * @return the value of public.users.google_access_token
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public String getGoogleAccessToken() {
        return googleAccessToken;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.users.google_access_token
     *
     * @param googleAccessToken the value for public.users.google_access_token
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setGoogleAccessToken(String googleAccessToken) {
        this.googleAccessToken = googleAccessToken;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.users.name
     *
     * @return the value of public.users.name
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.users.name
     *
     * @param name the value for public.users.name
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.users.gender
     *
     * @return the value of public.users.gender
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public String getGender() {
        return gender;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.users.gender
     *
     * @param gender the value for public.users.gender
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.users.birth_date
     *
     * @return the value of public.users.birth_date
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.users.birth_date
     *
     * @param birthDate the value for public.users.birth_date
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.users.birth_place
     *
     * @return the value of public.users.birth_place
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public String getBirthPlace() {
        return birthPlace;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.users.birth_place
     *
     * @param birthPlace the value for public.users.birth_place
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.users.phone
     *
     * @return the value of public.users.phone
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public String getPhone() {
        return phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.users.phone
     *
     * @param phone the value for public.users.phone
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.users.address
     *
     * @return the value of public.users.address
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.users.address
     *
     * @param address the value for public.users.address
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.users.id_card_file_id
     *
     * @return the value of public.users.id_card_file_id
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public Long getIdCardFileId() {
        return idCardFileId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.users.id_card_file_id
     *
     * @param idCardFileId the value for public.users.id_card_file_id
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setIdCardFileId(Long idCardFileId) {
        this.idCardFileId = idCardFileId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.users.created_date
     *
     * @return the value of public.users.created_date
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.users.created_date
     *
     * @param createdDate the value for public.users.created_date
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.users.update_date
     *
     * @return the value of public.users.update_date
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.users.update_date
     *
     * @param updateDate the value for public.users.update_date
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.users.device_id
     *
     * @return the value of public.users.device_id
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.users.device_id
     *
     * @param deviceId the value for public.users.device_id
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}