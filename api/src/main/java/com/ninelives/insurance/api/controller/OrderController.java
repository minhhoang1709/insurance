package com.ninelives.insurance.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ninelives.insurance.api.dto.FilterDto;
import com.ninelives.insurance.api.dto.OrderDocumentDto;
import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.PolicyOrderBeneficiaryDto;
import com.ninelives.insurance.api.dto.UserFileDto;
import com.ninelives.insurance.api.service.ApiFileUploadService;
import com.ninelives.insurance.api.service.ApiOrderService;
import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.exception.AppNotFoundException;
import com.ninelives.insurance.core.util.GsonUtil;
import com.ninelives.insurance.ref.ErrorCode;

@Controller
@RequestMapping("/api")
public class OrderController {
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
			
	@Autowired ApiFileUploadService fileUploadService;
	@Autowired ApiOrderService apiOrderService;
	
	@RequestMapping(value="/orders/{orderId}",
			method=RequestMethod.GET)	
	@ResponseBody
	public OrderDto getOrderByOrderId(@RequestAttribute("authUserId") String authUserId, 
			@PathVariable("orderId") String orderId) throws AppNotFoundException{
		
		logger.debug("GET getOrders userid is {} with orderid {}", authUserId, orderId);
		
		OrderDto orderDto = apiOrderService.fetchOrderDtoByOrderId(authUserId, orderId);
		if(orderDto==null){
			throw new AppNotFoundException(ErrorCode.ERR5001_ORDER_NOT_FOUND,"Transaksi tidak ditemukan");
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
		
		return apiOrderService.fetchOrderDtos(authUserId, orderFilter);
	}
	
	@RequestMapping(value="/orders",
			method=RequestMethod.POST)	
	@ResponseBody
	public OrderDto submitOrder(@RequestAttribute("authUserId") String authUserId,
			@RequestParam(value="test", defaultValue="false") boolean isValidateOnly,
			@RequestBody(required=false) @Valid OrderDto submitOrder, 
			HttpServletResponse response) throws AppException{
		
		logger.debug("POST submitOrder with request {} and validate-only {}", submitOrder, isValidateOnly);
		
		return apiOrderService.submitOrder(authUserId, submitOrder, isValidateOnly);
	}
	
	@RequestMapping(value="/orders/{orderId}",
			method=RequestMethod.DELETE)	
	@ResponseBody
	public void hideOrder(@RequestAttribute("authUserId") String authUserId, 
			@PathVariable("orderId") String orderId) throws AppException{
		
		logger.debug("Hide orders userid is {} with orderid {}", authUserId, orderId);
		
		apiOrderService.deleteOrder(authUserId, orderId);
	}
	
	@RequestMapping(value="/orders/{orderId}/beneficiary",
			method={ RequestMethod.POST, RequestMethod.PUT })	
	@ResponseBody
	public PolicyOrderBeneficiaryDto updateBeneficiary(@RequestAttribute("authUserId") String authUserId,
			@PathVariable("orderId") String orderId,
			@RequestBody @Valid PolicyOrderBeneficiaryDto beneficiaryDto, 
			HttpServletResponse response) throws AppException{
		
		logger.debug("PUT beneficiary userid is {} with order {} beneficiary {}", authUserId, orderId, beneficiaryDto);
		
		return apiOrderService.updateBeneficiary(authUserId, orderId, beneficiaryDto);
			
	}
	
	@RequestMapping(value="/orderDocuments",
			method=RequestMethod.POST)
	@ResponseBody
	public List<OrderDocumentDto> listRequiredOrderDocumentDtos (@RequestAttribute ("authUserId") String authUserId, 
			@RequestParam(value="test", defaultValue="false") boolean isValidateOnly,
			@RequestBody(required=false) @Valid OrderDto orderDto) throws AppException{
		if(isValidateOnly){
			return apiOrderService.requiredOrderDocumentDtos(authUserId, orderDto);
		}else{
			throw new AppBadRequestException(ErrorCode.ERR1001_GENERIC_ERROR,"Parameter not supported");
		}
	}
	
	@RequestMapping(value="/orderDocumentFiles",
			method=RequestMethod.POST)
	@ResponseBody
	public UserFileDto uploadClaimDocumentFile (@RequestAttribute ("authUserId") String authUserId, 
			@RequestParam("file") MultipartFile file) throws AppException{
		return fileUploadService.saveTemp(authUserId, file);
	}

}
