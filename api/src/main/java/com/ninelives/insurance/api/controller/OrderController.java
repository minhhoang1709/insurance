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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.FilterDto;
import com.ninelives.insurance.api.dto.PolicyOrderBeneficiaryDto;
import com.ninelives.insurance.api.exception.ApiException;
import com.ninelives.insurance.api.exception.ApiNotFoundException;
import com.ninelives.insurance.api.provider.storage.StorageException;
import com.ninelives.insurance.api.provider.storage.StorageProvider;
import com.ninelives.insurance.api.service.OrderService;
import com.ninelives.insurance.api.util.GsonUtil;
import com.ninelives.insurance.model.PolicyOrderBeneficiary;
import com.ninelives.insurance.ref.ErrorCode;

@Controller
@RequestMapping("/api")
public class OrderController {
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
			
	@Autowired OrderService orderService;
	@Autowired StorageProvider storageService;
	
	@RequestMapping(value="/orders/{orderId}",
			method=RequestMethod.GET)	
	@ResponseBody
	public OrderDto getOrderByOrderId(@RequestAttribute("authUserId") String authUserId, 
			@PathVariable("orderId") String orderId) throws ApiNotFoundException{
		
		logger.debug("GET getOrders userid is {} with orderid {}", authUserId, orderId);
		
		OrderDto orderDto = orderService.fetchOrderDtoByOrderId(authUserId, orderId);
		if(orderDto==null){
			throw new ApiNotFoundException(ErrorCode.ERR5001_ORDER_NOT_FOUND,"Transaksi tidak ditemukan");
		}
		
		return orderDto;
	}
	
	@RequestMapping(value="/orders",
			method=RequestMethod.GET)	
	@ResponseBody
	public List<OrderDto> getOrders(@RequestAttribute("authUserId") String authUserId,
			@RequestParam(value="filter",required=false) String filter){
		
		logger.debug("GET getOrders userid is {} with filter {}", authUserId, filter);
		
		FilterDto orderFilter = GsonUtil.gson.fromJson(filter, FilterDto.class);
		
		return orderService.fetchOrderDtos(authUserId, orderFilter);
	}
	
	@RequestMapping(value="/orders",
			method=RequestMethod.POST)	
	@ResponseBody
	public OrderDto submitOrder(@RequestAttribute("authUserId") String authUserId,
			@RequestParam(value="test", defaultValue="false") boolean isValidateOnly,
			@RequestBody(required=false) OrderDto submitOrder, 
			HttpServletResponse response) throws ApiException{
		
		logger.debug("POST submitOrder with request {} and validate-only {}", submitOrder, isValidateOnly);
		
		return orderService.submitOrder(authUserId, submitOrder, isValidateOnly);
	}
	
	@RequestMapping(value="/orders/{orderId}/beneficiary",
			method={ RequestMethod.POST, RequestMethod.PUT })	
	@ResponseBody
	public PolicyOrderBeneficiaryDto updateBeneficiary(@RequestAttribute("authUserId") String authUserId,
			@PathVariable("orderId") String orderId,
			@RequestBody PolicyOrderBeneficiaryDto beneficiaryDto, 
			HttpServletResponse response) throws ApiException{
		
		logger.debug("PUT beneficiary userid is {} with order {} beneficiary {}", authUserId, orderId, beneficiaryDto);
		
		return orderService.updateBeneficiary(authUserId, orderId, beneficiaryDto);
			
	}
	
	@RequestMapping(value="/orders/{orderId}/policy",
			method=RequestMethod.GET)	
	@ResponseBody
	public ResponseEntity<Resource> downloadPolicy(@RequestAttribute("authUserId") String authUserId,
			@RequestBody(required=false) Map<String, String> requestData, 
			HttpServletResponse response) throws StorageException{
		
		logger.debug("GET policy with request {}", requestData);
		
		Resource file = storageService.loadAsResource("policy.pdf");
	    
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + file.getFilename() + "\"")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE).body(file);
				
	}
		
}
