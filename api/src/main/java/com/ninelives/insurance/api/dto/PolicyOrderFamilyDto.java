package com.ninelives.insurance.api.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ninelives.insurance.ref.FamilyRelationship;
import com.ninelives.insurance.ref.Gender;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PolicyOrderFamilyDto {
    private String orderId;

    @Size(max=255)
    private String name;

    private FamilyRelationship relationship;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime birthDate;
    
    private Gender gender;

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

	public FamilyRelationship getRelationship() {
		return relationship;
	}

	public void setRelationship(FamilyRelationship relationship) {
		this.relationship = relationship;
	}

	public LocalDateTime getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDateTime birthDate) {
		this.birthDate = birthDate;
	}
	
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "PolicyOrderFamilyDto [orderId=" + orderId + ", name=" + name + ", relationship=" + relationship
				+ ", birthDate=" + birthDate + "]";
	}
    
    
}
