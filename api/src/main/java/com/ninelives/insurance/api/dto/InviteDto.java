package com.ninelives.insurance.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InviteDto {
	private Boolean hasInvite; //true if invitation is available
	private VoucherDto inviterVoucher;

	public Boolean getHasInvite() {
		return hasInvite;
	}
	public void setHasInvite(Boolean hasInvite) {
		this.hasInvite = hasInvite;
	}
	public VoucherDto getInviterVoucher() {
		return inviterVoucher;
	}
	public void setInviterVoucher(VoucherDto inviterVoucher) {
		this.inviterVoucher = inviterVoucher;
	}
	
}
