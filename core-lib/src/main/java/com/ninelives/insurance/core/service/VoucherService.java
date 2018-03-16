package com.ninelives.insurance.core.service;

import java.time.LocalDate;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.core.exception.AppNotFoundException;
import com.ninelives.insurance.core.mybatis.mapper.VoucherMapper;
import com.ninelives.insurance.model.Product;
import com.ninelives.insurance.model.Voucher;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.VoucherType;

@Service
public class VoucherService {
	
	@Autowired ProductService productService;
	@Autowired OrderService orderService;	
	@Autowired VoucherMapper voucherMapper;
	
	@Cacheable("InviteVoucher")
	public Voucher fetchVoucherForInviteById(int voucherId){
		Voucher voucher = voucherMapper.selectVoucherForInviteById(voucherId);
		if(voucher!=null && StringUtils.isEmpty(voucher.getCode())){
			voucher.setCode(null);
		}
		return voucher;
	}
	
	public Voucher fetchVoucherForInviteByUserId(String userId){
		return voucherMapper.selectVoucherForInviteByUserId(userId);
	}
	
	public Voucher fetchVoucherByCode(String code) throws AppNotFoundException{
		return fetchVoucherByCode(code, null);
	}
	
	public Voucher fetchVoucherByCode(String code, VoucherType voucherType) throws AppNotFoundException{
		//TODO return voucher from cache for B2B (non-invite)
		LocalDate today = LocalDate.now();
		
		if(StringUtils.isEmpty(code)){
			throw new AppNotFoundException(ErrorCode.ERR9001_VOUCHER_NOT_FOUND, "Asuransi gratis tidak ditemukan");
		}
		
		Voucher voucher = null;
		
		if(voucherType!=null && VoucherType.INVITE.equals(voucherType)){
			voucher = voucherMapper.selectByInviteCode(code);
		}else{
			voucher = voucherMapper.selectByCode(code);
			
			if(voucher ==null){
				voucher = voucherMapper.selectByInviteCode(code);
			}
		}
		if(voucher!=null){
			if(!CollectionUtils.isEmpty(voucher.getProducts())){
				for(Product p: voucher.getProducts()){
					Product cachedProduct = productService.fetchProductByProductId(p.getProductId());
					p.setName(cachedProduct.getName());
					p.setCoverage(cachedProduct.getCoverage());
					p.setCoverageId(cachedProduct.getCoverageId());
					p.setPeriod(cachedProduct.getPeriod());
					p.setPeriodId(cachedProduct.getPeriodId());
					p.setBasePremi(cachedProduct.getPremi());
					p.setStatus(cachedProduct.getStatus());
				}
				if(voucher.getPeriod()==null){
					voucher.setPeriod(voucher.getProducts().get(0).getPeriod());
				}
			}
			
			//incase of invite voucher, set the code from parameter since the code is not stored in table voucher
			if(StringUtils.isEmpty(voucher.getCode())){
				voucher.setCode(code);
			}
			
			if(voucher.getVoucherType().equals(VoucherType.INVITE)){				
				voucher.setPolicyStartDate(today);
				voucher.setPolicyEndDate(orderService.calculatePolicyEndDate(today, voucher.getPeriod()));
			}else if(voucher.getVoucherType().equals(VoucherType.B2B)){
				if(today.isBefore(voucher.getUseStartDate()) || today.isAfter(voucher.getUseEndDate())){
					throw new AppNotFoundException(ErrorCode.ERR9002_VOUCHER_EXPIRED, "Asuransi gratis ini telah habis masa penawarannya");
				}
			}
		}
		
		if(voucher==null){
			throw new AppNotFoundException(ErrorCode.ERR9001_VOUCHER_NOT_FOUND, "Asuransi gratis tidak ditemukan");
		}
		
		return voucher;
	}
	
	public void increaseInviterRewardCounter(String code, String userId){
		voucherMapper.increamentInviterRewardCount(code, userId);
	}
}