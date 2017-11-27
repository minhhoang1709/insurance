package com.ninelives.insurance.api.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ClaimController {
	
	private static final Logger logger = LoggerFactory.getLogger(ClaimController.class);
			
	@RequestMapping(value="/orders/{orderId}/claims",
			method=RequestMethod.POST)	
	@ResponseBody
	public Map<String, String> claim(@PathVariable("orderId") String orderId, @RequestBody Map<String, String> requestMap){
		logger.debug("Terima /order/claim POST untuk order {} dan data {}", orderId, requestMap);
		requestMap.put("claimId", "xxxxxxx");
		
		return requestMap;
	}
	@RequestMapping(value="/orders/{orderId}/claims",
			method=RequestMethod.PUT)	
	@ResponseBody
	public Map<String, String> claimPut(@PathVariable("orderId") String orderId, @RequestBody Map<String, String> requestMap){
		logger.debug("Terima /order/claim POST untuk order {} dan data {}", orderId, requestMap);
		requestMap.put("claimId", "xxxxxxx");
		
		return requestMap;
	}
}
