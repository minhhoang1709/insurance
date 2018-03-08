package com.ninelives.insurance.core.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.core.mybatis.mapper.ClaimDocTypeMapper;
import com.ninelives.insurance.core.mybatis.mapper.CoverageCategoryMapper;
import com.ninelives.insurance.core.mybatis.mapper.CoverageMapper;
import com.ninelives.insurance.core.mybatis.mapper.PeriodMapper;
import com.ninelives.insurance.core.mybatis.mapper.ProductMapper;
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
	
	public List<Period> fetchActivePeriod(){
		return periodMapper.selectByStatusActive();
	}
}
