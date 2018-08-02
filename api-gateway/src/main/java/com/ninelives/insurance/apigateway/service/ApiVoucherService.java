package com.ninelives.insurance.apigateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.apigateway.adapter.ModelMapperAdapter;
import com.ninelives.insurance.apigateway.dto.VoucherDto;
import com.ninelives.insurance.core.exception.AppNotFoundException;
import com.ninelives.insurance.core.service.VoucherService;

@Service
public class ApiVoucherService {
	
	@Autowired ModelMapperAdapter modelMapperAdapter;
	@Autowired VoucherService voucherService;

	public VoucherDto fetchVoucherDtoByCode(String code) throws AppNotFoundException{
		return modelMapperAdapter.toDto(voucherService.fetchVoucherByCode(code));
	}
	
	@Cacheable("InviteVoucherDto")
	public VoucherDto fetchVoucherDtoForInviteById(int voucherId){
		return modelMapperAdapter.toDto(voucherService.fetchVoucherForInviteById(voucherId));
	}
	
	public VoucherDto fetchVoucherDtoForInviteByUserId(String userId){
		return modelMapperAdapter.toDto(voucherService.fetchVoucherForInviteByUserId(userId));
	}
}
