package com.ninelives.insurance.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    
    private String subtitle;
    
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

    private List<ProductDto> products;

    private PeriodDto period;
    
    private UsersDto user;

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
	
	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
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

	public UsersDto getUser() {
		return user;
	}

	public void setUser(UsersDto user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "OrderDto [" + (orderId != null ? "orderId=" + orderId + ", " : "")
				+ (orderDate != null ? "orderDate=" + orderDate + ", " : "")
				+ (title != null ? "title=" + title + ", " : "")
				+ (subtitle != null ? "subtitle=" + subtitle + ", " : "")
				+ (imgUrl != null ? "imgUrl=" + imgUrl + ", " : "")
				+ (policyNumber != null ? "policyNumber=" + policyNumber + ", " : "")
				+ (policyStartDate != null ? "policyStartDate=" + policyStartDate + ", " : "")
				+ (policyEndDate != null ? "policyEndDate=" + policyEndDate + ", " : "")
				+ (totalPremi != null ? "totalPremi=" + totalPremi + ", " : "")
				+ (hasBeneficiary != null ? "hasBeneficiary=" + hasBeneficiary + ", " : "")
				+ (productCount != null ? "productCount=" + productCount + ", " : "")
				+ (status != null ? "status=" + status + ", " : "")
				+ (createdDate != null ? "createdDate=" + createdDate + ", " : "")
				+ (products != null ? "products=" + products + ", " : "")
				+ (period != null ? "period=" + period + ", " : "") + (user != null ? "user=" + user : "") + "]";
	}

    
}
