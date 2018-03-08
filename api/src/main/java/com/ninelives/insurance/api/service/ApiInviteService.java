package com.ninelives.insurance.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.api.adapter.ModelMapperAdapter;
import com.ninelives.insurance.api.dto.InviteDto;
import com.ninelives.insurance.api.dto.VoucherDto;
import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.service.InviteService;
import com.ninelives.insurance.model.UserInviteVoucher;

@Service
public class ApiInviteService {
	
	@Autowired InviteService inviteService;
	@Autowired ApiVoucherService apiVoucherService;
	@Autowired NinelivesConfigProperties config;
	
	
	@Autowired ModelMapperAdapter modelMapperAdapter;
	
	public InviteDto fetchInviteDtoByUserId(String userId){
		InviteDto dto = new InviteDto();
		dto.setHasInvite(inviteService.hasInvite(userId));
		
		VoucherDto inviterVoucherDto =  apiVoucherService.fetchVoucherDtoForInviteByUserId(userId);
		if(inviterVoucherDto == null){
			inviterVoucherDto  = apiVoucherService.fetchVoucherDtoForInviteById(config.getPromo().getInviteVoucherId());
			
			boolean isEligibleGenerateVoucher = inviteService.isEligibleGenerateVoucher(userId);
			
			if(isEligibleGenerateVoucher){
				UserInviteVoucher inviteVoucher = inviteService.generateInviteVoucher(userId, config.getPromo().getInviteVoucherId());
				
				VoucherDto newVoucherDto  = new VoucherDto();
				newVoucherDto.setCode(inviteVoucher.getCode());
				newVoucherDto.setDescription(inviterVoucherDto.getDescription());
				newVoucherDto.setTitle(inviterVoucherDto.getTitle());
				newVoucherDto.setSubtitle(inviterVoucherDto.getSubtitle());
				
				inviterVoucherDto = newVoucherDto;
			}				
		}
		dto.setInviterVoucher(inviterVoucherDto);
		return dto;
	}
		
}
