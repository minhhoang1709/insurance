package com.ninelives.insurance.api.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.SubmitOrderDto;
import com.ninelives.insurance.api.exception.ApiBadRequestException;
import com.ninelives.insurance.api.exception.ApiException;
import com.ninelives.insurance.api.exception.ApiNotFoundException;
import com.ninelives.insurance.api.model.Product;
import com.ninelives.insurance.api.mybatis.mapper.ProductMapper;
import com.ninelives.insurance.api.ref.ErrorCode;
import com.ninelives.insurance.api.service.OrderService;
import com.ninelives.insurance.api.service.ProductService;

@Controller
public class TestController {
	
	@Autowired public ProductService productService;
	@Autowired public OrderService orderService;
	@Autowired ProductMapper productMapper;
	
	@RequestMapping("/test/error/generic")
	public String errorGeneric() throws Exception{
		
		throw new Exception("olalala");		
		//return "ok";
	}
	@RequestMapping("/test/error/login")
	public String errorLogin() throws ApiNotFoundException{
		
		throw new ApiNotFoundException(ErrorCode.ERR2001_LOGIN_FAILURE,"olalala login gagal");		
		//return "ok";
	}
	
	@RequestMapping("/test/product")
	@ResponseBody
	public List<Product> getproduct(){
		return null;
	}
	
	@RequestMapping("/test/product/bylist")
	@ResponseBody
	public List<Product> getproductByList(){	
		//List<String> productIds = Arrays.asList("P101004102","P101003102","P101006102");
		List<String> productIds = Arrays.asList("P10");
		List<Product> products = productService.fetchProductByProductIds(productIds);
		if(products==null){
			System.out.println("NULL productlist");
		}
		return products;
	}
	
	@RequestMapping("/test/order")
	@ResponseBody
	public OrderDto order(@RequestAttribute("authUserId") String authUserId, @RequestBody(required=false) SubmitOrderDto submitOrder) throws ApiException{	
		//List<String> productIds = Arrays.asList("P101004102","P101003102","P101006102");
		return orderService.submitOrder(authUserId, submitOrder);
	}
	
	@RequestMapping("/test/conflictorder")
	@ResponseBody
	public List conflictOrder(@RequestAttribute("authUserId") String authUserId, @RequestBody(required=false) SubmitOrderDto submitOrder) throws ApiException{	
		//List<String> productIds = Arrays.asList("P101004102","P101003102","P101006102");
		return orderService.testConflict(authUserId, submitOrder);
	}
}
