package com.ninelives.insurance.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.api.adapter.ModelMapperAdapter;
import com.ninelives.insurance.api.dto.ClaimDocTypeDto;
import com.ninelives.insurance.api.dto.CoverageDto;
import com.ninelives.insurance.api.dto.PeriodDto;
import com.ninelives.insurance.api.dto.ProductDto;
import com.ninelives.insurance.api.model.ClaimDocType;
import com.ninelives.insurance.api.model.Coverage;
import com.ninelives.insurance.api.model.CoverageCategory;
import com.ninelives.insurance.api.model.Period;
import com.ninelives.insurance.api.model.Product;
import com.ninelives.insurance.api.mybatis.mapper.ClaimDocTypeMapper;
import com.ninelives.insurance.api.mybatis.mapper.CoverageCategoryMapper;
import com.ninelives.insurance.api.mybatis.mapper.CoverageMapper;
import com.ninelives.insurance.api.mybatis.mapper.PeriodMapper;
import com.ninelives.insurance.api.mybatis.mapper.ProductMapper;

@Service
public class ProductService {
	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
			
	@Autowired CoverageCategoryMapper coverageCategoryMapper;
	@Autowired CoverageMapper coverageMapper;
	@Autowired PeriodMapper periodMapper;
	@Autowired ProductMapper productMapper;		
	@Autowired ClaimDocTypeMapper claimDocTypeMapper;
	
	@Autowired ModelMapperAdapter modelMapperAdapter;
	
	@Cacheable("ProductDtos")
	public List<ProductDto> fetchProductDtosWithStatusActive(){
		List<Product> products = fetchProductsWithStatusActive();
		List<ProductDto> dtoList = new ArrayList<>();
		for(Product p: products){
			dtoList.add(modelMapperAdapter.toDto(p));
		}
		return dtoList;
	}
	
	public List<Coverage> fetchAllCoverage(){
		//test
		return coverageMapper.selectByStatusActive();
	}
	
	public List<Product> fetchProductByProductIds(Set<String> productIds){
		return productMapper.selectByProductIds(productIds);
	}
	
	@Cacheable("CoverageCategory")
	public CoverageCategory fetchCoverageCategoryByCoverageCategoryId(String coverageCategoryId){
		return coverageCategoryMapper.selectByCoverageCategoryId(coverageCategoryId);
	}
	
	@Cacheable("Coverage")
	public Coverage fetchCoverageByCoverageId(String coverageId){
		return coverageMapper.selectByCoverageId(coverageId);
	}
	
	@Cacheable("Product")
	public Product fetchProductByProductId(String productId){
		return productMapper.selectByProductId(productId);
	}
	
	@Cacheable("ClaimDocType")
	public ClaimDocType fetchClaimDocTypeByClaimDocTypeId(String claimDocTypeId){
		return claimDocTypeMapper.selectByClaimDocTypeId(claimDocTypeId);
	}

	public List<Product> fetchProductsWithStatusActive(){
		return productMapper.selectByStatusActive();
	}
	
	@Cacheable("CoverageDtosAll")
	public List<CoverageDto> fetchActiveCoverageDtos(){
		List<Coverage> coverages = coverageMapper.selectByStatusActive();
		List<CoverageDto> dtoList = new ArrayList<>();
		for(Coverage c: coverages){
			CoverageDto dto = new CoverageDto();
			dto.setCoverageId(c.getCoverageId());
			dto.setName(c.getName());
			dto.setRecommendation(c.getRecommendation());
			dto.setIsRecommended(c.getIsRecommended());
			dto.setHasBeneficiary(c.getHasBeneficiary());
			dto.setMaxLimit(c.getMaxLimit());
			dtoList.add(dto);
		}
		return dtoList;
	}	
	
	@Cacheable("PeriodDtosAll")
	public List<PeriodDto> fetchActivePeriodDtos(){
		List<Period> periods = periodMapper.selectByStatusActive();

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
	
	public List<Period> fetchAllPeriod(){
		return periodMapper.selectByStatusActive();
	}
}
