package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.ninelives.insurance.ref.OrderDocUsageType;

public class OrderDocType implements Serializable{
	private static final long serialVersionUID = 8574425368401496688L;

	private String orderDocTypeId;

    private String name;

    private String description;
    
    private OrderDocUsageType usageType;
    
    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

	public String getOrderDocTypeId() {
		return orderDocTypeId;
	}

	public void setOrderDocTypeId(String orderDocTypeId) {
		this.orderDocTypeId = orderDocTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public OrderDocUsageType getUsageType() {
		return usageType;
	}

	public void setUsageType(OrderDocUsageType usageType) {
		this.usageType = usageType;
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

	@Override
	public String toString() {
		return "OrderDocType [orderDocTypeId=" + orderDocTypeId + ", name=" + name + ", usageType=" + usageType + "]";
	}

}
