package com.ninelives.insurance.api.model;

import java.time.LocalDateTime;

import com.ninelives.insurance.api.ref.BeneficiaryRelationship;

public class UserBeneficiary {
    private Long userBeneficiaryId;

    private String userId;

    private String name;

    private String phone;

    private String email;

    private BeneficiaryRelationship relationship;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    public Long getUserBeneficiaryId() {
        return userBeneficiaryId;
    }

    public void setUserBeneficiaryId(Long userBeneficiaryId) {
        this.userBeneficiaryId = userBeneficiaryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BeneficiaryRelationship getRelationship() {
		return relationship;
	}

	public void setRelationship(BeneficiaryRelationship relationship) {
		this.relationship = relationship;
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