package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ninelives.insurance.ref.FamilyRelationship;
import com.ninelives.insurance.ref.Gender;

public class PolicyOrderFamily implements Serializable{
    private static final long serialVersionUID = -2093472748933402613L;

	private Long id;

    private Integer subId;
    
    private String orderId;

    private String name;

    private FamilyRelationship relationship;
    
    private LocalDate birthDate;

    private Gender gender;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSubId() {
		return subId;
	}

	public void setSubId(Integer subId) {
		this.subId = subId;
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

    public FamilyRelationship getRelationship() {
		return relationship;
	}

	public void setRelationship(FamilyRelationship relationship) {
		this.relationship = relationship;
	}

	public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    
    public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
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