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

import com.ninelives.insurance.api.mybatis.mapper.PolicyOrderMapper;
import com.ninelives.insurance.model.PolicyOrderCoverage;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class OrderServiceCoverageLimitTest {
	
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
		
		conflictList.add(new PolicyOrderCoverage());
		conflictList.get(0).setCoverageId("101001");
		conflictList.add(new PolicyOrderCoverage());
		conflictList.get(1).setCoverageId("101001");		
		
		boolean result = orderService.isOverCoverageInSamePeriodLimit("id", today, today, today, coverageIds);
		
		assertEquals(result, false);
		//---
		conflictList.clear();
		conflictList.add(new PolicyOrderCoverage());
		conflictList.get(0).setCoverageId("101001");
		conflictList.add(new PolicyOrderCoverage());
		conflictList.get(1).setCoverageId("101001");
		conflictList.add(new PolicyOrderCoverage());
		conflictList.get(2).setCoverageId("101001");
		conflictList.add(new PolicyOrderCoverage());
		conflictList.get(3).setCoverageId("101000");
		boolean result2 = orderService.isOverCoverageInSamePeriodLimit("id", today, today, today, coverageIds);
		
		assertEquals(result2, true);
		//---
		conflictList.clear();		
		conflictList.add(new PolicyOrderCoverage());
		conflictList.get(0).setCoverageId("101001");
		conflictList.add(new PolicyOrderCoverage());
		conflictList.get(1).setCoverageId("101002");
		conflictList.add(new PolicyOrderCoverage());
		conflictList.get(2).setCoverageId("101003");
		
		boolean result3 = orderService.isOverCoverageInSamePeriodLimit("id", today, today, today, coverageIds);
		
		assertEquals(result3, false);
		
		
	}
}
