package com.ninelives.insurance.apigateway.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.apigateway.dto.B2bTransactionData;
import com.ninelives.insurance.apigateway.dto.B2bTransactionListDto;
import com.ninelives.insurance.apigateway.dto.B2bTrxListDto;
import com.ninelives.insurance.apigateway.dto.BaseResponse;
import com.ninelives.insurance.apigateway.dto.UserB2bCodeDto;
import com.ninelives.insurance.apigateway.dto.UserB2bCodeListDto;
import com.ninelives.insurance.apigateway.dto.VoucherFormDto;
import com.ninelives.insurance.apigateway.dto.VoucherRegisterDto;
import com.ninelives.insurance.apigateway.dto.VoucherRegisterListDto;
import com.ninelives.insurance.apigateway.dto.VoucherUpdateFormDto;
import com.ninelives.insurance.apigateway.service.ApiCmsService;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.exception.AppNotAuthorizedException;
import com.ninelives.insurance.model.Voucher;

@Controller
@RequestMapping("/api-gateway")
public class B2B2CManagementController extends AbstractWebServiceStatusImpl {

	private static final Logger logger = LoggerFactory.getLogger(B2B2CManagementController.class);
	
	@Autowired 
	ApiCmsService apiCmsService;
	
	@RequestMapping(value="/saveb2b2cvoucher", method=RequestMethod.POST)	
	@ResponseBody
	public BaseResponse  saveB2b2cVoucher(
			@RequestBody VoucherFormDto voucherFormDto
			) throws AppNotAuthorizedException{
		
		final BaseResponse response = new BaseResponse();
		
		String codeVoucher= voucherFormDto.getPromoCode().trim();
		String corporateClientId= voucherFormDto.getCorporateClientId();
		
		try{
			Voucher voucher = apiCmsService.fetchVoucherByCode(codeVoucher,corporateClientId);
			
			if(voucher==null){
				apiCmsService.insertB2B2CVoucher(voucherFormDto);
			}
			else{
				response.setErrMsg("B2B2C Voucher Already Inserted");
		}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setErrMsg(e.getMessage());
			logger.info("****************response saveCorporateClient **********: "+"errMsgs : "+ response.toString());
			return response;
		} 
		
		return response;
	}
	
	@RequestMapping(value="/updateB2b2cVoucherCompany",method=RequestMethod.POST)
	@ResponseBody
	public VoucherRegisterDto updateB2b2cVoucherCompany(
			@RequestBody VoucherUpdateFormDto voucherUpdateFormDto,
			HttpServletResponse response, 
			HttpServletRequest request ) throws AppException{		
		
		logger.debug("Update B2B2C Voucher Client dto:<{}>", voucherUpdateFormDto.toString());
		
		VoucherRegisterDto rValue = apiCmsService.updateB2B2CVoucherClient(voucherUpdateFormDto);
		
		return rValue;
	
	}
	
	
	//In progress
	@RequestMapping(value="/getB2b2cVoucherListByDate", method=RequestMethod.GET)	
	@ResponseBody
	public VoucherRegisterListDto getB2b2cVoucherListByDate(
			@RequestParam String startDate,
			@RequestParam String endDate){
		
		VoucherRegisterListDto voucherListDto = new VoucherRegisterListDto();
		
		List<VoucherRegisterDto> listVoucher = apiCmsService.getListB2B2CVoucherByDate(startDate, endDate);
		
		voucherListDto.setiTotalRecords(listVoucher.size());
		voucherListDto.setiTotalDisplayRecords(10);
		voucherListDto.setsEcho(0);
		voucherListDto.setsColumns("");
		voucherListDto.setAaData(listVoucher);
		
		logger.info("listVoucher : "+ voucherListDto.toString());
		System.out.println(voucherListDto);
		return voucherListDto;
	}
	
	//In progress
	@RequestMapping(value="/getB2b2cVoucherRegister", method=RequestMethod.GET)	
	@ResponseBody
	public VoucherRegisterListDto getListB2B2CVoucherRegister(){
		
		VoucherRegisterListDto voucherListDto = new VoucherRegisterListDto();
	
		List<VoucherRegisterDto> listVoucher = apiCmsService.getListB2B2CVoucherRegister();
		
		voucherListDto.setiTotalRecords(listVoucher.size());
		voucherListDto.setiTotalDisplayRecords(10);
		voucherListDto.setsEcho(0);
		voucherListDto.setsColumns("");
		voucherListDto.setAaData(listVoucher);
		
		logger.info("listVoucher : "+ voucherListDto.toString());
		
		return voucherListDto;
	}
	
	@RequestMapping(value="/getListB2b2cUserbyCode", method=RequestMethod.GET)	
	@ResponseBody
	public UserB2bCodeListDto getListB2b2cUserbyCode(){
		
		UserB2bCodeListDto listDto = new UserB2bCodeListDto();
		List<UserB2bCodeDto> listUserB2b2cCode = apiCmsService.getListUserB2b2cByCode();
		
		listDto.setiTotalRecords(listUserB2b2cCode.size());
		listDto.setiTotalDisplayRecords(11);
		listDto.setsEcho(0);
		listDto.setsColumns("");
		listDto.setAaData(listUserB2b2cCode);
		
		logger.info("list UserB2b2c by Code : "+ listUserB2b2cCode.toString());
		return listDto;
	}
	
	
	@RequestMapping(value="/getListB2b2cUserbyDate", method=RequestMethod.GET)	
	@ResponseBody
	public UserB2bCodeListDto getListB2b2cUserbyDate(
			@RequestParam String startDate,
			@RequestParam String endDate){
		System.out.println(startDate + endDate);
		UserB2bCodeListDto listDto = new UserB2bCodeListDto();
		List<UserB2bCodeDto> listUserB2bCode = apiCmsService.getListUserB2b2cByDate(startDate, endDate);
		
		listDto.setiTotalRecords(listUserB2bCode.size());
		listDto.setiTotalDisplayRecords(11);
		listDto.setsEcho(0);
		listDto.setsColumns("");
		listDto.setAaData(listUserB2bCode);
		
		logger.info("list UserB2b2c by Code : "+ listUserB2bCode.toString());
		
		return listDto;
	}
}
