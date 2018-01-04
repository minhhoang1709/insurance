package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.ninelives.insurance.ref.VoucherType;

public class Voucher implements Serializable{
	private static final long serialVersionUID = -5288164944287308416L;

	private Integer id;

    private String code;

    private String title;

    private String subtitle;

    private String description;

    private LocalDate policyStartDate;

    private LocalDate policyEndDate;

    private LocalDate useStartDate;

    private LocalDate useEndDate;

    private Integer basePremi;

    private Integer totalPremi;

    private Boolean hasBeneficiary;

    private Integer productCount;

    private String periodId;

    private String status;

    private VoucherType voucherType;
    
    List<Product> products;
    
    Period period;
    
    private Integer inviterRewardLimit;
    
    private String inviterUserId;
    
    //private User inviter;    
    
    private Integer inviterRewardCount;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(LocalDate policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public LocalDate getPolicyEndDate() {
		return policyEndDate;
	}

	public void setPolicyEndDate(LocalDate policyEndDate) {
		this.policyEndDate = policyEndDate;
	}

	public LocalDate getUseStartDate() {
		return useStartDate;
	}

	public void setUseStartDate(LocalDate useStartDate) {
		this.useStartDate = useStartDate;
	}

	public LocalDate getUseEndDate() {
		return useEndDate;
	}

	public void setUseEndDate(LocalDate useEndDate) {
		this.useEndDate = useEndDate;
	}

	public Integer getBasePremi() {
        return basePremi;
    }

    public void setBasePremi(Integer basePremi) {
        this.basePremi = basePremi;
    }

    public Integer getTotalPremi() {
        return totalPremi;
    }

    public void setTotalPremi(Integer totalPremi) {
        this.totalPremi = totalPremi;
    }

    public Boolean getHasBeneficiary() {
        return hasBeneficiary;
    }

    public void setHasBeneficiary(Boolean hasBeneficiary) {
        this.hasBeneficiary = hasBeneficiary;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public VoucherType getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(VoucherType voucherType) {
        this.voucherType = voucherType;
    }
    
    public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}
	
	public String getInviterUserId() {
		return inviterUserId;
	}

	public void setInviterUserId(String inviterUserId) {
		this.inviterUserId = inviterUserId;
	}

	public Integer getInviterRewardLimit() {
		return inviterRewardLimit;
	}

	public void setInviterRewardLimit(Integer inviterRewardLimit) {
		this.inviterRewardLimit = inviterRewardLimit;
	}

	public Integer getInviterRewardCount() {
		return inviterRewardCount;
	}

	public void setInviterRewardCount(Integer inviterRewardCount) {
		this.inviterRewardCount = inviterRewardCount;
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