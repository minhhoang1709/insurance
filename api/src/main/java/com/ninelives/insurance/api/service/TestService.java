package com.ninelives.insurance.api.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.core.mybatis.mapper.TestMapper;
import com.ninelives.insurance.core.service.OrderService;
import com.ninelives.insurance.core.service.ProductService;
import com.ninelives.insurance.model.CoverageCategory;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.Product;
import com.ninelives.insurance.model.UserAggStat;

@Service
@Profile("dev")
public class TestService {
	@Autowired ProductService productService;
	@Autowired TestMapper testMapper;
	@Autowired OrderService orderService;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public CoverageCategory fetchCoverageCategoryByCoverageCategoryId(String coverageCategoryId){
		return productService.fetchCoverageCategoryByCoverageCategoryId(coverageCategoryId);
	}
	
	public List<Product> fetchProductsWithStatusActive(){
		return productService.fetchProductsWithTypeNormalAndStatusActive();
	}
	
	public Product fetchProduct(String productId){
		System.out.println("mau panggil yang di cache oi");
		return productService.fetchProductByProductId(productId);
	}

	public OrderDto changeOrderStatus(String authUserId, OrderDto orderDto) {
		//PolicyStatus status = orderDto.getStatus();
		PolicyOrder order = new PolicyOrder();
		order.setUserId(authUserId);
		order.setOrderId(orderDto.getOrderId());
		order.setStatus(orderDto.getStatus());
		testMapper.updateStatus(order);
		return orderDto;
	}
	
	public void updatespend(String authUserId, int spend){
		UserAggStat stat = new UserAggStat();
		stat.setUserId(authUserId);
		stat.setSuccessPaymentAmount(spend);
		testMapper.updateByUserId(stat);
	}

	public String testGetMaxPolicyEndDate(String userId, String policyEndDateStr, String coverage) {
		LocalDate policyEndDate = LocalDate.parse(policyEndDateStr, formatter);
		List<String> covIds = new ArrayList<>();
		covIds.add(coverage);
		
		LocalDate result = orderService.fetchMaxPolicyEndDateByCoverage(userId, policyEndDate, covIds);
		if(result!=null){
			return result.format(formatter);
		}else{
			return "kagak ketemu";
		}	
	}
}
