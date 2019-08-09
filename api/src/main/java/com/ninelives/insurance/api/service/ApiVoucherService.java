package com.ninelives.insurance.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.api.adapter.ModelMapperAdapter;
import com.ninelives.insurance.api.dto.VoucherDto;
import com.ninelives.insurance.core.exception.AppNotFoundException;
import com.ninelives.insurance.core.service.VoucherService;

@Service
public class ApiVoucherService {
	
	@Autowired ModelMapperAdapter modelMapperAdapter;
	@Autowired VoucherService voucherService;

	public VoucherDto fetchVoucherDtoByCode(String code, String authUserId) throws AppNotFoundException{
		return modelMapperAdapter.toDto(voucherService.fetchVoucherByCode(code, authUserId));
	}
	
	//@Cacheable("InviteVoucherDto")
	public VoucherDto fetchVoucherDtoForInviteById(int voucherId){
		return modelMapperAdapter.toDto(voucherService.fetchVoucherForInviteById(voucherId));
	}
	
	public VoucherDto fetchVoucherDtoForInviteByUserId(String userId){
		return modelMapperAdapter.toDto(voucherService.fetchVoucherForInviteByUserId(userId));
	}
}
