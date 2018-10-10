package com.ninelives.insurance.api.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.ProductDto;
import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.service.InsuranceService;
import com.ninelives.insurance.core.service.OrderService;
import com.ninelives.insurance.core.service.ProductService;
import com.ninelives.insurance.core.service.UserService;
import com.ninelives.insurance.core.trx.PolicyOrderTrxService;
import com.ninelives.insurance.model.Coverage;
import com.ninelives.insurance.model.Period;
import com.ninelives.insurance.model.Product;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.Gender;
import com.ninelives.insurance.ref.PeriodUnit;

@RunWith(SpringRunner.class)
public class ApiOrderServiceRegisterTest {
	
	@Test(expected=AppException.class)
	public void testExceptionEmptyParameter() throws AppException{
		ApiOrderService service = new ApiOrderService();
		OrderDto orderDto = new OrderDto();
		
		try{
			service.registerOrder("testId", orderDto, true);
			
		}catch(AppException e){
			assertThat(e.getCode(), is(ErrorCode.ERR4000_ORDER_INVALID));
			throw new AppException(ErrorCode.ERR1001_GENERIC_ERROR,"");
		}
	}
	
	@Test
	public void testTravelPeriodStartDate(){	
		int policyStartDatePeriodParam1 = 1;
		int policyStartDatePeriodParam2 = 0;
		int configTravelMinPolicyStartDatePeriod = 2;
		
		NinelivesConfigProperties config = new NinelivesConfigProperties();
		config.getOrder().setPolicyStartDatePeriod(365);
		config.getOrder().setTravelMinPolicyStartDatePeriod(configTravelMinPolicyStartDatePeriod);
		
		ApiOrderService service = new ApiOrderService();
		service.config = config;	
				
		// Param start
		String prodId = "P102006204001";
		String covCatId = "102";
		
		OrderDto orderDto = new OrderDto();
		orderDto.setPolicyStartDate(LocalDateTime.now().plusDays(policyStartDatePeriodParam1));
		
		ProductDto prodDto = new ProductDto();
		prodDto.setProductId(prodId);		
		List<ProductDto> products = new ArrayList<ProductDto>();
		products.add(prodDto);
		orderDto.setProducts(products);

		orderDto.setTotalPremi(5000);
		// Param end---
		
		Product prod = new Product();
		prod.setProductId(prodId);
		prod.setCoverage(new Coverage());
		prod.getCoverage().setCoverageCategoryId(covCatId);
		prod.setPeriodId("204");
		
		service.productService = Mockito.mock(ProductService.class);
		when(service.productService.fetchProductByProductId(prodId)).thenReturn(prod);		

		AppException exp = null;
		try{
			service.registerOrder("testId", orderDto, true);			
		}catch(AppException e){
			exp = e;
		}
		assertNotNull(exp);
		assertThat(exp.getCode(), is(ErrorCode.ERR4026_ORDER_TRAVEL_STARTDATE_INVALID));

		
		// Param start
		OrderDto orderDto2 = new OrderDto();
		orderDto2.setPolicyStartDate(LocalDateTime.now().plusDays(policyStartDatePeriodParam2));		
		orderDto2.setProducts(products);
		orderDto2.setTotalPremi(5000);
		// Param end---
		
		exp = null;
		try{
			service.registerOrder("testId", orderDto2, true);			
		}catch(AppException e){
			exp = e;
		}
		assertNotNull(exp);
		assertThat(exp.getCode(), is(ErrorCode.ERR4026_ORDER_TRAVEL_STARTDATE_INVALID));
		
	}
	
	@Test
	public void testTravelValidateOnlyOrder(){
		int policyStartDatePeriod = 3;
		int configTravelMinPolicyStartDatePeriod = 2;
				
		NinelivesConfigProperties config = new NinelivesConfigProperties();
		config.getOrder().setPolicyStartDatePeriod(365);
		config.getOrder().setTravelMinPolicyStartDatePeriod(configTravelMinPolicyStartDatePeriod);
		
		ApiOrderService service = new ApiOrderService();
		service.config = config;	
				
		// Param start
		String prodId = "P102006204001";
		String covCatId = "102";
		
		OrderDto orderDto = new OrderDto();
		orderDto.setPolicyStartDate(LocalDateTime.now().plusDays(policyStartDatePeriod));
		orderDto.setPolicyEndDate(LocalDateTime.now().plusDays(10));
		
		ProductDto prodDto = new ProductDto();
		prodDto.setProductId(prodId);
		List<ProductDto> products = new ArrayList<ProductDto>();
		products.add(prodDto);
		orderDto.setProducts(products);

		orderDto.setTotalPremi(5000);
		// Param end---
		
		Product prod = new Product();
		prod.setProductId(prodId);
		prod.setCoverage(new Coverage());
		prod.getCoverage().setCoverageCategoryId(covCatId);
		prod.getCoverage().setHasBeneficiary(false);
		prod.setPeriodId("204");
		prod.setPeriod(new Period());
		prod.getPeriod().setUnit(PeriodUnit.RANGE_DAY);
		prod.getPeriod().setStartValue(1);
		prod.getPeriod().setEndValue(100);
		prod.setPremi(5000);
		prod.setBasePremi(5000);
		
		service.productService = Mockito.mock(ProductService.class);
		when(service.productService.fetchProductByProductId(prodId)).thenReturn(prod);
		
		service.orderService = Mockito.mock(OrderService.class);
		when(service.orderService.isPolicyEndDateWithinRange(orderDto.getPolicyStartDate().toLocalDate(),
				orderDto.getPolicyEndDate().toLocalDate(),
				prod.getPeriod())).thenReturn(true);
		
		AppException exp = null;
		try{ 
			service.registerOrder("testId", orderDto, true);
		}catch(AppException e){
			exp = e;
		}
		
		assertNull(exp);		
	}
	
