package com.ninelives.insurance.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDocumentDto {
	private UserFileDto file;
	private OrderDocTypeDto orderDocType;
	public UserFileDto getFile() {
		return file;
	}
	public void setFile(UserFileDto file) {
		this.file = file;
	}
	public OrderDocTypeDto getOrderDocType() {
		return orderDocType;
	}
	public void setOrderDocType(OrderDocTypeDto orderDocType) {
		this.orderDocType = orderDocType;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + ((orderDocType == null) ? 0 : orderDocType.hashCode());
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
		OrderDocumentDto other = (OrderDocumentDto) obj;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		if (orderDocType == null) {
			if (other.orderDocType != null)
				return false;
		} else if (!orderDocType.equals(other.orderDocType))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "OrderDocumentDto [file=" + file + ", orderDocType=" + orderDocType + "]";
	}
	
}
