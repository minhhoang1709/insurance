package com.ninelives.insurance.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ninelives.insurance.api.ref.PolicyStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto {
	private String orderId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate orderDate;
    
    private String title;
    
    private String imgUrl;

    private String policyNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate policyStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate policyEndDate;

    private Integer totalPremi;

    private Boolean hasBeneficiary;

    private Integer productCount;

    private PolicyStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;

    private List<ProductDto> productList;

    private PeriodDto period;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
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

	public PolicyStatus getStatus() {
		return status;
	}

	public void setStatus(PolicyStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public List<ProductDto> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductDto> productList) {
		this.productList = productList;
	}

	public PeriodDto getPeriod() {
		return period;
	}

	public void setPeriod(PeriodDto period) {
		this.period = period;
	}

    
}
