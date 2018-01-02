package com.ninelives.insurance.api.controller;

import java.util.List;

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
import com.ninelives.insurance.api.service.ProductService;

@Controller
public class ProductController {
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired public ProductService productService;
	
	@RequestMapping(value="/products",
			method=RequestMethod.GET)	
	@ResponseBody
	public List<ProductDto> getProducts(){
		return productService.fetchProductDtosWithTypeNormalAndStatusActive();
	}
	
	@RequestMapping(value="/coverages",
			method=RequestMethod.GET)	
	@ResponseBody
	public List<CoverageDto> getCoverages(){
		return productService.fetchCoverageDtosWithStatusActive();
	}
	
//	@RequestMapping(value="/periods",
//			method=RequestMethod.GET)	
//	@ResponseBody
//	public List<PeriodDto> getPeriods(){		
//		return productService.fetchPeriodDtosWithStatusActive();				
//	}
}
