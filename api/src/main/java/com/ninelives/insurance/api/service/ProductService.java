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
import com.ninelives.insurance.api.mybatis.mapper.ClaimDocTypeMapper;
import com.ninelives.insurance.api.mybatis.mapper.CoverageCategoryMapper;
import com.ninelives.insurance.api.mybatis.mapper.CoverageMapper;
import com.ninelives.insurance.api.mybatis.mapper.PeriodMapper;
import com.ninelives.insurance.api.mybatis.mapper.ProductMapper;
import com.ninelives.insurance.model.ClaimDocType;
import com.ninelives.insurance.model.Coverage;
import com.ninelives.insurance.model.CoverageCategory;
import com.ninelives.insurance.model.Period;
import com.ninelives.insurance.model.Product;

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
	public List<ProductDto> fetchProductDtosWithTypeNormalAndStatusActive(){
		List<Product> products = fetchProductsWithTypeNormalAndStatusActive();
		List<ProductDto> dtoList = new ArrayList<>();
		for(Product p: products){
			dtoList.add(modelMapperAdapter.toDto(p));
		}
		return dtoList;
	}
	
	@Cacheable("CoverageDtos")
	public List<CoverageDto> fetchCoverageDtosWithStatusActive(){
		List<Coverage> coverages = fetchCoveragesWithStatusActive();
		List<CoverageDto> dtoList = new ArrayList<>();
		for(Coverage c: coverages){			
			dtoList.add(modelMapperAdapter.toDto(c));
		}
		return dtoList;
	}	
	
	@Cacheable("PeriodDtos")
	public List<PeriodDto> fetchPeriodDtosWithStatusActive(){
		List<Period> periods = periodMapper.selectByStatusActive();
		List<PeriodDto> dtoList = new ArrayList<>();
		for(Period c: periods) {			
			dtoList.add(modelMapperAdapter.toDto(c));
		}
		return dtoList;
	}
	
//	public List<Product> fetchProductByProductIds(Set<String> productIds){
//		return productMapper.selectByProductIds(productIds);
//	}
	
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
		//logger.debug("PANGGIL product {}",productId);
		return productMapper.selectByProductId(productId);
	}
	
	@Cacheable("ClaimDocType")
	public ClaimDocType fetchClaimDocTypeByClaimDocTypeId(String claimDocTypeId){
		return claimDocTypeMapper.selectByClaimDocTypeId(claimDocTypeId);
	}

	public List<Product> fetchProductsWithTypeNormalAndStatusActive(){
		return productMapper.selectByTypeNormalAndStatusActive();
	}
	
	public List<Coverage> fetchCoveragesWithStatusActive(){
		return coverageMapper.selectByStatusActive();
	}	
	
	public List<Period> fetchAllPeriod(){
		return periodMapper.selectByStatusActive();
	}
}
