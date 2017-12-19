package com.ninelives.insurance.api.controller;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ninelives.insurance.api.dto.ChargeDto;
import com.ninelives.insurance.api.dto.ChargeResponseDto;
import com.ninelives.insurance.api.exception.ApiBadRequestException;
import com.ninelives.insurance.api.exception.ApiException;
import com.ninelives.insurance.api.exception.ApiNotAuthorizedException;
import com.ninelives.insurance.api.model.ApiSessionData;
import com.ninelives.insurance.api.service.AuthService;
import com.ninelives.insurance.api.service.PaymentService;
import com.ninelives.insurance.ref.ErrorCode;

@Controller
public class TestPaymentController {
	private static final Logger logger = LoggerFactory.getLogger(TestPaymentController.class);
	
	@Autowired AuthService authService;
	
	@Autowired PaymentService paymentService;
	
//	@RequestMapping(value="/test/payment/charge", 
//			method=RequestMethod.POST)
//	@ResponseStatus(value = HttpStatus.OK)
//	@ResponseBody
//	public String charge(HttpServletRequest request, 
//			@RequestHeader(required=false) Map<String,String> headers, 
//			@RequestParam(required=false) Map<String,String> requestParams, 
//			@RequestBody(required=false) String requestBody){
//		logger.info("---");
//		logger.info("Terima callback {} {}", request.getMethod(), request.getRequestURI());
//		logger.info("Body {} ", requestBody);
//		if( headers!=null&&headers.size()>0 ){
//			headers.forEach((k,v)->logger.info("Header : " + k + " | Value : " + v));
//		}
//		if( requestParams!=null&&requestParams.size()>0 ){
//			requestParams.forEach((k,v)->logger.info("Param : " + k + " | Value : " + v));
//		}
//		
//		RestTemplate template = new RestTemplate();
//		HttpHeaders restHeader = new HttpHeaders();
//		restHeader.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//		restHeader.setContentType(MediaType.APPLICATION_JSON);
//		restHeader.set("Authorization", "Basic U0ItTWlkLXNlcnZlci1kcF9JVFlVY3hCMGlRMWh0T0xRXzJLWjM6");
//		HttpEntity<String> entity = new HttpEntity<>(requestBody, restHeader);
//		
//		ResponseEntity<String> resp = null;
//		resp = template.exchange("https://app.sandbox.midtrans.com/snap/v1/transactions", HttpMethod.POST, entity, String.class);
//		
//		String respBody = null;
//		if(resp!=null){
//			respBody = resp.getBody();
//			String respHeader = resp.getHeaders().toString();
//			String respStatus = resp.getStatusCode().toString();			
//			logger.info("Call to restteamplate {} with result {}", entity.toString(), resp.toString());
//			
//		}else{
//			logger.info("Call to restteamplate {} with result null", entity.toString());
//		}
//		
//		return respBody;
//	}
	
//	@RequestMapping(value="/charge", 
//			method=RequestMethod.POST)
//	@ResponseBody
//	public ChargeResponseDto charge(HttpServletRequest request, 
//			HttpServletResponse response, 
//			@RequestHeader(required=false) Map<String,String> headers, 
//			@RequestParam(required=false) Map<String,String> requestParams, 
//			@RequestBody(required=true) ChargeDto chargeDto) throws ApiException{
//		logger.info("---");
//		logger.info("Terima callback {} {}", request.getMethod(), request.getRequestURI());
//		logger.info("Body {} ", chargeDto);
//		if( headers!=null&&headers.size()>0 ){
//			headers.forEach((k,v)->logger.info("Header : " + k + " | Value : " + v));
//		}
//		if( requestParams!=null&&requestParams.size()>0 ){
//			requestParams.forEach((k,v)->logger.info("Param : " + k + " | Value : " + v));
//		}
//		
//		String tokenId = chargeDto.getAuthToken();
//				
//		ApiSessionData sessionData;
//		try {
//			sessionData = authService.validateAuthToken(tokenId);
//		} catch (ApiNotAuthorizedException e) {
//			throw new ApiBadRequestException(ErrorCode.ERR2002_NOT_AUTHORIZED, "Authentication code not valid");
//		}
//		
//		chargeDto.setAuthToken(null);//dont forward authtoken to midtrans
//		
//		ChargeResponseDto responseDto = paymentService.charge(sessionData.getUserId(), chargeDto);
////		if(responseDto.getHttpStatus()!=null){
////			response.setStatus(responseDto.getHttpStatus().value());
////		}
////		
//		//TODO: return paymentTokenDto atau String as it is from midtrans
//		
//		return responseDto;
//	}
	
//	@RequestMapping(value="/charge", 
//			method=RequestMethod.POST)
//	@ResponseStatus(value = HttpStatus.OK)
//	@ResponseBody
//	public String charge2(HttpServletRequest request, 
//			@RequestHeader(required=false) Map<String,String> headers, 
//			@RequestParam(required=false) Map<String,String> requestParams, 
//			@RequestBody(required=false) String requestBody){
//		logger.info("---");
//		logger.info("Terima callback {} {}", request.getMethod(), request.getRequestURI());
//		logger.info("Body {} ", requestBody);
//		if( headers!=null&&headers.size()>0 ){
//			headers.forEach((k,v)->logger.info("Header : " + k + " | Value : " + v));
//		}
//		if( requestParams!=null&&requestParams.size()>0 ){
//			requestParams.forEach((k,v)->logger.info("Param : " + k + " | Value : " + v));
//		}
//		
//		RestTemplate template = new RestTemplate();
//		HttpHeaders restHeader = new HttpHeaders();
//		restHeader.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//		restHeader.setContentType(MediaType.APPLICATION_JSON);
//		restHeader.set("Authorization", "Basic U0ItTWlkLXNlcnZlci1kcF9JVFlVY3hCMGlRMWh0T0xRXzJLWjM6");
//		HttpEntity<String> entity = new HttpEntity<>(requestBody, restHeader);
//		
//		ResponseEntity<String> resp = null;
//		resp = template.exchange("https://app.sandbox.midtrans.com/snap/v1/transactions", HttpMethod.POST, entity, String.class);
//		
//		String respBody = null;
//		if(resp!=null){
//			respBody = resp.getBody();
//			String respHeader = resp.getHeaders().toString();
//			String respStatus = resp.getStatusCode().toString();			
//			logger.info("Call to restteamplate {} with result {}", entity.toString(), resp.toString());
//			
//		}else{
//			logger.info("Call to restteamplate {} with result null", entity.toString());
//		}
//		
//		return respBody;
//	}
//	
	@RequestMapping(value="/payment/notification", 
			method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void notification(HttpServletRequest request, 
			@RequestHeader(required=false) Map<String,String> headers, 
			@RequestParam(required=false) Map<String,String> requestParams, 
			@RequestBody(required=false) String requestBody){
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
	
	@RequestMapping(value="/payment/finish")
	@ResponseStatus(value = HttpStatus.OK)
	public void finish(HttpServletRequest request, 
			@RequestHeader(required=false) Map<String,String> headers, 
			@RequestParam(required=false) Map<String,String> requestParams, 
			@RequestBody(required=false) String requestBody){
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
	@RequestMapping(value="/payment/unfinish")
	@ResponseStatus(value = HttpStatus.OK)
	public void unfinish(HttpServletRequest request,
			@RequestHeader(required=false) Map<String,String> headers, 
			@RequestParam(required=false) Map<String,String> requestParams, 
			@RequestBody(required=false) String requestBody){
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
	@RequestMapping(value="/payment/error")
	@ResponseStatus(value = HttpStatus.OK)
	public void error(HttpServletRequest request,
			@RequestHeader(required=false) Map<String,String> headers, 
			@RequestParam(required=false) Map<String,String> requestParams, 
			@RequestBody(required=false) String requestBody){
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

}
