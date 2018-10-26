package com.ninelives.insurance.api.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ninelives.insurance.ref.FamilyRelationship;
import com.ninelives.insurance.ref.Gender;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PolicyClaimFamilyDto {
    private Integer subId;

    @Size(max=255)
    private String name;

    private FamilyRelationship relationship;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime birthDate;
    
    private Gender gender;

	public Integer getSubId() {
		return subId;
	}

	public void setSubId(Integer subId) {
		this.subId = subId;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((birthDate == null) ? 0 : birthDate.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((relationship == null) ? 0 : relationship.hashCode());
		result = prime * result + ((subId == null) ? 0 : subId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PolicyClaimFamilyDto other = (PolicyClaimFamilyDto) obj;
		if (birthDate == null) {
			if (other.birthDate != null)
				return false;
		} else if (!birthDate.equals(other.birthDate))
			return false;
		if (gender != other.gender)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (relationship != other.relationship)
			return false;
		if (subId == null) {
			if (other.subId != null)
				return false;
		} else if (!subId.equals(other.subId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PolicyClaimFamilyDto [subId=" + subId + ", name=" + name + ", relationship=" + relationship
				+ ", birthDate=" + birthDate + ", gender=" + gender + "]";
	}
    
    
}