	@Test
	public void testTravelUserAge(){
		int configTravelMinPolicyStartDatePeriod = 2;
		int configTravelMinAge = 17;
		int configTravelMaxAge = 75;
		
		int policyStartDatePlusYear = 1;				
				
		NinelivesConfigProperties config = new NinelivesConfigProperties();
		config.getOrder().setPolicyStartDatePeriod(365);
		config.getOrder().setTravelMinPolicyStartDatePeriod(configTravelMinPolicyStartDatePeriod);
		config.getOrder().setTravelMinimumAge(configTravelMinAge);
		config.getOrder().setTravelMaximumAge(configTravelMaxAge);
		
		ApiOrderService service = new ApiOrderService();
		service.config = config;	
				
		String prodId = "P102006204001";
		String covCatId = "102";
		
		OrderDto orderDto = new OrderDto();
		orderDto.setPolicyStartDate(LocalDateTime.now().plusYears(policyStartDatePlusYear)); //set the policy start-date next year
		orderDto.setPolicyEndDate(LocalDateTime.now().plusYears(policyStartDatePlusYear).plusDays(10));
		
		ProductDto prodDto = new ProductDto();
		prodDto.setProductId(prodId);
		List<ProductDto> products = new ArrayList<ProductDto>();
		products.add(prodDto);
		orderDto.setProducts(products);

		orderDto.setTotalPremi(5000);
		
		Product prod = new Product();
		prod.setProductId(prodId);
		prod.setCoverage(new Coverage());
		prod.getCoverage().setCoverageCategoryId(covCatId);
		prod.getCoverage().setHasBeneficiary(false);
		prod.setPeriodId("204");
		prod.setPeriod(new Period());
		prod.getPeriod().setUnit(PeriodUnit.RANGE_DAY);
		prod.getPeriod().setStartValue(1);
		prod.getPeriod().setEndValue(100);
		prod.setPremi(5000);
		prod.setBasePremi(5000);
		
		service.productService = Mockito.mock(ProductService.class);
		when(service.productService.fetchProductByProductId(prodId)).thenReturn(prod);
		
		service.orderService = Mockito.mock(OrderService.class);
		when(service.orderService.isPolicyEndDateWithinRange(orderDto.getPolicyStartDate().toLocalDate(),
				orderDto.getPolicyEndDate().toLocalDate(),
				prod.getPeriod())).thenReturn(true);
		
		service.insuranceService = Mockito.mock(InsuranceService.class);
		service.policyOrderTrxService = Mockito.mock(PolicyOrderTrxService.class);
		/*
		 * Test 1
		 */
		int userAgeAtPolicyStartDate = 16;//test value; current age is userAgeAtPolicyStartDate-policyStartDatePlusYear
		User user = new User();
		user.setBirthDate(LocalDate.now().minusYears(userAgeAtPolicyStartDate-policyStartDatePlusYear));
		user.setUserId("testId");
		user.setIdCardFileId(123L);
		user.setPassportFileId(123L);
		
		service.userService = Mockito.mock(UserService.class);
		when(service.userService.fetchByUserId(user.getUserId())).thenReturn(user);
		
		AppException exp = null;
		try{ 
			service.registerOrder(user.getUserId(), orderDto, false);
		}catch(AppException e){
			exp=e;
		}
		assertNotNull(exp);
		assertThat(exp.getCode(), is(ErrorCode.ERR4018_ORDER_PROFILE_AGE_INVALID));
		
		/*
		 * Test 2
		 */
		userAgeAtPolicyStartDate = 17;//test value; current age is userAgeAtPolicyStartDate-policyStartDatePlusYear
		User user2 = new User();
		user2.setBirthDate(LocalDate.now().minusYears(userAgeAtPolicyStartDate-policyStartDatePlusYear));
		user2.setUserId("testId2");
		user2.setName("name");
		user2.setIdCardFileId(123L);
		user2.setBirthPlace("test");
		user2.setGender(Gender.FEMALE);
		user2.setIdCardFileId(123L);
		user2.setPassportFileId(123L);
		user2.setPhone("123");
		
		when(service.userService.fetchByUserId(user2.getUserId())).thenReturn(user2);
		when(service.orderService.isUserProfileCompleteForOrder(user2)).thenReturn(true);
		
		exp = null;
		try{ 
			service.registerOrder(user2.getUserId(), orderDto, false);
		}catch(AppException e){
			exp=e;			
		}
		assertNull(exp);
	}
		
}
