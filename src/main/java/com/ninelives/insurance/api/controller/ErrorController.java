package com.ninelives.insurance.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.api.dto.ErrorDto;
import com.ninelives.insurance.api.ref.ErrorCode;

@Controller
public class ErrorController extends BasicErrorController{
	private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);
	
	public ErrorController(){
		super(new DefaultErrorAttributes(), new ErrorProperties());
	}	
	
	@ResponseBody
	@Override
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		Map<String, Object> body = getErrorAttributes(request,
				isIncludeStackTrace(request, MediaType.ALL));
		HttpStatus status = getStatus(request);
		
		String message = (String)body.get("error");
		if(StringUtils.isEmpty(message)){
			message = (String)body.get("message");
		}
		ErrorDto errorDto = new ErrorDto(status.value(), ErrorCode.ERR1001_GENERIC_ERROR, message);
		
		Map<String, Object> errorDtoResp = new HashMap<>();
		errorDtoResp.put("error", errorDto);		
		//body.forEach((k,v)->System.out.println("Entity : " + k + ", Item : " + v));
		
		return new ResponseEntity<Map<String, Object>>(errorDtoResp, status);
	}
	
}

