package com.ninelives.insurance.api.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.ninelives.insurance.api.ref.OrderDtoFilterStatus;
import com.ninelives.insurance.api.service.OrderService;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class OrderServiceFilterTypeTest {
	@Test
	public void testFilterType(){
		
		OrderService orderService = new OrderService();
		
		String[] statusActive = {"ACTIVE"};		
		OrderDtoFilterStatus filterType = orderService.getFetchOrderFilterType(statusActive);		
		assertEquals(OrderDtoFilterStatus.ACTIVE, filterType);
		
		String[] statusApproved = {"APPROVED"};
		filterType = orderService.getFetchOrderFilterType(statusApproved);		
		assertEquals(OrderDtoFilterStatus.APPROVED, filterType);
		
		String[] statusExpired = {"EXPIRED", "TERMINATED"};
		filterType = orderService.getFetchOrderFilterType(statusExpired);
		assertEquals(OrderDtoFilterStatus.EXPIRED, filterType);
		
		String[] statusUnpaid = {"SUBMITTED", "OFFEREXPIRED", "INPAYMENT", "PAID"};
		filterType = orderService.getFetchOrderFilterType(statusUnpaid);
		assertEquals(OrderDtoFilterStatus.UNPAID, filterType);
		
		String[] statusAll = null;
		filterType = orderService.getFetchOrderFilterType(statusAll);
		assertEquals(OrderDtoFilterStatus.ALL, filterType);
		
		String[] statusAll2 = {""};
		filterType = orderService.getFetchOrderFilterType(statusAll2);
		assertEquals(OrderDtoFilterStatus.ALL, filterType);
	}
}
