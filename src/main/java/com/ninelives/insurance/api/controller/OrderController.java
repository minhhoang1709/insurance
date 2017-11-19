package com.ninelives.insurance.api.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.SubmitOrderDto;
import com.ninelives.insurance.api.service.OrderService;
import com.ninelives.insurance.api.service.StorageService;

@Controller
public class OrderController {
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
			
	@Autowired OrderService orderService;
	@Autowired StorageService storageService;
	
	@RequestMapping(value="/orders/{orderId}",
			method=RequestMethod.GET)	
	@ResponseBody
	public OrderDto getOrderWithId( @PathVariable("orderId") String orderId, @RequestBody(required=false) Map<String, String> requestData, HttpServletResponse response){
		OrderDto orderDto = orderService.fetchOrderByOrderId(orderId);
		if(orderDto==null){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return orderDto;
	}
	
	@RequestMapping(value="/orders",
			method=RequestMethod.GET)	
	@ResponseBody
	public List<OrderDto> getOrders(@RequestAttribute("authUserId") String authUserId,
			@RequestBody(required=false) Map<String, String> requestData, 
			@RequestParam(value="filter", required=false) String filter, 
			HttpServletResponse response){
		
		logger.debug("GET getOrders userid is {} with filter", authUserId, filter);
		
		return orderService.fetchOrderListByUserId("b4bc66a0f04a4a628b45d8604b3426e1");
	}
	
	@RequestMapping(value="/orders",
			method=RequestMethod.POST)	
	@ResponseBody
	public OrderDto submitOrder(@RequestAttribute("authUserId") String authUserId,
			@RequestBody(required=false) SubmitOrderDto submitOrder, 
			HttpServletResponse response){
		
		logger.debug("POST submitOrder with request {}", submitOrder);
		
		return orderService.fetchOrderByOrderId("02d40b53-c918-4770-b324-df7d1b0230dc");
	}
	
	@RequestMapping(value="/orders/{orderId}/beneficiary",
			method=RequestMethod.PUT)	
	@ResponseBody
	public Map<String, String> updateBeneficiary(@RequestAttribute("authUserId") String authUserId,
			@RequestBody(required=false) Map<String, String> requestData, 
			HttpServletResponse response){
		
		logger.debug("PUT beneficiary with request {}", requestData);
		
		return requestData;
	}
	
	@RequestMapping(value="/orders/{orderId}/policy",
			method=RequestMethod.GET)	
	@ResponseBody
	public ResponseEntity<Resource> downloadPolicy(@RequestAttribute("authUserId") String authUserId,
			@RequestBody(required=false) Map<String, String> requestData, 
			HttpServletResponse response){
		
		logger.debug("GET policy with request {}", requestData);
		
		Resource file = storageService.loadAsResource("policy.pdf");
	    
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + file.getFilename() + "\"")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE).body(file);
				
	}
		
}
