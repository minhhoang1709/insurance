package com.ninelives.insurance.apigateway.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.apigateway.dto.BaseResponse;
import com.ninelives.insurance.apigateway.dto.CompanyRegisterDto;
import com.ninelives.insurance.apigateway.dto.CorporateClientDto;
import com.ninelives.insurance.apigateway.service.ApiCmsService;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.exception.AppNotAuthorizedException;
import com.ninelives.insurance.model.CorporateClient;

public class CorporateClientManagementController extends AbstractWebServiceStatusImpl {

	private static final Logger logger = LoggerFactory.getLogger(CorporateClientManagementController.class);
	
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
}
