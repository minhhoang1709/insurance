package com.ninelives.insurance.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ninelives.insurance.api.dto.UserFileDto;
import com.ninelives.insurance.api.exception.ApiException;

@Controller
public class ClaimController {
	
	//test
	int testnum;
	
	private static final Logger logger = LoggerFactory.getLogger(ClaimController.class);
			
	@RequestMapping(value="/orders/{orderId}/claims",
			method=RequestMethod.POST)	
	@ResponseBody
	public Map<String, String> claim(@PathVariable("orderId") String orderId, @RequestBody Map<String, String> requestMap){
		logger.debug("Terima /order/claim POST untuk order {} dan data {}", orderId, requestMap);
		requestMap.put("claimId", "xxxxxxx");
		
		return requestMap;
	}
//	@RequestMapping(value="/orders/{orderId}/claims",
//			method=RequestMethod.PUT)	
//	@ResponseBody
//	public Map<String, String> claimPut(@PathVariable("orderId") String orderId, @RequestBody Map<String, String> requestMap){
//		logger.debug("Terima /order/claim POST untuk order {} dan data {}", orderId, requestMap);
//		requestMap.put("claimId", "xxxxxxx");
//		
//		return requestMap;
//	}
//	
	//test, dummy
	@RequestMapping(value="/claimDocumentFiles",
			method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Integer> updateIdCardFile (@RequestAttribute ("authUserId") String authUserId, 
			@RequestParam("file") MultipartFile file) throws ApiException{

		Map<String, Integer> result = new HashMap<>();
		result.put("fileId", getNextSequence());
		return result;
	}
	
	//test
	public int getNextSequence(){
		testnum++;
		return testnum;
	}
}
