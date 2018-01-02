package com.ninelives.insurance.api.service;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.api.NinelivesConfigProperties;
import com.ninelives.insurance.api.adapter.ModelMapperAdapter;
import com.ninelives.insurance.api.dto.InviteDto;
import com.ninelives.insurance.api.dto.VoucherDto;
import com.ninelives.insurance.api.mybatis.mapper.UserAggStatMapper;
import com.ninelives.insurance.api.mybatis.mapper.UserInviteVoucherMapper;
import com.ninelives.insurance.model.UserInviteVoucher;
import com.ninelives.insurance.model.Voucher;
import com.ninelives.insurance.model.util.RandomStringUtil;
import com.ninelives.insurance.ref.InviteVoucherStatus;

@Service
public class InviteService {
	@Autowired OrderService orderService;
	@Autowired VoucherService voucherService;
	@Autowired NinelivesConfigProperties config;
	
	@Autowired UserAggStatMapper userAggStatMapper;
	@Autowired UserInviteVoucherMapper userInviteVoucherMapper;
	
	@Autowired ModelMapperAdapter modelMapperAdapter;
	
	public InviteDto fetchInviteDtoByUserId(String userId){
		InviteDto dto = new InviteDto();
		dto.setHasInvite(hasInvite(userId));
		
		VoucherDto inviterVoucherDto =  voucherService.fetchVoucherDtoForInviteByUserId(userId);
		if(inviterVoucherDto == null){
			inviterVoucherDto  = voucherService.fetchVoucherDtoForInviteById(config.getPromo().getInviteVoucherId());
			
			//check spend, if enough spend, generate code, otherwise return generic
			Integer totalSpend = userAggStatMapper.selecSuccessPaymentAmounttByUserId(userId);
			
			if(totalSpend!=null && totalSpend >= config.getPromo().getVoucherMinimumAggregatePayment()){
				UserInviteVoucher inviteVoucher = generateInviteVoucher(userId, config.getPromo().getInviteVoucherId());
				
				VoucherDto newVoucherDto  = new VoucherDto();
				newVoucherDto.setCode(inviteVoucher.getCode());
				newVoucherDto.setDescription(inviterVoucherDto.getDescription());
				newVoucherDto.setTitle(inviterVoucherDto.getTitle());
				newVoucherDto.setSubtitle(inviterVoucherDto.getSubtitle());
				
				inviterVoucherDto = newVoucherDto;
			}				
		}
		//Voucher inviterVoucher =  
		dto.setInviterVoucher(inviterVoucherDto);
		return dto;
	}
	
	public boolean hasInvite(String userId){
		if(config.getPromo().getIsPromoAvailable()){
			return true;
		}else{
			return !orderService.hasPaidOrder(userId);
		}
	}
	
	public UserInviteVoucher generateInviteVoucher(String userId, int voucherId){
		//random text
		//insert into database, return not voucher but userinvite?
		String text = RandomStringUtil.generate(config.getPromo().getVoucherCodeLength());
		
		UserInviteVoucher inviteVoucher = new UserInviteVoucher();
		inviteVoucher.setCode(text);
		inviteVoucher.setVoucherId(voucherId);
		inviteVoucher.setInviterRewardCount(0);
		inviteVoucher.setUserId(userId);
		inviteVoucher.setStatus(InviteVoucherStatus.ACTIVE);
		
		userInviteVoucherMapper.insert(inviteVoucher);
		
		return inviteVoucher;
	}	
}
