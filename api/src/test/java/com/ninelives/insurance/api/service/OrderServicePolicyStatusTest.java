package com.ninelives.insurance.api.service;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.ninelives.insurance.api.NinelivesConfigProperties;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.ref.PolicyStatus;

@RunWith(SpringRunner.class)
public class OrderServicePolicyStatusTest {
	final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@Test
	public void testPolicyStatusMappingSubmitted(){
		NinelivesConfigProperties config = new NinelivesConfigProperties();
		config.getOrder().setPolicyDueDatePeriod(5);
				
		OrderService orderService = new OrderService();
		orderService.config=config;
		
		PolicyOrder policyOrder = new PolicyOrder();
		policyOrder.setStatus(PolicyStatus.SUBMITTED);
		policyOrder.setOrderDate(LocalDate.parse("2017-11-10", formatter));
		policyOrder.setPolicyStartDate(LocalDate.parse("2017-11-15", formatter));
		LocalDate today = LocalDate.parse("2017-11-15", formatter);				
		orderService.mapPolicyOrderStatus(policyOrder, today);		
		assertEquals(PolicyStatus.SUBMITTED, policyOrder.getStatus());
		
		PolicyOrder policyOrder2 = new PolicyOrder();
		policyOrder2.setStatus(PolicyStatus.SUBMITTED);
		policyOrder2.setOrderDate(LocalDate.parse("2017-11-10", formatter));
		policyOrder2.setPolicyStartDate(LocalDate.parse("2017-11-14", formatter));
		LocalDate today2 = LocalDate.parse("2017-11-14", formatter);				
		orderService.mapPolicyOrderStatus(policyOrder2, today2);		
		assertEquals(PolicyStatus.SUBMITTED, policyOrder2.getStatus());
		
	}
	
	@Test
	public void testPolicyStatusMappingOverdue(){
		NinelivesConfigProperties config = new NinelivesConfigProperties();
		config.getOrder().setPolicyDueDatePeriod(5);
				
		OrderService orderService = new OrderService();
		orderService.config=config;
				
		PolicyOrder policyOrder = new PolicyOrder();
		policyOrder.setStatus(PolicyStatus.SUBMITTED);
		policyOrder.setOrderDate(LocalDate.parse("2017-11-10", formatter));		
		LocalDate today = LocalDate.parse("2017-11-16", formatter);	
		orderService.mapPolicyOrderStatus(policyOrder, today);		
		assertEquals(PolicyStatus.OVERDUE, policyOrder.getStatus());
		
		PolicyOrder policyOrder2 = new PolicyOrder();
		policyOrder2.setStatus(PolicyStatus.SUBMITTED);
		policyOrder2.setOrderDate(LocalDate.parse("2017-11-10", formatter));
		LocalDate today2 = LocalDate.parse("2017-11-17", formatter);	
		orderService.mapPolicyOrderStatus(policyOrder2, today2);		
		assertEquals(PolicyStatus.OVERDUE, policyOrder2.getStatus());
		
		PolicyOrder policyOrder3 = new PolicyOrder();
		policyOrder3.setStatus(PolicyStatus.SUBMITTED);
		policyOrder3.setOrderDate(LocalDate.parse("2017-11-10", formatter));
		policyOrder3.setPolicyStartDate(LocalDate.parse("2017-11-13", formatter));		
		LocalDate today3 = LocalDate.parse("2017-11-14", formatter);	
		orderService.mapPolicyOrderStatus(policyOrder3, today3);		
		assertEquals(PolicyStatus.OVERDUE, policyOrder3.getStatus());
		
		PolicyOrder policyOrder4 = new PolicyOrder();
		policyOrder4.setStatus(PolicyStatus.SUBMITTED);
		policyOrder4.setOrderDate(LocalDate.parse("2017-11-10", formatter));
		policyOrder4.setPolicyStartDate(LocalDate.parse("2017-11-14", formatter));		
		LocalDate today4 = LocalDate.parse("2017-11-14", formatter);	
		orderService.mapPolicyOrderStatus(policyOrder4, today4);		
		assertEquals(PolicyStatus.SUBMITTED, policyOrder4.getStatus());

	}
	
	@Test
	public void testPolicyStatusMappingApproved(){
		NinelivesConfigProperties config = new NinelivesConfigProperties();
		config.getOrder().setPolicyDueDatePeriod(5);
				
		OrderService orderService = new OrderService();
		orderService.config=config;
				
		PolicyOrder policyOrder = new PolicyOrder();
		policyOrder.setStatus(PolicyStatus.APPROVED);
		policyOrder.setPolicyStartDate(LocalDate.parse("2017-11-10", formatter));
		policyOrder.setPolicyEndDate(LocalDate.parse("2017-11-15", formatter));
		LocalDate today = LocalDate.parse("2017-11-09", formatter);	
		orderService.mapPolicyOrderStatus(policyOrder, today);		
		assertEquals(PolicyStatus.APPROVED, policyOrder.getStatus());
	}
	
