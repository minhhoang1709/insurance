package com.ninelives.insurance.apigateway.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.apigateway.dto.BaseResponse;
import com.ninelives.insurance.apigateway.dto.FreeVoucherFormDto;
import com.ninelives.insurance.apigateway.dto.PromoCodeDto;
import com.ninelives.insurance.apigateway.dto.PromoCodeListDto;
import com.ninelives.insurance.apigateway.service.ApiCmsService;
import com.ninelives.insurance.core.exception.AppNotAuthorizedException;
import com.ninelives.insurance.model.Voucher;

@Controller
@RequestMapping("/api-gateway")
public class PromoManagementController extends AbstractWebServiceStatusImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(PromoManagementController.class);
	
	@Autowired 
	ApiCmsService apiCmsService;
	
	@RequestMapping(value="/saveFreeVoucher", method=RequestMethod.POST)	
	@ResponseBody
	public BaseResponse  saveFreeVoucher(
			@RequestBody FreeVoucherFormDto freeVoucherFormDto
			) throws AppNotAuthorizedException{
		
		final BaseResponse response = new BaseResponse();
		String codeVoucher= freeVoucherFormDto.getPromoCode().trim();
		try{
			Voucher voucher = apiCmsService.fetchFreeVoucherByCode("FREE_PROMO_NEW_USER",codeVoucher);
			if(voucher==null){
				apiCmsService.insertFreeVoucher(freeVoucherFormDto);
			}
			else{
				response.setErrMsg("Voucher Already Inserted");
			}
		}catch (Exception e) {
			e.printStackTrace();
			response.setErrMsg(e.getMessage());
			logger.info("****************response saveCorporateClient **********: "+"errMsgs : "+ response.toString());
			return response;
		} 
		
		return response;
	}
	
	
	@RequestMapping(value="/getPromoCodeList", method=RequestMethod.GET)	
	@ResponseBody
	public PromoCodeListDto getAllPromoCodeList(){
		
		PromoCodeListDto listDto = new PromoCodeListDto();
		List<PromoCodeDto> listPromoCode = apiCmsService.getListPromoCode();
		
		listDto.setiTotalRecords(listPromoCode.size());
		listDto.setiTotalDisplayRecords(11);
		listDto.setsEcho(0);
		listDto.setsColumns("");
		listDto.setAaData(listPromoCode);
		
		logger.info("ListPromoCode : "+ listPromoCode.toString());
		
		return listDto;
	}
	
	
	
	@RequestMapping(value="/getPromoCodeListByDate", method=RequestMethod.GET)	
	@ResponseBody
	public PromoCodeListDto getPromoCodeListByDate(
			@RequestParam String startDate,
			@RequestParam String endDate){
		
		PromoCodeListDto listDto = new PromoCodeListDto();
	
		List<PromoCodeDto> listPromoCode = apiCmsService.getListPromoCodeByDate(startDate, endDate);
		
		listDto.setiTotalRecords(listPromoCode.size());
		listDto.setiTotalDisplayRecords(11);
		listDto.setsEcho(0);
		listDto.setsColumns("");
		listDto.setAaData(listPromoCode);
		
		logger.info("listPromoCode : "+ listPromoCode.toString());
		
		return listDto;
		
	}	
	
	
	
}
