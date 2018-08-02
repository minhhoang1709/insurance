package com.ninelives.insurance.apigateway.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.apigateway.service.ConfigService;

@Controller
@RequestMapping("/api")
public class AppClientController {
	
	@Autowired ConfigService configService;
	
	@RequestMapping(value="/configs",
			method=RequestMethod.GET)
	@ResponseBody
	public Map<String, String> getConfigs(){		
		return configService.fetchClientAppConfigMapWithStatusActive();
	}
}
