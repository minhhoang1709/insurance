package com.ninelives.insurance.api.service;

import java.time.LocalDate;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.api.adapter.ModelMapperAdapter;
import com.ninelives.insurance.api.dto.InviteDto;
import com.ninelives.insurance.api.dto.VoucherDto;
import com.ninelives.insurance.api.exception.ApiNotFoundException;
import com.ninelives.insurance.api.mybatis.mapper.VoucherMapper;
import com.ninelives.insurance.model.Product;
import com.ninelives.insurance.model.Voucher;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.VoucherType;

@Service
public class VoucherService {
	
	@Autowired ProductService productService;
	@Autowired OrderService orderService;
	@Autowired ModelMapperAdapter modelMapperAdapter;
	@Autowired VoucherMapper voucherMapper;
	

	public VoucherDto fetchVoucherDtoByCode(String code) throws ApiNotFoundException{
		return modelMapperAdapter.toDto(fetchVoucherByCode(code));
	}
	
	@Cacheable("InviteVoucherDto")
	public VoucherDto fetchVoucherDtoForInviteById(int voucherId){
		return modelMapperAdapter.toDto(fetchVoucherForInviteById(voucherId));
	}
	
	public VoucherDto fetchVoucherDtoForInviteByUserId(String userId){
		return modelMapperAdapter.toDto(fetchVoucherForInviteByUserId(userId));
	}
	
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
	
	
	public Voucher fetchVoucherByCode(String code) throws ApiNotFoundException{
		//TODO return voucher from cache for B2B (non-invite)
		
		if(StringUtils.isEmpty(code)){
			throw new ApiNotFoundException(ErrorCode.ERR9001_VOUCHER_NOT_FOUND, "Asuransi gratis tidak ditemukan");
		}
		String upper = code.toUpperCase();
		
		Voucher voucher = voucherMapper.selectByCode(upper);
		
		if(voucher ==null){
			voucher = voucherMapper.selectByInviteCode(upper);
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
				voucher.setCode(upper);
			}
			
			if(voucher.getVoucherType().equals(VoucherType.INVITE)){
				LocalDate today = LocalDate.now();
				voucher.setPolicyStartDate(today);
				voucher.setPolicyEndDate(orderService.calculatePolicyEndDate(today, voucher.getPeriod()));
			}
		}
		
		if(voucher==null){
			throw new ApiNotFoundException(ErrorCode.ERR9001_VOUCHER_NOT_FOUND, "Asuransi gratis tidak ditemukan");
		}
		
		return voucher;
	}
	
}
