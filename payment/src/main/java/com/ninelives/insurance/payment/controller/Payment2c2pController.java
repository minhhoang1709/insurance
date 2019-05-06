package com.ninelives.insurance.payment.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ninelives.insurance.payment.exception.PaymentNotificationException;
import com.ninelives.insurance.payment.service.Payment2c2pNotificationService;

@Controller
public class Payment2c2pController {
	private static final Logger logger = LoggerFactory.getLogger(Payment2c2pController.class);
	
	@Autowired Payment2c2pNotificationService paymentNotificationService;
			
	@RequestMapping(value="/payment/2c2p/notification", method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void notification(HttpServletRequest request, 
			HttpServletResponse response,
			Model model) 
			throws PaymentNotificationException{
		
		paymentNotificationService.processNotification(request, response);
	
	}
	

}
