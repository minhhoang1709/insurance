package com.ninelives.insurance.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.api.model.CoverageCategory;
import com.ninelives.insurance.api.model.Product;

@Service
public class TestService {
	@Autowired ProductService productService;
	
	public CoverageCategory fetchCoverageCategoryByCoverageCategoryId(String coverageCategoryId){
		return productService.fetchCoverageCategoryByCoverageCategoryId(coverageCategoryId);
	}
	
	public List<Product> fetchProductsWithStatusActive(){
		return productService.fetchProductsWithStatusActive();
	}
	
	public Product fetchProduct(String productId){
		System.out.println("mau panggil yang di cache oi");
		return productService.fetchProductByProductId(productId);
	}
}
