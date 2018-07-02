package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import com.ninelives.insurance.ref.Gender;

public class PolicyOrderUsers implements Serializable{
	private static final long serialVersionUID = 4903160912260642857L;

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
    
    private String idCardNo;

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
    
    public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
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
		return "PolicyOrderUsers [policyOrderUsersId=" + policyOrderUsersId + ", orderId=" + orderId + ", email="
				+ email + ", name=" + name + ", gender=" + gender + ", birthDate=" + birthDate + ", birthPlace="
				+ birthPlace + ", phone=" + phone + ", address=" + address + ", idCardFileId=" + idCardFileId
				+ ", idCardNo=" + idCardNo + ", createdDate=" + createdDate + ", updateDate=" + updateDate + "]";
	}
    
    
}