package com.ninelives.insurance.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.api.model.Coverage;
import com.ninelives.insurance.api.model.Period;
import com.ninelives.insurance.api.model.Product;
import com.ninelives.insurance.api.mybatis.mapper.CoverageMapper;
import com.ninelives.insurance.api.mybatis.mapper.PeriodMapper;
import com.ninelives.insurance.api.mybatis.mapper.ProductMapper;

@Service
public class ProductService {
	
	@Autowired CoverageMapper coverageMapper;
	@Autowired PeriodMapper periodMapper;
	@Autowired ProductMapper productMapper;
	
	public List<Product> fetchAllProduct(){
		return productMapper.selectAll();
	}
	
	public List<Coverage> fetchAllCoverage(){
		return coverageMapper.selectAll();
	}
	
	public List<Period> fetchAllPeriod(){
		return periodMapper.selectAll();
	}
}
