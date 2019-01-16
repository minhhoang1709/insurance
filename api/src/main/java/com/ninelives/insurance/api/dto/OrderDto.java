package com.ninelives.insurance.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ninelives.insurance.ref.PolicyStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto {
	private String orderId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime orderDate;
    
    private String title;
    
    private String subtitle;
    
    private String imgUrl;

    private String policyNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime policyStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime policyEndDate;

    private Integer totalPremi;

    private Boolean hasBeneficiary;

    private Integer productCount;

    private PolicyStatus status;
    
    private Boolean isFamily;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;

    private List<ProductDto> products;

    private CoverageCategoryDto coverageCategory;
    
    private PeriodDto period;
    
    @Valid
    private UserDto user;
    
    private VoucherDto voucher;
    
    private PaymentDto payment;
    
    private List<PolicyOrderFamilyDto> families;
    
    private List<OrderDocumentDto> orderDocuments;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
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
	
	public Boolean getIsFamily() {
		return isFamily;
	}

	public void setIsFamily(Boolean isFamily) {
		this.isFamily = isFamily;
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

	public CoverageCategoryDto getCoverageCategory() {
		return coverageCategory;
	}

	public void setCoverageCategory(CoverageCategoryDto coverageCategory) {
		this.coverageCategory = coverageCategory;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}
	
	public VoucherDto getVoucher() {
		return voucher;
	}

	public void setVoucher(VoucherDto voucher) {
		this.voucher = voucher;
	}

	public PaymentDto getPayment() {
		return payment;
	}

	public void setPayment(PaymentDto payment) {
		this.payment = payment;
	}
	
	public List<PolicyOrderFamilyDto> getFamilies() {
		return families;
	}

	public void setFamilies(List<PolicyOrderFamilyDto> families) {
		this.families = families;
	}

	public List<OrderDocumentDto> getOrderDocuments() {
		return orderDocuments;
	}

	public void setOrderDocuments(List<OrderDocumentDto> orderDocuments) {
		this.orderDocuments = orderDocuments;
	}

	@Override
	public String toString() {
		return "OrderDto [orderId=" + orderId + ", orderDate=" + orderDate + ", title=" + title + ", subtitle="
				+ subtitle + ", imgUrl=" + imgUrl + ", policyNumber=" + policyNumber + ", policyStartDate="
				+ policyStartDate + ", policyEndDate=" + policyEndDate + ", totalPremi=" + totalPremi
				+ ", hasBeneficiary=" + hasBeneficiary + ", productCount=" + productCount + ", status=" + status
				+ ", isFamily=" + isFamily + ", createdDate=" + createdDate + ", products=" + products
				+ ", coverageCategory=" + coverageCategory + ", period=" + period + ", user=" + user + ", voucher="
				+ voucher + ", payment=" + payment + ", families=" + families + ", orderDocuments=" + orderDocuments
				+ "]";
	}

    
}
