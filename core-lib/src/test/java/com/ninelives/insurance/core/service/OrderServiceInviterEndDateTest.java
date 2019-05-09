package com.ninelives.insurance.core.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.mybatis.mapper.PolicyOrderMapper;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.ref.PolicyStatus;

public class OrderServiceInviterEndDateTest {
	OrderService orderService;
	LocalDate today;
	@Test
	public void testEmptyListAfterFilter() {
		//test data --1-- overdue
		List<PolicyOrder> pos = new ArrayList<>();
		pos.add(new PolicyOrder());
		pos.get(0).setOrderDate(today.minusDays(5));
		pos.get(0).setPolicyStartDate(today.minusDays(4)); //this should be overdue already
		pos.get(0).setPolicyEndDate(today.minusDays(3));
		pos.get(0).setStatus(PolicyStatus.SUBMITTED);
						
		//test data --2-- expired
		pos.add(new PolicyOrder());
		pos.get(1).setOrderDate(today.minusDays(5));
		pos.get(1).setPolicyStartDate(today.minusDays(4)); //this should be overdue already
		pos.get(1).setPolicyEndDate(today.minusDays(3));
		pos.get(1).setStatus(PolicyStatus.APPROVED);
				
		PolicyOrderMapper orderMapper = Mockito.mock(PolicyOrderMapper.class);
		when(
				orderMapper.selectForInviterPolicyStartDateByCoverage(any(String.class), (List<String>) any(List.class))
		).thenReturn(pos);
		orderService.policyOrderMapper=orderMapper;
		
		//run test
		LocalDate result = orderService.resolveInviterPolicyStartDate("userId", new ArrayList<String>(), today);
		assertEquals(today, result);
	}
	
	@Test
	public void testNullList() {
		PolicyOrderMapper orderMapper = Mockito.mock(PolicyOrderMapper.class);
		when(
				orderMapper.selectForInviterPolicyStartDateByCoverage(any(String.class), (List<String>) any(List.class))
		).thenReturn(null);
		orderService.policyOrderMapper=orderMapper;
		
		//run test
		LocalDate result = orderService.resolveInviterPolicyStartDate("userId", new ArrayList<String>(), today);
		assertEquals(today, result);
		
	}
	
	@Test
	public void testEmptyList() {
		PolicyOrderMapper orderMapper = Mockito.mock(PolicyOrderMapper.class);
		when(
				orderMapper.selectForInviterPolicyStartDateByCoverage(any(String.class), (List<String>) any(List.class))
		).thenReturn(new ArrayList<PolicyOrder>());
		orderService.policyOrderMapper=orderMapper;
		
		//run test
		LocalDate result = orderService.resolveInviterPolicyStartDate("userId", new ArrayList<String>(), today);
		assertEquals(today, result);
		
	}
	
	@Test
	public void testMaxFromSubmitted() {
		//test data --1-- overdue
		List<PolicyOrder> pos = new ArrayList<>();
		pos.add(new PolicyOrder());
		pos.get(0).setOrderDate(today.minusDays(5));
		pos.get(0).setPolicyStartDate(today.minusDays(4)); //this should be overdue already
		pos.get(0).setPolicyEndDate(today.plusDays(15));
		pos.get(0).setStatus(PolicyStatus.SUBMITTED);
						
		//test data --2-- active
		pos.add(new PolicyOrder());
		pos.get(1).setOrderDate(today.minusDays(5));
		pos.get(1).setPolicyStartDate(today.minusDays(4)); 
		pos.get(1).setPolicyEndDate(today.plusDays(10)); //active
		pos.get(1).setStatus(PolicyStatus.APPROVED);
		
		//test data --3-- terminated
		pos.add(new PolicyOrder());
		pos.get(2).setOrderDate(today.minusDays(5));
		pos.get(2).setPolicyStartDate(today.minusDays(4)); 
		pos.get(2).setPolicyEndDate(today.plusDays(20));
		pos.get(2).setStatus(PolicyStatus.TERMINATED);
				
		//test data --4-- overdue
		pos.add(new PolicyOrder());
		pos.get(3).setOrderDate(today.minusDays(5)); //not overdue yet
		pos.get(3).setPolicyStartDate(today.plusDays(4)); 
		pos.get(3).setPolicyEndDate(today.plusDays(30));
		pos.get(3).setStatus(PolicyStatus.SUBMITTED);

		PolicyOrderMapper orderMapper = Mockito.mock(PolicyOrderMapper.class);
		when(
				orderMapper.selectForInviterPolicyStartDateByCoverage(any(String.class), (List<String>) any(List.class))
		).thenReturn(pos);
		orderService.policyOrderMapper=orderMapper;
		
		//run test
		LocalDate result = orderService.resolveInviterPolicyStartDate("userId", new ArrayList<String>(), today);
		assertEquals(today.plusDays(31), result);
	}
	
