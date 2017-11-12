package com.ninelives.insurance.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ninelives.insurance.api.exception.NotFoundException;
import com.ninelives.insurance.api.ref.ErrorCode;

@Controller
public class TestController {
	
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
}
