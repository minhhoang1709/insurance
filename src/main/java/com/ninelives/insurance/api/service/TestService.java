package com.ninelives.insurance.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.model.CoverageCategory;
import com.ninelives.insurance.api.model.PolicyOrder;
import com.ninelives.insurance.api.model.Product;
import com.ninelives.insurance.api.mybatis.mapper.TestMapper;
import com.ninelives.insurance.api.ref.PolicyStatus;

@Service
public class TestService {
	@Autowired ProductService productService;
	@Autowired TestMapper testMapper;
	
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

	public OrderDto changeOrderStatus(String authUserId, OrderDto orderDto) {
		// TODO Auto-generated method stub
		//PolicyStatus status = orderDto.getStatus();
		PolicyOrder order = new PolicyOrder();
		order.setUserId(authUserId);
		order.setOrderId(orderDto.getOrderId());
		order.setStatus(orderDto.getStatus());
		testMapper.updateStatus(order);
		return orderDto;
	}
}
