package com.ninelives.insurance.api.service;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.ninelives.insurance.api.model.PolicyOrderCoverage;
import com.ninelives.insurance.api.mybatis.mapper.PolicyOrderMapper;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class OrderServiceOrderTest {
	
	@Test
	public void testConflict(){
		List<PolicyOrderCoverage> conflictList = new ArrayList<>();
		
		List<String> coverageIds = Arrays.asList(new String[]{"101001", "101002", "101003"});
		
		LocalDate today = LocalDate.now();
		
		PolicyOrderMapper policyOrderMapper = Mockito.mock(PolicyOrderMapper.class);
		when(policyOrderMapper.selectCoverageWithConflictedPolicyDate("id", today, today, 
				today, coverageIds)).thenReturn(conflictList);
		
		OrderService orderService = new OrderService();
		orderService.policyOrderMapper = policyOrderMapper;
		orderService.policyConflictPeriodLimit=3;
		
		PolicyOrderCoverage poc11 = new PolicyOrderCoverage();
		poc11.setCoverageId("101001");
		conflictList.add(poc11);
		PolicyOrderCoverage poc12 = new PolicyOrderCoverage();
		poc12.setCoverageId("101001");
		conflictList.add(poc12);		
		
		boolean result = orderService.isOverCoverageInSamePeriodLimit("id", today, today, today, coverageIds);
		
		assertEquals(result, false);
		
		conflictList.clear();
		
		PolicyOrderCoverage poc21 = new PolicyOrderCoverage();
		poc21.setCoverageId("101001");
		conflictList.add(poc21);
		PolicyOrderCoverage poc22 = new PolicyOrderCoverage();
		poc22.setCoverageId("101001");
		conflictList.add(poc22);
		PolicyOrderCoverage poc23 = new PolicyOrderCoverage();
		poc23.setCoverageId("101001");
		conflictList.add(poc23);
		PolicyOrderCoverage poc24 = new PolicyOrderCoverage();
		poc24.setCoverageId("101000");
		conflictList.add(poc24);
		
		boolean result2 = orderService.isOverCoverageInSamePeriodLimit("id", today, today, today, coverageIds);
		
		assertEquals(result2, true);
		
	}
}
