package com.ninelives.insurance.api.controller;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.api.dto.CoverageDto;
import com.ninelives.insurance.api.dto.PeriodDto;
import com.ninelives.insurance.api.dto.ProductDto;
import com.ninelives.insurance.api.service.ApiProductService;

@Controller
@RequestMapping("/api")
public class ProductController {
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired public ApiProductService apiProductService;
	
	@RequestMapping(value="/products",
			method=RequestMethod.GET)	
	@ResponseBody
	public List<ProductDto> getProducts(Locale locale){
		return apiProductService.fetchProductDtosWithTypeNormalAndStatusActive(locale.getCountry(), locale.getLanguage());
	}
	
	@RequestMapping(value="/coverages",
			method=RequestMethod.GET)	
	@ResponseBody
	public List<CoverageDto> getCoverages(){
		return apiProductService.fetchCoverageDtosWithStatusActive();
	}
	
//	@RequestMapping(value="/periods",
//			method=RequestMethod.GET)	
//	@ResponseBody
//	public List<PeriodDto> getPeriods(){		
//		return productService.fetchPeriodDtosWithStatusActive();				
//	}
}
