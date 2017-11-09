package com.ninelives.insurance.api.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.service.OrderService;

@Controller
public class OrderController {
	
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
	public List<OrderDto> getOrders( @RequestBody(required=false) Map<String, String> requestData, HttpServletResponse response){
		
		return orderService.fetchOrderListByUserId(501);
	}
	
	
}
