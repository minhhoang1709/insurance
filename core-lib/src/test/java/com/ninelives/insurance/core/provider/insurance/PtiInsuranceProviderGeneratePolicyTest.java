package com.ninelives.insurance.core.provider.insurance;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

import java.time.LocalDate;

import org.junit.Test;
import org.mockito.Mockito;

import com.ninelives.insurance.core.mybatis.mapper.InsurerMapper;
import com.ninelives.insurance.model.CoverageCategory;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.ref.CoverageCategoryType;

public class PtiInsuranceProviderGeneratePolicyTest {
	@Test
	public void testGeneratePolicyNumber() throws InsuranceProviderException{
		PtiInsuranceProvider prod = new PtiInsuranceProvider();
		prod.insurerMapper = Mockito.mock(InsurerMapper.class);
		doReturn(213).when(prod.insurerMapper).nextPolicyNumberSequence(any(String.class));
		
		PolicyOrder order = new PolicyOrder();
		order.setCoverageCategory(new CoverageCategory());
		order.getCoverageCategory().setType(CoverageCategoryType.PA);
		order.setOrderDate(LocalDate.now());
		
		String year = String.valueOf(LocalDate.now().getYear());
		
		String result = prod.generatePolicyNumber(order);		
		
		assertEquals("PA0000213/9Lives/"+year,result);
	}
}
