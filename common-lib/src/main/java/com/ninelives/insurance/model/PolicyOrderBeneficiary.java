package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.ninelives.insurance.ref.BeneficiaryRelationship;

public class PolicyOrderBeneficiary implements Serializable{
    private static final long serialVersionUID = -5461832608607460940L;

	private Long orderBeneficiaryId;

    private String orderId;

    private String name;

    private String phone;

    private String email;

    private BeneficiaryRelationship relationship;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    public Long getOrderBeneficiaryId() {
        return orderBeneficiaryId;
    }

    public void setOrderBeneficiaryId(Long orderBeneficiaryId) {
        this.orderBeneficiaryId = orderBeneficiaryId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

	@Override
	public String toString() {
		return "PolicyOrderBeneficiary ["
				+ (orderBeneficiaryId != null ? "orderBeneficiaryId=" + orderBeneficiaryId + ", " : "")
				+ (orderId != null ? "orderId=" + orderId + ", " : "") + (name != null ? "name=" + name + ", " : "")
				+ (phone != null ? "phone=" + phone + ", " : "") + (email != null ? "email=" + email + ", " : "")
				+ (relationship != null ? "relationship=" + relationship + ", " : "")
				+ (createdDate != null ? "createdDate=" + createdDate + ", " : "")
				+ (updateDate != null ? "updateDate=" + updateDate : "") + "]";
	}
	
	
    
}