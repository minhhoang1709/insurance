package com.ninelives.insurance.api.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.ninelives.insurance.api.service.ApiOrderService;
import com.ninelives.insurance.ref.OrderDtoFilterStatus;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class ApiOrderServiceFilterTypeTest {
	@Test
	public void testFilterType(){
		
		ApiOrderService orderService = new ApiOrderService();
		
		String[] statusActive = {"ACTIVE"};		
		OrderDtoFilterStatus filterType = orderService.getFetchOrderFilterType(statusActive);		
		assertEquals(OrderDtoFilterStatus.ACTIVE, filterType);
		
		String[] statusApproved = {"APPROVED"};
		filterType = orderService.getFetchOrderFilterType(statusApproved);		
		assertEquals(OrderDtoFilterStatus.APPROVED, filterType);
		
		String[] statusExpired = {"EXPIRED", "TERMINATED"};
		filterType = orderService.getFetchOrderFilterType(statusExpired);
		assertEquals(OrderDtoFilterStatus.EXPIRED, filterType);
		
		String[] statusUnpaid = {"SUBMITTED", "OVERDUE", "INPAYMENT", "PAID"};
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