	@Test
	public void testPolicyStatusMappingActive(){
		NinelivesConfigProperties config = new NinelivesConfigProperties();
		config.getOrder().setPolicyDueDatePeriod(5);
				
		OrderService orderService = new OrderService();
		orderService.config=config;
				
		PolicyOrder policyOrder = new PolicyOrder();
		policyOrder.setStatus(PolicyStatus.APPROVED);
		policyOrder.setPolicyStartDate(LocalDate.parse("2017-11-10", formatter));
		policyOrder.setPolicyEndDate(LocalDate.parse("2017-11-15", formatter));
		LocalDate today = LocalDate.parse("2017-11-10", formatter);	
		orderService.mapPolicyOrderStatus(policyOrder, today);		
		assertEquals(PolicyStatus.ACTIVE, policyOrder.getStatus());	
		
		PolicyOrder policyOrder2 = new PolicyOrder();
		policyOrder2.setStatus(PolicyStatus.APPROVED);
		policyOrder2.setPolicyStartDate(LocalDate.parse("2017-11-10", formatter));
		policyOrder2.setPolicyEndDate(LocalDate.parse("2017-11-15", formatter));
		LocalDate today2 = LocalDate.parse("2017-11-15", formatter);	
		orderService.mapPolicyOrderStatus(policyOrder2, today2);		
		assertEquals(PolicyStatus.ACTIVE, policyOrder2.getStatus());
		
		PolicyOrder policyOrder3 = new PolicyOrder();
		policyOrder3.setStatus(PolicyStatus.APPROVED);
		policyOrder3.setPolicyStartDate(LocalDate.parse("2017-11-10", formatter));
		policyOrder3.setPolicyEndDate(LocalDate.parse("2017-11-15", formatter));
		LocalDate today3 = LocalDate.parse("2017-11-13", formatter);	
		orderService.mapPolicyOrderStatus(policyOrder3, today3);		
		assertEquals(PolicyStatus.ACTIVE, policyOrder3.getStatus());
	}
	
	@Test
	public void testPolicyStatusMappingExpired(){
		NinelivesConfigProperties config = new NinelivesConfigProperties();
		config.getOrder().setPolicyDueDatePeriod(5);
				
		OrderService orderService = new OrderService();
		orderService.config=config;
				
		PolicyOrder policyOrder = new PolicyOrder();
		policyOrder.setStatus(PolicyStatus.APPROVED);
		policyOrder.setPolicyStartDate(LocalDate.parse("2017-11-10", formatter));
		policyOrder.setPolicyEndDate(LocalDate.parse("2017-11-15", formatter));
		LocalDate today = LocalDate.parse("2017-11-16", formatter);	
		orderService.mapPolicyOrderStatus(policyOrder, today);		
		assertEquals(PolicyStatus.EXPIRED, policyOrder.getStatus());
		
		PolicyOrder policyOrder2 = new PolicyOrder();
		policyOrder2.setStatus(PolicyStatus.APPROVED);
		policyOrder2.setPolicyStartDate(LocalDate.parse("2017-11-10", formatter));
		policyOrder2.setPolicyEndDate(LocalDate.parse("2017-11-15", formatter));
		LocalDate today2 = LocalDate.parse("2017-11-17", formatter);	
		orderService.mapPolicyOrderStatus(policyOrder2, today2);		
		assertEquals(PolicyStatus.EXPIRED, policyOrder2.getStatus());
	}
	
	@Test
	public void testPolicyStatusMappingOtherStatus(){
		OrderService orderService = new OrderService();
		LocalDate today = LocalDate.parse("2017-11-16", formatter);
		
		PolicyOrder policyOrder = new PolicyOrder();
		policyOrder.setStatus(PolicyStatus.PAID);
		orderService.mapPolicyOrderStatus(policyOrder, today);
		
		PolicyOrder policyOrder2 = new PolicyOrder();
		policyOrder2.setStatus(PolicyStatus.INPAYMENT);
		orderService.mapPolicyOrderStatus(policyOrder2, today);
		
		PolicyOrder policyOrder3 = new PolicyOrder();
		policyOrder3.setStatus(PolicyStatus.TERMINATED);
		orderService.mapPolicyOrderStatus(policyOrder3, today);
	}
}
