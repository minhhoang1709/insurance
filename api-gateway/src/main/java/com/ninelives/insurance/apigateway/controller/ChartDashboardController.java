package com.ninelives.insurance.apigateway.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.apigateway.dto.DataProviderAgeDto;
import com.ninelives.insurance.apigateway.dto.DataProviderAgeListDto;
import com.ninelives.insurance.apigateway.dto.DataProviderGenderDto;
import com.ninelives.insurance.apigateway.dto.DataProviderGenderListDto;
import com.ninelives.insurance.apigateway.dto.StringDto;
import com.ninelives.insurance.apigateway.service.ApiCmsService;

@Controller
@RequestMapping("/api-gateway")
public class ChartDashboardController extends AbstractWebServiceStatusImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(ChartDashboardController.class);
	
	@Autowired 
	ApiCmsService apiCmsService;
	
	
	@RequestMapping(value="/getGenderChart", method=RequestMethod.GET)	
	@ResponseBody
	public DataProviderGenderListDto getGenderChartList(){
		
		DataProviderGenderListDto listDto = new DataProviderGenderListDto();
		List<DataProviderGenderDto> listDataProvider = apiCmsService.getListGenderChart();
	
		listDto.setDataProvider(listDataProvider);
		
		logger.info("listDataProvider : "+ listDataProvider.toString());
		
		return listDto;
	}
	
	
	
	@RequestMapping(value="/getAgeChart", method=RequestMethod.GET)	
	@ResponseBody
	public DataProviderAgeListDto getAgeChartList(){
		
		DataProviderAgeListDto listDto = new DataProviderAgeListDto();
		List<DataProviderAgeDto> listDataProvider = apiCmsService.getListAgeChart();
	
		listDto.setDataProvider(listDataProvider);
		
		logger.info("listDataProvider : "+ listDataProvider.toString());
		
		return listDto;
	}
	
	
	@RequestMapping(value="/getAgeNoRangeChart", method=RequestMethod.GET)	
	@ResponseBody
	public DataProviderAgeListDto getAgeNoRangeChartList(){
		
		DataProviderAgeListDto listDto = new DataProviderAgeListDto();
		List<DataProviderAgeDto> listDataProvider = apiCmsService.getListAgeNoRangeChart();
	
		listDto.setDataProvider(listDataProvider);
		
		logger.info("listDataProvider : "+ listDataProvider.toString());
		
		return listDto;
	}
	
	@RequestMapping(value="/getDemographicCount", method=RequestMethod.GET)	
	@ResponseBody
	public StringDto getDemographicCount(){
		StringDto rValue = new StringDto();
		rValue = apiCmsService.getDemographicCount();
		
		return rValue;
	}
	
}
