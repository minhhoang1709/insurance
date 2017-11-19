package com.ninelives.insurance.api.dto;

import java.util.Arrays;

public class OrderFilterDto {
	int offset;
	int limit;
	String[] status;
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String[] getStatus() {
		return status;
	}
	public void setStatus(String[] status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "OrderFilterDto [offset=" + offset + ", limit=" + limit + ", status=" + Arrays.toString(status) + "]";
	}
	
}
