package com.ninelives.insurance.api.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ninelives.insurance.api.exception.ApiException;
import com.ninelives.insurance.api.exception.ApiInternalServerErrorException;
import com.ninelives.insurance.api.service.OrderService;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.ref.ErrorCode;

@Controller
@RequestMapping("/api")
@Profile("!noaswata")
public class OrderDownloadController {
	private static final Logger logger = LoggerFactory.getLogger(OrderDownloadController.class);
	
	@Autowired OrderService orderService;
	
	@RequestMapping(value="/orders/{orderId}/policy",
			method=RequestMethod.GET)	
	@ResponseBody
	public void downloadPolicy(@RequestAttribute("authUserId") String authUserId,
			@PathVariable("orderId") String orderId,
			HttpServletResponse response) throws ApiException{
		
		logger.debug("GET download policy, userId <{}>, orderId: <{}>", authUserId, orderId);
		
		PolicyOrder order = orderService.fetchOrderForDownload(authUserId, orderId);
		
		RestTemplate restTemplate = new RestTemplate();

	    response.setStatus(HttpStatus.OK.value());
	    response.setContentType("application/pdf");
	    response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + (order.getPolicyNumber()==null?orderId:order.getPolicyNumber())+".pdf\"");

	    //order.getProviderDownloadUrl()
	    try {
			restTemplate.execute(
				order.getProviderDownloadUrl(),
			    HttpMethod.GET,
			    (ClientHttpRequest requestCallback) -> {},
			    responseExtractor -> {
			    	//responseExtractor.getHeaders().forEach((k,v) -> response.addHeader(k, responseExtractor.getHeaders().getFirst(k)));
//			    	if(responseExtractor.getHeaders()!=null){
//			    		response.setHeader(HttpHeaders.CONTENT_TYPE, responseExtractor.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE));
//			    		response.setHeader(HttpHeaders.CONTENT_LENGTH, responseExtractor.getHeaders().getFirst(HttpHeaders.Conte));
//			    	}
			    	
			        IOUtils.copy(responseExtractor.getBody(), response.getOutputStream());
			        return null;
			    });
		} catch (RestClientException e) {
			logger.error("Error on download policy from ASWATA", e);
			throw new ApiInternalServerErrorException(ErrorCode.ERR4303_DOWNLOAD_PROVIDER_ERROR, "Maaf permintaan Anda belum dapat dilayani, terjadi kesalahan pada sistem");
		}
		
	    return;
		//Resource file = storageService.loadAsResource("policy.pdf");
	    
//		return ResponseEntity.ok()
//				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + file.getFilename() + "\"")
//				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE).body(file);
				
	}
}
