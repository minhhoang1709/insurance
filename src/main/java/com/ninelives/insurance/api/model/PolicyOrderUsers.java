package com.ninelives.insurance.api.model;

import java.time.LocalDate;
import java.util.Date;

import com.ninelives.insurance.api.ref.Gender;

public class PolicyOrderUsers {
    private Long policyOrderUsersId;

    private String orderId;

    private String email;

    private String name;

    private Gender gender;

    private LocalDate birthDate;

    private String birthPlace;

    private String phone;

    private String address;

    private Long idCardFileId;

    private Date createdDate;

    private Date updateDate;

    public Long getPolicyOrderUsersId() {
        return policyOrderUsersId;
    }

    public void setPolicyOrderUsersId(Long policyOrderUsersId) {
        this.policyOrderUsersId = policyOrderUsersId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
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

	@Override
	public String toString() {
		return "PolicyOrderUsers ["
				+ (policyOrderUsersId != null ? "policyOrderUsersId=" + policyOrderUsersId + ", " : "")
				+ (orderId != null ? "orderId=" + orderId + ", " : "") + (email != null ? "email=" + email + ", " : "")
				+ (name != null ? "name=" + name + ", " : "") + (gender != null ? "gender=" + gender + ", " : "")
				+ (birthDate != null ? "birthDate=" + birthDate + ", " : "")
				+ (birthPlace != null ? "birthPlace=" + birthPlace + ", " : "")
				+ (phone != null ? "phone=" + phone + ", " : "") + (address != null ? "address=" + address + ", " : "")
				+ (idCardFileId != null ? "idCardFileId=" + idCardFileId + ", " : "")
				+ (createdDate != null ? "createdDate=" + createdDate + ", " : "")
				+ (updateDate != null ? "updateDate=" + updateDate : "") + "]";
	}
    
    
}