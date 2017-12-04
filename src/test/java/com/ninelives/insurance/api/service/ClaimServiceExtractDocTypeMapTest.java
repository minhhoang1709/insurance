package com.ninelives.insurance.api.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.ninelives.insurance.api.model.CoverageClaimDocType;
import com.ninelives.insurance.api.model.PolicyOrderProduct;

//@RunWith(SpringRunner.class)
public class ClaimServiceExtractDocTypeMapTest {
	
//	@Test
//	public void testExtractMap(){
//		List<PolicyOrderProduct> pops = new ArrayList<>();
//		pops.add(new PolicyOrderProduct());
//		pops.get(0).setCoverageClaimDocTypes(new ArrayList<CoverageClaimDocType>());
//		pops.get(0).getCoverageClaimDocTypes().add(new CoverageClaimDocType());
//		pops.get(0).getCoverageClaimDocTypes().get(0).setClaimDocTypeId("DT001");
//		pops.get(0).getCoverageClaimDocTypes().get(0).setIsMandatory(true);
//		pops.get(0).getCoverageClaimDocTypes().add(new CoverageClaimDocType());
//		pops.get(0).getCoverageClaimDocTypes().get(1).setClaimDocTypeId("DT002");
//		pops.get(0).getCoverageClaimDocTypes().get(1).setIsMandatory(true);
//		pops.get(0).getCoverageClaimDocTypes().add(new CoverageClaimDocType());
//		pops.get(0).getCoverageClaimDocTypes().get(2).setClaimDocTypeId("DT004");
//		pops.get(0).getCoverageClaimDocTypes().get(2).setIsMandatory(false);
//		
//		pops.add(new PolicyOrderProduct());
//		pops.get(1).setCoverageClaimDocTypes(new ArrayList<CoverageClaimDocType>());
//		pops.get(1).getCoverageClaimDocTypes().add(new CoverageClaimDocType());
//		pops.get(1).getCoverageClaimDocTypes().get(0).setClaimDocTypeId("DT001");
//		pops.get(1).getCoverageClaimDocTypes().get(0).setIsMandatory(false);
//		pops.get(1).getCoverageClaimDocTypes().add(new CoverageClaimDocType());
//		pops.get(1).getCoverageClaimDocTypes().get(1).setClaimDocTypeId("DT003");
//		pops.get(1).getCoverageClaimDocTypes().get(1).setIsMandatory(false);
//		pops.get(1).getCoverageClaimDocTypes().add(new CoverageClaimDocType());
//		pops.get(1).getCoverageClaimDocTypes().get(2).setClaimDocTypeId("DT004");
//		pops.get(1).getCoverageClaimDocTypes().get(2).setIsMandatory(true);
//		
//		ClaimService claimService = new ClaimService();
//		Map<String,Boolean> result = claimService.extractDocTypeMap(pops);
//		
//		Map<String,Boolean> expectedResult = new HashMap<>();
//		expectedResult.put("DT001", true);
//		expectedResult.put("DT002", true);
//		expectedResult.put("DT003", false);
//		expectedResult.put("DT004", true);
//		
//		System.out.println("result map is "+result);
//		
//		assertEquals(expectedResult, result);
//	}
}
