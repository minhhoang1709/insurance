package com.ninelives.insurance.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.api.dto.CoverageDto;
import com.ninelives.insurance.api.dto.PeriodDto;
import com.ninelives.insurance.api.dto.ProductDto;
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
		//test
		return productMapper.selectByStatusActive();
	}
	
	public List<Coverage> fetchAllCoverage(){
		//test
		return coverageMapper.selectByStatusActive();
	}
	
	public List<Product> fetchProductByProductIds(List<String> productIds){
		return productMapper.selectByProductIds(productIds);
	}
	
	@Cacheable("ProductDtosAll")
	public List<ProductDto> fetchActiveProductDtos(){
		List<Product> products = productMapper.selectByStatusActive();
		
		List<ProductDto> dtoList = new ArrayList<>();
		for(Product p: products){
			ProductDto productDto = new ProductDto();
			productDto.setProductId(p.getProductId());			
			productDto.setName(p.getName());
			productDto.setPremi(p.getPremi());
			
			if(p.getCoverage()!=null){
				CoverageDto coverageDto = new CoverageDto();
				coverageDto.setCoverageId(p.getCoverageId());
				coverageDto.setName(p.getCoverage().getName());
				coverageDto.setRecommendation(p.getCoverage().getRecommendation());
				coverageDto.setHasBeneficiary(p.getCoverage().getHasBeneficiary());
				coverageDto.setMaxLimit(p.getCoverage().getMaxLimit());				
				productDto.setCoverage(coverageDto);
			}
			
			if(p.getPeriod()!=null){
				PeriodDto periodDto = new PeriodDto();
				periodDto.setPeriodId(p.getPeriodId());
				periodDto.setName(p.getPeriod().getName());
				periodDto.setUnit(p.getPeriod().getUnit());
				periodDto.setValue(p.getPeriod().getValue());
				productDto.setPeriod(periodDto);
			}

			dtoList.add(productDto);
		}
		return dtoList;
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
