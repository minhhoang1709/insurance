package com.ninelives.insurance.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.api.exception.NotFoundException;
import com.ninelives.insurance.api.model.Product;
import com.ninelives.insurance.api.mybatis.mapper.ProductMapper;
import com.ninelives.insurance.api.ref.ErrorCode;
import com.ninelives.insurance.api.service.ProductService;

@Controller
public class TestController {
	
	@Autowired public ProductService productService;
	@Autowired ProductMapper productMapper;
	
	@RequestMapping("/test/error/generic")
	public String errorGeneric() throws Exception{
		
		throw new Exception("olalala");		
		//return "ok";
	}
	@RequestMapping("/test/error/login")
	public String errorLogin() throws NotFoundException{
		
		throw new NotFoundException(ErrorCode.ERR2001_LOGIN_FAILURE,"olalala login gagal");		
		//return "ok";
	}
	
	@RequestMapping("/test/product")
	@ResponseBody
	public List<Product> getproduct(){
		return null;
	}
}
