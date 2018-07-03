package com.ninelives.insurance.apigateway.dto;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ninelives.insurance.ref.BeneficiaryRelationship;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PolicyOrderBeneficiaryDto {
	private Long orderBeneficiaryId;

    private String orderId;

    @Size(max=255)
    private String name;

    @Size(max=50)
    private String phone;

    @Email
    private String email;

    private BeneficiaryRelationship relationship;

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

	@Override
	public String toString() {
		return "PolicyOrderBeneficiaryDto ["
				+ (orderBeneficiaryId != null ? "orderBeneficiaryId=" + orderBeneficiaryId + ", " : "")
				+ (orderId != null ? "orderId=" + orderId + ", " : "") + (name != null ? "name=" + name + ", " : "")
				+ (phone != null ? "phone=" + phone + ", " : "") + (email != null ? "email=" + email + ", " : "")
				+ (relationship != null ? "relationship=" + relationship : "") + "]";
	}

    
}
