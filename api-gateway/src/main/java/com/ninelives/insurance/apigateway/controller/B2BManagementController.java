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
import com.ninelives.insurance.apigateway.dto.CompanyRegisterDto;
import com.ninelives.insurance.apigateway.dto.CompanyRegisterListDto;
import com.ninelives.insurance.apigateway.dto.CorporateClientDto;
import com.ninelives.insurance.apigateway.dto.VoucherFormDto;
import com.ninelives.insurance.apigateway.dto.VoucherRegisterDto;
import com.ninelives.insurance.apigateway.dto.VoucherRegisterListDto;
import com.ninelives.insurance.apigateway.dto.VoucherUpdateFormDto;
import com.ninelives.insurance.apigateway.service.ApiCmsService;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.exception.AppNotAuthorizedException;
import com.ninelives.insurance.model.CorporateClient;
import com.ninelives.insurance.model.Voucher;

@Controller
@RequestMapping("/api-gateway")
public class B2BManagementController extends AbstractWebServiceStatusImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(B2BManagementController.class);
	
	@Autowired 
	ApiCmsService apiCmsService;
	
	@RequestMapping(value="/saveCorporateClient", method=RequestMethod.POST)	
	@ResponseBody
	public BaseResponse  saveCorporateClient(
			@RequestBody CorporateClientDto corporateClientDto
			) throws AppNotAuthorizedException{
		
		final BaseResponse response = new BaseResponse();
		
		String companyName= corporateClientDto.getCompanyName().trim();
		String corporateProvId= corporateClientDto.getProviderId().trim();
		
		try{
			CorporateClient corClient = apiCmsService.fetchByCompanyIdAndCorporateId(companyName,
					corporateProvId);
			
			if(corClient==null){
				apiCmsService.insertCorporateClient(corporateClientDto);
				
			}
			else{
				response.setErrMsg("Company Already Registered");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setErrMsg(e.getMessage());
			logger.info("****************response saveCorporateClient **********: "+"errMsgs : "+ response.toString());
			return response;
		} 
		
		return response;
	}

	
	@RequestMapping(value="/getCompanyRegisterList", method=RequestMethod.GET)	
	@ResponseBody
	public CompanyRegisterListDto getAllCompanyRegister(){
		
		CompanyRegisterListDto companyRegisterListDto = new CompanyRegisterListDto();
		List<CompanyRegisterDto> listCompanyRegister = apiCmsService.getListCompanyRegister();
		
		companyRegisterListDto.setiTotalRecords(listCompanyRegister.size());
		companyRegisterListDto.setiTotalDisplayRecords(11);
		companyRegisterListDto.setsEcho(0);
		companyRegisterListDto.setsColumns("");
		companyRegisterListDto.setAaData(listCompanyRegister);
		
		logger.info("listCompanyRegister : "+ listCompanyRegister.toString());
		
		return companyRegisterListDto;
	}	
	
	
	@RequestMapping(value="/updateCorporateClient",method=RequestMethod.POST)
	@ResponseBody
	public CompanyRegisterDto updateCompanyClient(
			@RequestBody CompanyRegisterDto companyRegisterDto,
			HttpServletResponse response, 
			HttpServletRequest request ) throws AppException{		
		
		logger.debug("Update Company Client dto:<{}>", companyRegisterDto.toString());
		
		CompanyRegisterDto rValue = apiCmsService.updateCompanyClient(companyRegisterDto);
		
		return rValue;
	
	}
	
	
	@RequestMapping(value="/saveVoucher", method=RequestMethod.POST)	
	@ResponseBody
	public BaseResponse  saveVoucher(
			@RequestBody VoucherFormDto voucherFormDto
			) throws AppNotAuthorizedException{
		
		final BaseResponse response = new BaseResponse();
		
		String codeVoucher= voucherFormDto.getPromoCode().trim();
		String corporateClientId= voucherFormDto.getCorporateClientId();
		
		try{
			Voucher voucher = apiCmsService.fetchVoucherByCode(codeVoucher,corporateClientId);
			
			if(voucher==null){
				apiCmsService.insertVoucher(voucherFormDto);
			}
			else{
				response.setErrMsg("Voucher Already Inserted");
		}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setErrMsg(e.getMessage());
			logger.info("****************response saveCorporateClient **********: "+"errMsgs : "+ response.toString());
			return response;
		} 
		
		return response;
	}
	
	@RequestMapping(value="/getVoucherRegisterList", method=RequestMethod.GET)	
	@ResponseBody
	public VoucherRegisterListDto getAllVoucherRegister(){
		
		VoucherRegisterListDto voucherRegisterListDto = new VoucherRegisterListDto();
		List<VoucherRegisterDto> listVoucherRegister = apiCmsService.getListVoucherRegister();
		
		voucherRegisterListDto.setiTotalRecords(listVoucherRegister.size());
		voucherRegisterListDto.setiTotalDisplayRecords(11);
		voucherRegisterListDto.setsEcho(0);
		voucherRegisterListDto.setsColumns("");
		voucherRegisterListDto.setAaData(listVoucherRegister);
		
		logger.info("listVoucherRegister : "+ listVoucherRegister.toString());
		
		return voucherRegisterListDto;
	}
	
	@RequestMapping(value="/updateVoucherCompany",method=RequestMethod.POST)
	@ResponseBody
	public VoucherRegisterDto updateVoucherCompany(
			@RequestBody VoucherUpdateFormDto voucherUpdateFormDto,
			HttpServletResponse response, 
			HttpServletRequest request ) throws AppException{		
		
		logger.debug("Update Voucher Client dto:<{}>", voucherUpdateFormDto.toString());
		
		VoucherRegisterDto rValue = apiCmsService.updateVoucherClient(voucherUpdateFormDto);
		
		return rValue;
	
	}
	
	@RequestMapping(value="/getVoucherListByDate", method=RequestMethod.GET)	
	@ResponseBody
	public VoucherRegisterListDto getVoucherListByDate(
			@RequestParam String startDate,
			@RequestParam String endDate){
		
		VoucherRegisterListDto voucherListDto = new VoucherRegisterListDto();
	
		List<VoucherRegisterDto> listVoucher = apiCmsService.getListVoucherByDate(startDate, endDate);
		
		voucherListDto.setiTotalRecords(listVoucher.size());
		voucherListDto.setiTotalDisplayRecords(11);
		voucherListDto.setsEcho(0);
		voucherListDto.setsColumns("");
		voucherListDto.setAaData(listVoucher);
		
		logger.info("listVoucher : "+ voucherListDto.toString());
		
		return voucherListDto;
	}	
	
	
	@RequestMapping(value="/getB2bTransactionList", method=RequestMethod.GET)	
	@ResponseBody
	public B2bTrxListDto getB2BTransactionList(){
		
		B2bTrxListDto listDto = new B2bTrxListDto();
		List<B2bTransactionListDto> listB2bTrx = apiCmsService.getListB2bTransaction();
		
		listDto.setiTotalRecords(listB2bTrx.size());
		listDto.setiTotalDisplayRecords(11);
		listDto.setsEcho(0);
		listDto.setsColumns("");
		listDto.setAaData(listB2bTrx);
		
		logger.info("listVoucher : "+ listB2bTrx.toString());
		
		return listDto;
	}
	
	@RequestMapping(value="/getB2bTransactionData", method=RequestMethod.GET)	
	@ResponseBody
	public B2bTransactionData getB2bTransactionData(){
		
		B2bTransactionData listDto = new B2bTransactionData();
		List<B2bTransactionListDto> listB2bTrx = apiCmsService.getListB2bTransaction();
		
		listDto.setData(listB2bTrx);
		
		logger.info("listDto : "+ listB2bTrx.toString());
		
		return listDto;
	}
	
	@RequestMapping(value="/getB2bTransactionDataByDate", method=RequestMethod.GET)	
	@ResponseBody
	public B2bTransactionData getB2bTransactionDataByDate(
			@RequestParam String startDate,
			@RequestParam String endDate){
		
		B2bTransactionData listDto = new B2bTransactionData();
		List<B2bTransactionListDto> listB2bTrx = 
				apiCmsService.getListB2bTransactionByDate(startDate, endDate);
		
		listDto.setData(listB2bTrx);
		
		logger.info("listDto : "+ listB2bTrx.toString());
		
		return listDto;
	}
	
	
	@RequestMapping(value="/getB2cTransactionData", method=RequestMethod.GET)	
	@ResponseBody
	public B2bTransactionData getB2cTransactionData(){
		
		B2bTransactionData listDto = new B2bTransactionData();
		List<B2bTransactionListDto> listB2bTrx = apiCmsService.getListB2cTransaction();
		
		listDto.setData(listB2bTrx);
		
		logger.info("listDto : "+ listB2bTrx.toString());
		
		return listDto;
	}
	
	@RequestMapping(value="/getB2cTransactionDataByDate", method=RequestMethod.GET)	
	@ResponseBody
	public B2bTransactionData getB2cTransactionDataByDate(
			@RequestParam String startDate,
			@RequestParam String endDate){
		
		B2bTransactionData listDto = new B2bTransactionData();
		List<B2bTransactionListDto> listB2bTrx = 
				apiCmsService.getListB2cTransactionByDate(startDate, endDate);
		
		listDto.setData(listB2bTrx);
		
		logger.info("listDto : "+ listB2bTrx.toString());
		
		return listDto;
	}
	
	
	
}
