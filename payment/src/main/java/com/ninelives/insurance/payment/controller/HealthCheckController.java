package com.ninelives.insurance.payment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class HealthCheckController {
	@RequestMapping(value="/payment/healthCheck", 
			method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public void defaultHealthCheck() {
		return;
	}
}
