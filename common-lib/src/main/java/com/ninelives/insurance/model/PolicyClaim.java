package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.ninelives.insurance.ref.ClaimStatus;

public class PolicyClaim <T extends PolicyClaimDetail> implements Serializable{
	private static final long serialVersionUID = 3234000691514739484L;

	private String claimId;
    
    private String coverageCategoryId;

    private String orderId;

    private String userId;

    private LocalDate claimDate;

    private LocalDateTime incidentDateTime;

    private String incidentSummary;
    
    private Boolean isUserClaimant;

    private Boolean hasFamily;
    
    private Boolean isLumpSum;

    private ClaimStatus status;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;
    
    private T policyClaimDetail;
    
    private PolicyClaimBankAccount policyClaimBankAccount;
    
    private List<PolicyClaimDocument> policyClaimDocuments;
    
    private List<PolicyClaimCoverage> policyClaimCoverages;
    
    private List<PolicyClaimFamily> policyClaimFamilies;

    private PolicyOrder policyOrder;
    
    private CoverageCategory coverageCategory;
    
    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }
    
    public String getCoverageCategoryId() {
		return coverageCategoryId;
	}

	public void setCoverageCategoryId(String coverageCategoryId) {
		this.coverageCategoryId = coverageCategoryId;
	}

	public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDate claimDate) {
        this.claimDate = claimDate;
    }

    public LocalDateTime getIncidentDateTime() {
		return incidentDateTime;
	}

	public void setIncidentDateTime(LocalDateTime incidentDateTime) {
		this.incidentDateTime = incidentDateTime;
	}

	public String getIncidentSummary() {
        return incidentSummary;
    }

    public void setIncidentSummary(String incidentSummary) {
        this.incidentSummary = incidentSummary;
    }

    public Boolean getIsUserClaimant() {
		return isUserClaimant;
	}

	public void setIsUserClaimant(Boolean isUserClaimant) {
		this.isUserClaimant = isUserClaimant;
	}

	public Boolean getHasFamily() {
		return hasFamily;
	}

	public void setHasFamily(Boolean hasFamily) {
		this.hasFamily = hasFamily;
	}

	public Boolean getIsLumpSum() {
		return isLumpSum;
	}

	public void setIsLumpSum(Boolean isLumpSum) {
		this.isLumpSum = isLumpSum;
	}

	public List<PolicyClaimFamily> getPolicyClaimFamilies() {
		return policyClaimFamilies;
	}

	public void setPolicyClaimFamilies(List<PolicyClaimFamily> policyClaimFamilies) {
		this.policyClaimFamilies = policyClaimFamilies;
	}

	public T getPolicyClaimDetail() {
		return policyClaimDetail;
	}

	public void setPolicyClaimDetail(T policyClaimDetail) {
		this.policyClaimDetail = policyClaimDetail;
	}
	
	public PolicyClaimBankAccount getPolicyClaimBankAccount() {
		return policyClaimBankAccount;
	}

	public void setPolicyClaimBankAccount(PolicyClaimBankAccount policyClaimBankAccount) {
		this.policyClaimBankAccount = policyClaimBankAccount;
	}
	
	public List<PolicyClaimDocument> getPolicyClaimDocuments() {
		return policyClaimDocuments;
	}

	public void setPolicyClaimDocuments(List<PolicyClaimDocument> policyClaimDocuments) {
		this.policyClaimDocuments = policyClaimDocuments;
	}
	
	public List<PolicyClaimCoverage> getPolicyClaimCoverages() {
		return policyClaimCoverages;
	}

	public void setPolicyClaimCoverages(List<PolicyClaimCoverage> policyClaimCoverages) {
		this.policyClaimCoverages = policyClaimCoverages;
	}
	
	public PolicyOrder getPolicyOrder() {
		return policyOrder;
	}

	public void setPolicyOrder(PolicyOrder policyOrder) {
		this.policyOrder = policyOrder;
	}

	public CoverageCategory getCoverageCategory() {
		return coverageCategory;
	}

	public void setCoverageCategory(CoverageCategory coverageCategory) {
		this.coverageCategory = coverageCategory;
	}

	public ClaimStatus getStatus() {
		return status;
	}

	public void setStatus(ClaimStatus status) {
		this.status = status;
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