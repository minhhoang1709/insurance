package com.ninelives.insurance.api.controller;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

@Controller
public class PaymentController {
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
	
	@RequestMapping(value="/payment/charge", 
			method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String charge(HttpServletRequest request, 
			@RequestHeader(required=false) Map<String,String> headers, 
			@RequestParam(required=false) Map<String,String> requestParams, 
			@RequestBody(required=false) String requestBody){
		
		//TODO: Tidy the logging
		//TODO: Return 201 created
		if(logger.isDebugEnabled()){
			logger.info("---");
			logger.info("Terima callback {} {}", request.getMethod(), request.getRequestURI());
			logger.info("Body {} ", requestBody);

			if( headers!=null&&headers.size()>0 ){
				headers.forEach((k,v)->logger.info("Header : " + k + " | Value : " + v));
			}
			if( requestParams!=null&&requestParams.size()>0 ){
				requestParams.forEach((k,v)->logger.info("Param : " + k + " | Value : " + v));
			}
			
		}
		
		RestTemplate template = new RestTemplate();
		HttpHeaders restHeader = new HttpHeaders();
		restHeader.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		restHeader.setContentType(MediaType.APPLICATION_JSON);
		restHeader.set("Authorization", "Basic U0ItTWlkLXNlcnZlci1kcF9JVFlVY3hCMGlRMWh0T0xRXzJLWjM6");
		HttpEntity<String> entity = new HttpEntity<>(requestBody, restHeader);
		
		ResponseEntity<String> resp = null;
		resp = template.exchange("https://app.sandbox.midtrans.com/snap/v1/transactions", HttpMethod.POST, entity, String.class);
		
		String respBody = null;
		if(resp!=null){
			respBody = resp.getBody();
			String respHeader = resp.getHeaders().toString();
			String respStatus = resp.getStatusCode().toString();			
			logger.info("Call to restteamplate {} with result {}", entity.toString(), resp.toString());
			
		}else{
			logger.info("Call to restteamplate {} with result null", entity.toString());
		}
		
		return respBody;
	}
}