	@Test
	public void testMaxFromActive() {
		//test data --1-- overdue
		List<PolicyOrder> pos = new ArrayList<>();
		pos.add(new PolicyOrder());
		pos.get(0).setOrderDate(today.minusDays(5));
		pos.get(0).setPolicyStartDate(today.minusDays(4)); //this should be overdue already
		pos.get(0).setPolicyEndDate(today.plusDays(15));
		pos.get(0).setStatus(PolicyStatus.SUBMITTED);
						
		//test data --2-- active
		pos.add(new PolicyOrder());
		pos.get(1).setOrderDate(today.minusDays(5));
		pos.get(1).setPolicyStartDate(today.minusDays(4)); 
		pos.get(1).setPolicyEndDate(today.plusDays(10)); //active
		pos.get(1).setStatus(PolicyStatus.APPROVED);
		
		//test data --3-- terminated
		pos.add(new PolicyOrder());
		pos.get(2).setOrderDate(today.minusDays(5));
		pos.get(2).setPolicyStartDate(today.minusDays(4)); 
		pos.get(2).setPolicyEndDate(today.plusDays(20));
		pos.get(2).setStatus(PolicyStatus.TERMINATED);
				
		//test data --4-- overdue
		pos.add(new PolicyOrder());
		pos.get(3).setOrderDate(today.minusDays(31)); //overdue already
		pos.get(3).setPolicyStartDate(today.plusDays(4)); 
		pos.get(3).setPolicyEndDate(today.plusDays(30));
		pos.get(3).setStatus(PolicyStatus.SUBMITTED);

		PolicyOrderMapper orderMapper = Mockito.mock(PolicyOrderMapper.class);
		when(
				orderMapper.selectForInviterPolicyStartDateByCoverage(any(String.class), (List<String>) any(List.class))
		).thenReturn(pos);
		orderService.policyOrderMapper=orderMapper;
		
		//run test
		LocalDate result = orderService.resolveInviterPolicyStartDate("userId", new ArrayList<String>(), today);
		assertEquals(today.plusDays(11), result);
	}
	
	@Test
	public void testMaxFromListOfActive() {
		//test data --1-- active
		List<PolicyOrder> pos = new ArrayList<>();
		pos.add(new PolicyOrder());		
		pos.get(0).setOrderDate(today.minusDays(5));
		pos.get(0).setPolicyStartDate(today.minusDays(4)); 
		pos.get(0).setPolicyEndDate(today.plusDays(11)); //active
		pos.get(0).setStatus(PolicyStatus.APPROVED);
						
		//test data --2-- active
		pos.add(new PolicyOrder());
		pos.get(1).setOrderDate(today.minusDays(5));
		pos.get(1).setPolicyStartDate(today.minusDays(4)); 
		pos.get(1).setPolicyEndDate(today.plusDays(10)); //active
		pos.get(1).setStatus(PolicyStatus.APPROVED);
		
		//test data --3-- active
		pos.add(new PolicyOrder());
		pos.get(2).setOrderDate(today.minusDays(5));
		pos.get(2).setPolicyStartDate(today.minusDays(4)); 
		pos.get(2).setPolicyEndDate(today.plusDays(13)); //active
		pos.get(2).setStatus(PolicyStatus.APPROVED);
				
		//test data --4-- active
		pos.add(new PolicyOrder());
		pos.get(3).setOrderDate(today.minusDays(5));
		pos.get(3).setPolicyStartDate(today.minusDays(4)); 
		pos.get(3).setPolicyEndDate(today.plusDays(14)); //active
		pos.get(3).setStatus(PolicyStatus.APPROVED);

		PolicyOrderMapper orderMapper = Mockito.mock(PolicyOrderMapper.class);
		when(
				orderMapper.selectForInviterPolicyStartDateByCoverage(any(String.class), (List<String>) any(List.class))
		).thenReturn(pos);
		orderService.policyOrderMapper=orderMapper;
		
		//run test
		LocalDate result = orderService.resolveInviterPolicyStartDate("userId", new ArrayList<String>(), today);
		assertEquals(today.plusDays(15), result);
	}
	
	@Before
	public void before() {
		NinelivesConfigProperties config = new NinelivesConfigProperties();
		config.getOrder().setPolicyDueDatePeriod(30);
		
		orderService = new OrderService();
		orderService.config=config;
		
		today =  LocalDate.parse("2018-05-05");
	}
}
