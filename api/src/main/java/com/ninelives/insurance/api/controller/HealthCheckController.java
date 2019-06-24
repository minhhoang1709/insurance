package com.ninelives.insurance.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/api")
public class HealthCheckController {
	@RequestMapping(value="/healthCheck", 
			method=RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public void defaultHealthCheck() {
		return;
	}
}
