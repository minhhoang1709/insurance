package com.ninelives.insurance.api.controller;

import java.util.Map;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.api.service.ConfigService;

@Controller
@Produces({ MediaType.APPLICATION_JSON })
public class AppClientController {
	
	@Autowired ConfigService configService;
	
	@RequestMapping(value="/configs",
			method=RequestMethod.GET)
	@ResponseBody
	public Map<String, String> getConfigs(){		
		return configService.fetchClientAppConfigMapWithStatusActive();
	}
}
