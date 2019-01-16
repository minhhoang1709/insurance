package com.ninelives.insurance.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ninelives.insurance.ref.OrderDocUsageType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDocTypeDto {
	private String orderDocTypeId;
	private String name;
	private OrderDocUsageType usageType;
	
	@JsonIgnore private Integer displayRank;

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

	public OrderDocUsageType getUsageType() {
		return usageType;
	}

	public void setUsageType(OrderDocUsageType usageType) {
		this.usageType = usageType;
	}

	
	public Integer getDisplayRank() {
		return displayRank;
	}

	public void setDisplayRank(Integer displayRank) {
		this.displayRank = displayRank;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((orderDocTypeId == null) ? 0 : orderDocTypeId.hashCode());
		result = prime * result + ((usageType == null) ? 0 : usageType.hashCode());
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
		OrderDocTypeDto other = (OrderDocTypeDto) obj;
		if (orderDocTypeId == null) {
			if (other.orderDocTypeId != null)
				return false;
		} else if (!orderDocTypeId.equals(other.orderDocTypeId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrderDocTypeDto [orderDocTypeId=" + orderDocTypeId + ", name=" + name + "]";
	}

}
