package com.ninelives.insurance.api.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.api.adapter.ModelMapperAdapter;
import com.ninelives.insurance.api.dto.CoverageDto;
import com.ninelives.insurance.api.dto.PeriodDto;
import com.ninelives.insurance.api.dto.ProductDto;
import com.ninelives.insurance.core.service.ProductService;
import com.ninelives.insurance.model.Coverage;
import com.ninelives.insurance.model.Period;
import com.ninelives.insurance.model.Product;

@Service
public class ApiProductService {
	private static final Logger logger = LoggerFactory.getLogger(ApiProductService.class);
			
	@Autowired ModelMapperAdapter modelMapperAdapter;
	@Autowired ProductService productService;
	
	@Cacheable("ProductDtos")
	public List<ProductDto> fetchProductDtosWithTypeNormalAndStatusActive(){
		List<Product> products = productService.fetchProductsWithTypeNormalAndStatusActive();
		List<ProductDto> dtoList = new ArrayList<>();
		for(Product p: products){
			dtoList.add(modelMapperAdapter.toDto(p));
		}
		return dtoList;
	}
	
	@Cacheable("CoverageDtos")
	public List<CoverageDto> fetchCoverageDtosWithStatusActive(){
		List<Coverage> coverages = productService.fetchCoveragesWithStatusActive();
		List<CoverageDto> dtoList = new ArrayList<>();
		for(Coverage c: coverages){			
			dtoList.add(modelMapperAdapter.toDto(c));
		}
		return dtoList;
	}	
	
	@Cacheable("PeriodDtos")
	public List<PeriodDto> fetchPeriodDtosWithStatusActive(){
		List<Period> periods = productService.fetchActivePeriod();
		List<PeriodDto> dtoList = new ArrayList<>();
		for(Period c: periods) {			
			dtoList.add(modelMapperAdapter.toDto(c));
		}
		return dtoList;
	}
	
}
