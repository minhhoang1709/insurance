package com.ninelives.insurance.api.service;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.config.NinelivesConfigProperties;
import com.ninelives.insurance.api.adapter.ModelMapperAdapter;
import com.ninelives.insurance.api.dto.InviteDto;
import com.ninelives.insurance.api.dto.VoucherDto;
import com.ninelives.insurance.api.mybatis.mapper.UserAggStatMapper;
import com.ninelives.insurance.api.mybatis.mapper.UserInviteVoucherMapper;
import com.ninelives.insurance.model.UserAggStat;
import com.ninelives.insurance.model.UserInviteVoucher;
import com.ninelives.insurance.model.Voucher;
import com.ninelives.insurance.ref.InviteVoucherStatus;
import com.ninelives.insurance.util.RandomStringUtil;

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
			
			boolean isEligibleGenerateVoucher = isEligibleGenerateVoucher(userId);
			
			if(isEligibleGenerateVoucher){
				UserInviteVoucher inviteVoucher = generateInviteVoucher(userId, config.getPromo().getInviteVoucherId());
				
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
	
	public boolean isEligibleGenerateVoucher(String userId){
		//check spend, if enough spend, generate code, otherwise return generic
		UserAggStat aggStat = userAggStatMapper.selecSuccessPaymentAmountAndB2BUseCntByUserId(userId);
		if(aggStat!=null){
			if ((aggStat.getSuccessPaymentAmount() != null
					&& aggStat.getSuccessPaymentAmount() >= config.getPromo().getVoucherMinimumAggregatePayment())
					|| (aggStat.getVoucherB2bUseCnt() != null && aggStat.getVoucherB2bUseCnt() > 0)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasInvite(String userId){
		if(config.getPromo().getIsPromoAvailable()){
			return true;
		}else{
			return !orderService.hasPaidOrder(userId);
		}
	}
	
	public UserInviteVoucher generateInviteVoucher(String userId, int voucherId){
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
