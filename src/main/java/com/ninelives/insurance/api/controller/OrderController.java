package com.ninelives.insurance.api.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.service.OrderService;

@Controller
@SessionAttributes("authUserId")
public class OrderController {
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
			
	@Autowired OrderService orderService;
	
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
	public List<OrderDto> getOrders(@ModelAttribute("authUserId") String authUserId,
			@RequestBody(required=false) Map<String, String> requestData, 
			HttpServletResponse response){
		
		logger.debug("userid is {}", authUserId);
		
		return orderService.fetchOrderListByUserId("b4bc66a0f04a4a628b45d8604b3426e1");
	}
	
	
}
