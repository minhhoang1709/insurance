package com.ninelives.insurance.model;

import java.io.Serializable;
import java.util.Date;



public class UserCms implements Serializable{
	
	private static final long serialVersionUID = 134746234530187692L;
	
	private Long id;
	
	private String userId;

    private String userName;
    
    private String roleId;
    
    private String password;

    private String email;
    
    private String isActive;
    
    private Date createdDate;
    
    private String createdBy;
    
    private Date modifiedDate;
    
    private String modifiedBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
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

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Override
	public String toString() {
		return "UserCms [id=" + id + ", userId=" + userId + ", userName=" + userName + ", roleId=" + roleId
				+ ", password=" + password + ", email=" + email + ", isActive=" + isActive + ", createdDate="
				+ createdDate + ", createdBy=" + createdBy + ", modifiedDate=" + modifiedDate + ", modifiedBy="
				+ modifiedBy + "]";
	}
    
     	

}	
