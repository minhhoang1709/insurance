package com.ninelives.insurance.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.api.adapter.ModelMapperAdapter;
import com.ninelives.insurance.api.dto.VoucherDto;
import com.ninelives.insurance.api.exception.ApiNotFoundException;
import com.ninelives.insurance.api.service.VoucherService;

@Controller
@RequestMapping("/api")
public class VoucherController {
	private static final Logger logger = LoggerFactory.getLogger(VoucherController.class);
	
	@Autowired VoucherService voucherService;	
	@Autowired ModelMapperAdapter modelMapperAdapter;
	
	@RequestMapping(value="/vouchers/{code}",
			method={ RequestMethod.GET })
	@ResponseBody
	public VoucherDto getVoucher(@RequestAttribute ("authUserId") String authUserId,
			@PathVariable("code") String code) throws ApiNotFoundException{
		
		return voucherService.fetchVoucherDtoByCode(code);			
	}
}
