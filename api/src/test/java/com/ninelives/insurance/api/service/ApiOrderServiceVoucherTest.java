package com.ninelives.insurance.api.service;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.Test;

import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.ProductDto;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.service.VoucherService;
import com.ninelives.insurance.ref.ErrorCode;

public class ApiOrderServiceVoucherTest {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	@Test
	public void testValidateVoucherInvalidVoucherCode(){
		ApiOrderService apiService = new ApiOrderService();
		VoucherService voucherService = new VoucherService();
		
		apiService.voucherService=voucherService;
		
		OrderDto dto = new OrderDto();
		dto.setPolicyStartDate(LocalDateTime.parse("2018-04-01 00:00",formatter));
		dto.setPolicyEndDate(LocalDateTime.parse("2018-04-01 00:00",formatter));
		dto.setProducts(new ArrayList<ProductDto>());
		dto.getProducts().add(new ProductDto());
		dto.getProducts().get(0).setProductId("P101001105001");
		dto.getProducts().get(0).setPremi(0);
		dto.getProducts().add(new ProductDto());
		dto.getProducts().get(1).setProductId("P101002105001");
		dto.getProducts().get(1).setPremi(0);
		dto.getProducts().add(new ProductDto());
		dto.getProducts().get(2).setProductId("P101003105001");
		dto.getProducts().get(2).setPremi(0);
				
		String userId = "xxxx";
		
		try {
			apiService.registerOrder(userId, null, true);
		} catch (AppException e) {
			// TODO Auto-generated catch block
			assertEquals(ErrorCode.ERR4000_ORDER_INVALID,e.getCode());
		}
		
	}
//	
//	@Test
//	public void testValidateVoucherInvalidVoucherCode(){
//		
//	}
}
