package com.ninelives.insurance.api.controller;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.OrderFilterDto;
import com.ninelives.insurance.api.dto.SubmitOrderDto;
import com.ninelives.insurance.api.dto.UserDto;
import com.ninelives.insurance.api.exception.ApiBadRequestException;
import com.ninelives.insurance.api.exception.ApiException;
import com.ninelives.insurance.api.exception.ApiNotFoundException;
import com.ninelives.insurance.api.model.PolicyOrder;
import com.ninelives.insurance.api.model.Product;
import com.ninelives.insurance.api.model.User;
import com.ninelives.insurance.api.mybatis.mapper.PolicyOrderMapper;
import com.ninelives.insurance.api.mybatis.mapper.ProductMapper;
import com.ninelives.insurance.api.mybatis.mapper.UserMapper;
import com.ninelives.insurance.api.ref.ErrorCode;
import com.ninelives.insurance.api.service.OrderService;
import com.ninelives.insurance.api.service.ProductService;
import com.ninelives.insurance.api.service.UserService;
import com.ninelives.insurance.api.util.GsonUtil;

@Controller
public class TestController {
	
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@Autowired ProductService productService;
	@Autowired OrderService orderService;
	@Autowired ProductMapper productMapper;
	@Autowired PolicyOrderMapper policyOrderMapper;
	
	@Autowired UserService userService;
	@Autowired UserMapper userMapper;
	
	@Value("${ninelives.order.list-limmit:50}")
	int defaultFilterLimit;
	
	@Value("${ninelives.order.list-offset:0}")
	int defaultFilterOffset;

	
	@RequestMapping("/test/error/generic")
	public String errorGeneric() throws Exception{
		
		throw new Exception("olalala");		
		//return "ok";
	}
	@RequestMapping("/test/error/login")
	public String errorLogin() throws ApiNotFoundException{
		
		throw new ApiNotFoundException(ErrorCode.ERR2001_LOGIN_FAILURE,"olalala login gagal");		
		//return "ok";
	}
	
	@RequestMapping("/test/product")
	@ResponseBody
	public List<Product> getproduct(){
		return null;
	}
	
//	@RequestMapping("/test/product/bylist")
//	@ResponseBody
//	public List<Product> getproductByList(){	
//		//List<String> productIds = Arrays.asList("P101004102","P101003102","P101006102");
//		List<String> productIds = Arrays.asList("P10");
//		List<Product> products = productService.fetchProductByProductIds(productIds);
//		if(products==null){
//			System.out.println("NULL productlist");
//		}
//		return products;
//	}
	
//	@RequestMapping(value="/test/order", method=RequestMethod.POST)
//	@ResponseBody
//	public OrderDto order(@RequestAttribute("authUserId") String authUserId, 
//			@RequestBody(required=false) SubmitOrderDto submitOrderDto) throws ApiException{	
//		//List<String> productIds = Arrays.asList("P101004102","P101003102","P101006102");
//		return orderService.submitOrder(authUserId, submitOrderDto);
//	}
//	
	@RequestMapping(value="/test/fullorder", method=RequestMethod.POST)
	@ResponseBody
	public OrderDto order(@RequestAttribute("authUserId") String authUserId, 
			@RequestBody(required=false) OrderDto orderDto) throws ApiException{	
		//List<String> productIds = Arrays.asList("P101004102","P101003102","P101006102");
		return orderService.submitOrder(authUserId, orderDto);
	}
	
	@RequestMapping(value="/test/testfetchorder", method=RequestMethod.GET)
	@ResponseBody
	public List<PolicyOrder> getTestFetchOrder(@RequestAttribute("authUserId") String authUserId, 
			@RequestParam(value="filter",required=false) String filter) throws ApiException{
		//List<String> productIds = Arrays.asList("P101004102","P101003102","P101006102");
		
		//return orderService.submitOrder(authUserId, submitOrder);
		//policyOrderMapper.selectByUserId(authUserId, 100, 0);
		//orderFilter = new OrderFilterDto();
		//orderFilter.setLimit(limit);
		
		OrderFilterDto orderFilter = GsonUtil.gson.fromJson(filter, OrderFilterDto.class);
		
		return orderService.tesFetch(authUserId, orderFilter); 
	}
	
	@RequestMapping(value="/test/order", method=RequestMethod.GET)
	@ResponseBody
	public List<OrderDto> getOrder(@RequestAttribute("authUserId") String authUserId, 
			@RequestParam(value="filter",required=false) String filter) throws ApiException{
		//List<String> productIds = Arrays.asList("P101004102","P101003102","P101006102");
		
		//return orderService.submitOrder(authUserId, submitOrder);
		//policyOrderMapper.selectByUserId(authUserId, 100, 0);
		//orderFilter = new OrderFilterDto();
		//orderFilter.setLimit(limit);
		
		OrderFilterDto orderFilter = GsonUtil.gson.fromJson(filter, OrderFilterDto.class);
		
		return orderService.fetchOrderDtos(authUserId, orderFilter);
	}
	
	@RequestMapping(value="/test/order/{orderId}", method=RequestMethod.GET)
	@ResponseBody
	public PolicyOrder getOrderDetail(@RequestAttribute("authUserId") String authUserId,
			@PathVariable("orderId") String orderId) throws ApiException{
		//List<String> productIds = Arrays.asList("P101004102","P101003102","P101006102");
		
		//return orderService.submitOrder(authUserId, submitOrder);
		return policyOrderMapper.selectByUserIdAndOrderId(authUserId, orderId);
	}
	
	@RequestMapping("/test/conflictorder")
	@ResponseBody
	public List conflictOrder(@RequestAttribute("authUserId") String authUserId, @RequestBody(required=false) OrderDto submitOrder) throws ApiException{	
		//List<String> productIds = Arrays.asList("P101004102","P101003102","P101006102");
		return orderService.testConflict(authUserId, submitOrder);
	}
	
	@RequestMapping(value="/test/user/{userId}", method=RequestMethod.GET)
	@ResponseBody
	public User getUser(@PathVariable("userId") String userId){
		return userMapper.selectByUserId(userId);
	}
	
	@RequestMapping(value="/test/users/{userId}",
			method={ RequestMethod.PATCH, RequestMethod.PUT })
	@ResponseBody
	public UserDto updateUsers (@RequestAttribute ("authUserId") String authUserId, @PathVariable("userId") String userId, @RequestBody UserDto usersDto){
		logger.debug("Terima /users PUT untuk authuser {} and user {}", authUserId, userId);
		logger.debug("put data: {}", usersDto);
		logger.debug("---");
		UserDto result = userService.getUserDto(userId);
		if(result==null){
			result = usersDto;
		}
		return result;
	}
}
