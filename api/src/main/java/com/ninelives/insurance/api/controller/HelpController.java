package com.ninelives.insurance.api.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.exception.AppInternalServerErrorException;
import com.ninelives.insurance.core.provider.storage.StorageProvider;
import com.ninelives.insurance.ref.ErrorCode;

@Controller
@RequestMapping("/api")
public class HelpController {
	private static final Logger logger = LoggerFactory.getLogger(HelpController.class);
	
	@Autowired NinelivesConfigProperties config;
	
	@Autowired StorageProvider storageProvider;
	
	@RequestMapping(value="/help/policyStandard",
			method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Resource> downloadPolicyStandard(@RequestAttribute("authUserId") String authUserId, HttpServletResponse response) throws AppException{
		
		try {
			Resource file = storageProvider.loadAsResource(config.getHelpPolicyStandardFilePath());
			
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + file.getFilename() + "\"")
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE).body(file);

		} catch (Exception e) {
			logger.error("Error on loading policy standar", e);
			throw new AppInternalServerErrorException(ErrorCode.ERR1002_STORAGE_ERROR, "Maaf, terjadi kesalahan pada sistem.");
		}	    

	}
	
	@RequestMapping(value="/help/travelPolicyStandard",
			method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Resource> downloadTravelPolicyStandard(@RequestAttribute("authUserId") String authUserId, HttpServletResponse response) throws AppException{
		
		try {
			Resource file = storageProvider.loadAsResource(config.getHelpTravelPolicyStandardFilePath());
			
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + file.getFilename() + "\"")
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE).body(file);

		} catch (Exception e) {
			logger.error("Error on loading policy standar", e);
			throw new AppInternalServerErrorException(ErrorCode.ERR1002_STORAGE_ERROR, "Maaf, terjadi kesalahan pada sistem.");
		}	    

	}
	
	@RequestMapping(value="/help/selfiePolicyStandard",
			method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Resource> downloadSelfiePolicyStandard(@RequestAttribute("authUserId") String authUserId, HttpServletResponse response) throws AppException{
		
		try {
			Resource file = storageProvider.loadAsResource(config.getHelpSelfiePolicyStandardFilePath());
			
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + file.getFilename() + "\"")
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE).body(file);

		} catch (Exception e) {
			logger.error("Error on loading policy standar", e);
			throw new AppInternalServerErrorException(ErrorCode.ERR1002_STORAGE_ERROR, "Maaf, terjadi kesalahan pada sistem.");
		}	    

	}
}
