package com.ninelives.insurance.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ninelives.insurance.ref.VoucherType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoucherDto {
	private String code;
	
    private String title;
    
    private String subtitle;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime policyStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime policyEndDate;

    private Integer basePremi;
    
    private Integer totalPremi;

    private Boolean hasBeneficiary;

    private Integer productCount;
    
    private List<ProductDto> products;
    
    private PeriodDto period;
    
    private String description;
    
    private VoucherType voucherType;

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

	public LocalDateTime getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(LocalDateTime policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public LocalDateTime getPolicyEndDate() {
		return policyEndDate;
	}

	public void setPolicyEndDate(LocalDateTime policyEndDate) {
		this.policyEndDate = policyEndDate;
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

	public List<ProductDto> getProducts() {
		return products;
	}

	public void setProducts(List<ProductDto> products) {
		this.products = products;
	}

	public PeriodDto getPeriod() {
		return period;
	}

	public void setPeriod(PeriodDto period) {
		this.period = period;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public VoucherType getVoucherType() {
		return voucherType;
	}

	public void setVoucherType(VoucherType voucherType) {
		this.voucherType = voucherType;
	}
}
