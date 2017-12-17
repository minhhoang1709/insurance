package com.ninelives.insurance.payment.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ninelives.insurance.payment.dto.MidtransNotificationDto;
import com.ninelives.insurance.payment.exception.PaymentNotificationException;
import com.ninelives.insurance.payment.service.MidtransPaymentNotificationService;

@Controller
public class MidtransPaymentController {
	private static final Logger logger = LoggerFactory.getLogger(MidtransPaymentController.class);
	
	@Autowired MidtransPaymentNotificationService paymentNotificationService;
			
	@RequestMapping(value="/payment/midtrans/notification", 
			method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void notification(HttpServletRequest request, 
			@RequestHeader(required=false) Map<String,String> headers, 
			@RequestParam(required=false) Map<String,String> requestParams, 
			@RequestBody MidtransNotificationDto notifDto) throws PaymentNotificationException{
		logger.info("---");
		logger.info("Terima callback {} {}", request.getMethod(), request.getRequestURI());
		logger.info("Body {} ", notifDto);
		if( headers!=null&&headers.size()>0 ){
			headers.forEach((k,v)->logger.info("Header : " + k + " | Value : " + v));
		}
		if( requestParams!=null&&requestParams.size()>0 ){
			requestParams.forEach((k,v)->logger.info("Param : " + k + " | Value : " + v));
		}
		
		paymentNotificationService.processNotification(notifDto);
	}
	
	@RequestMapping(value="/payment/midtrans/finish")
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
	@RequestMapping(value="/payment/midtrans/unfinish")
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
	@RequestMapping(value="/payment/midtrans/error")
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
