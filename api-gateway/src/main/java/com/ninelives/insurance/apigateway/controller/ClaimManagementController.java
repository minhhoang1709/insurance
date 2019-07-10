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

import com.ninelives.insurance.apigateway.dto.ClaimManagementDto;
import com.ninelives.insurance.apigateway.dto.ClaimManagementListDto;
import com.ninelives.insurance.apigateway.dto.ClaimStatusDto;
import com.ninelives.insurance.apigateway.service.ApiCmsService;
import com.ninelives.insurance.core.exception.AppException;

@Controller
@RequestMapping("/api-gateway")
public class ClaimManagementController extends AbstractWebServiceStatusImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(ClaimManagementController.class);
	
	@Autowired 
	ApiCmsService apiCmsService;
	
	
	@RequestMapping(value="/getClaimManagementList", method=RequestMethod.GET)	
	@ResponseBody
	public ClaimManagementListDto getClaimManagementList(){
		
		ClaimManagementListDto claimManagementListDto = new ClaimManagementListDto();
		List<ClaimManagementDto> listClaimManagement = apiCmsService.getListClaimManagement();
		
		claimManagementListDto.setiTotalRecords(listClaimManagement.size());
		claimManagementListDto.setiTotalDisplayRecords(11);
		claimManagementListDto.setsEcho(0);
		claimManagementListDto.setsColumns("");
		claimManagementListDto.setAaData(listClaimManagement);
		
		logger.info("listClaimManagement : "+ listClaimManagement.toString());
		
		return claimManagementListDto;
	}
	
	@RequestMapping(value="/getClaimListByDate", method=RequestMethod.GET)	
	@ResponseBody
	public ClaimManagementListDto getClaimListByDate(
			@RequestParam String startDate,
			@RequestParam String endDate){	
		
		ClaimManagementListDto claimManagementListDto = new ClaimManagementListDto();
		List<ClaimManagementDto> listClaimManagement = apiCmsService.getListClaimManagementByDate(startDate, endDate);
		
		claimManagementListDto.setiTotalRecords(listClaimManagement.size());
		claimManagementListDto.setiTotalDisplayRecords(11);
		claimManagementListDto.setsEcho(0);
		claimManagementListDto.setsColumns("");
		claimManagementListDto.setAaData(listClaimManagement);
		
		logger.info("listClaimManagement : "+ listClaimManagement.toString());
		
		return claimManagementListDto;
	}
	
	
	@RequestMapping(value="/updateClaimStatus",method=RequestMethod.POST)
	@ResponseBody
	public ClaimStatusDto updateClaimStatus(
			@RequestBody ClaimStatusDto claimStatusDto,
			HttpServletResponse response, 
			HttpServletRequest request ) throws AppException{		
		
		logger.debug("Update Claim Status dto:<{}>", claimStatusDto.toString());
		
		ClaimStatusDto rValue = apiCmsService.updateClaimStatus(claimStatusDto);
		
		return rValue;
	
	}
	
	
}
