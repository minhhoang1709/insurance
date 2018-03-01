package com.ninelives.insurance.api.dev;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.api.exception.ApiException;
import com.ninelives.insurance.core.provider.storage.StorageException;
import com.ninelives.insurance.core.provider.storage.StorageProvider;

@Controller
@RequestMapping("/api")
@Profile("noaswata")
public class TestOrderDownloadController {
	private static final Logger logger = LoggerFactory.getLogger(TestOrderDownloadController.class);
	
	@Autowired StorageProvider storageService;
	
	@RequestMapping(value="/orders/{orderId}/policy",
			method=RequestMethod.GET)	
	@ResponseBody
	public ResponseEntity<Resource> downloadPolicyForTestNoAswata(@RequestAttribute("authUserId") String authUserId,
			@PathVariable("orderId") String orderId,
			HttpServletResponse response) throws ApiException, StorageException{
		
		logger.debug("GET download policy no aswata, userId <{}>, orderId: <{}>", authUserId, orderId);
		
		Resource file = storageService.loadAsResource("policy.pdf");
	    
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + file.getFilename() + "\"")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE).body(file);
				
	}
}
