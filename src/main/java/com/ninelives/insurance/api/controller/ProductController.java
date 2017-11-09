package com.ninelives.insurance.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.api.dto.CoverageDto;
import com.ninelives.insurance.api.dto.PeriodDto;
import com.ninelives.insurance.api.dto.ProductDto;
import com.ninelives.insurance.api.model.Coverage;
import com.ninelives.insurance.api.model.Period;
import com.ninelives.insurance.api.model.Product;
import com.ninelives.insurance.api.model.Users;
import com.ninelives.insurance.api.service.ProductService;

@Controller
public class ProductController {
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired public ProductService productService;
	
	@RequestMapping(value="/products",
			method=RequestMethod.GET)	
	@ResponseBody
	public List<ProductDto> getProducts( @RequestBody(required=false) Map<String, String> requestData, HttpServletResponse response){
		
		List<Product> products = productService.fetchAllProduct();
		List<Coverage> coverages = productService.fetchAllCoverage();
		List<Period> periods = productService.fetchAllPeriod();
		
		Map<String,CoverageDto> covMap = new HashMap<>();
		for(Coverage c: coverages){
			CoverageDto dto = new CoverageDto();
			dto.setCoverageId(c.getCoverageId());
			dto.setName(c.getName());
			dto.setRecommendation(c.getRecommendation());
			dto.setHasBeneficiary(c.getHasBeneficiary());
			dto.setMaxLimit(c.getMaxLimit());
			covMap.put(dto.getCoverageId(), dto);
		}
		
		Map<String,PeriodDto> perMap = new HashMap<>();
		for(Period c: periods) {
			PeriodDto dto = new PeriodDto();
			dto.setPeriodId(c.getPeriodId());
			dto.setName(c.getName());
			dto.setValue(c.getValue());
			dto.setUnit(c.getUnit());
			perMap.put(c.getPeriodId(), dto);
		}
		
		List<ProductDto> productDtos = new ArrayList<>();
		
		for(Product p: products){
			ProductDto productDto = new ProductDto();
			productDto.setProductId(p.getProductId());			
			productDto.setName(p.getName());
			productDto.setPremi(p.getPremi());
			productDto.setCoverage(covMap.get(p.getCoverageId()));
			productDto.setPeriod(perMap.get(p.getPeriodId()));
			productDtos.add(productDto);
		}
		
		return productDtos;
	}
	
	@RequestMapping(value="/coverages",
			method=RequestMethod.GET)	
	@ResponseBody
	public List<CoverageDto> getCoverages( @RequestBody(required=false) Map<String, String> requestData, HttpServletResponse response){
		
		List<Coverage> coverages = productService.fetchAllCoverage();
	
		List<CoverageDto> dtoList = new ArrayList<>();
		for(Coverage c: coverages){
			CoverageDto dto = new CoverageDto();
			dto.setCoverageId(c.getCoverageId());
			dto.setName(c.getName());
			dto.setRecommendation(c.getRecommendation());
			dto.setHasBeneficiary(c.getHasBeneficiary());
			dto.setMaxLimit(c.getMaxLimit());
			dtoList.add(dto);
		}
		
		
		return dtoList;
	}
	
	@RequestMapping(value="/periods",
			method=RequestMethod.GET)	
	@ResponseBody
	public List<PeriodDto> getPeriods( @RequestBody(required=false) Map<String, String> requestData, HttpServletResponse response){
		
		List<Period> periods = productService.fetchAllPeriod();

		List<PeriodDto> dtoList = new ArrayList<>();
		for(Period c: periods) {
			PeriodDto dto = new PeriodDto();
			dto.setPeriodId(c.getPeriodId());
			dto.setName(c.getName());
			dto.setValue(c.getValue());
			dto.setUnit(c.getUnit());
			dtoList.add(dto);
		}
		
		return dtoList;
	}
}
