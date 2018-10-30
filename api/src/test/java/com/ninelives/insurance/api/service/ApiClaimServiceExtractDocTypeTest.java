package com.ninelives.insurance.api.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.ninelives.insurance.api.dto.ClaimCoverageDto;
import com.ninelives.insurance.api.dto.CoverageDto;
import com.ninelives.insurance.core.service.ProductService;
import com.ninelives.insurance.model.ClaimDocType;
import com.ninelives.insurance.model.Coverage;
import com.ninelives.insurance.model.CoverageClaimDocType;
import com.ninelives.insurance.ref.ClaimDocUsageType;

//@RunWith(SpringRunner.class)
public class ApiClaimServiceExtractDocTypeTest {
	//case:
	// case 1 with isfamily true, 2 diff cove with usage KK 1 mandatory 1 optional, one of it with usage Regular
	// case 2 with isfamily false, 2 diff cove with usage KK 1 mandatory 1 optional, one of it with usage Regular
	
//	@Test
//	public void testFamilyCardIncludedInDocMap(){
//		
//		//coverage-1 with 1 mandatory KK, 1 mandatory regular
//		Coverage cov1 = new Coverage();
//		cov1.setCoverageId("101001");
//		cov1.setCoverageClaimDocTypes(new ArrayList<>());
//		cov1.getCoverageClaimDocTypes().add(new CoverageClaimDocType());
//		cov1.getCoverageClaimDocTypes().get(0).setClaimDocTypeId("DT001");
//		cov1.getCoverageClaimDocTypes().get(0).setIsMandatory(true);
//		cov1.getCoverageClaimDocTypes().get(0).setClaimDocType(new ClaimDocType());
//		cov1.getCoverageClaimDocTypes().get(0).getClaimDocType().setClaimDocTypeId("DT001");
//		cov1.getCoverageClaimDocTypes().get(0).getClaimDocType().setUsageType(ClaimDocUsageType.FAMILY_CARD);
//		cov1.getCoverageClaimDocTypes().add(new CoverageClaimDocType());
//		cov1.getCoverageClaimDocTypes().get(1).setClaimDocTypeId("DT002");
//		cov1.getCoverageClaimDocTypes().get(1).setIsMandatory(true);
//		cov1.getCoverageClaimDocTypes().get(1).setClaimDocType(new ClaimDocType());
//		cov1.getCoverageClaimDocTypes().get(1).getClaimDocType().setClaimDocTypeId("DT002");
//		cov1.getCoverageClaimDocTypes().get(1).getClaimDocType().setUsageType(ClaimDocUsageType.REGULAR);
//
//		//coverage-2 with 1 optional KK, 1 optional regular
//		Coverage cov2 = new Coverage();
//		cov2.setCoverageId("102001");
//		cov2.setCoverageClaimDocTypes(new ArrayList<>());
//		cov2.getCoverageClaimDocTypes().add(new CoverageClaimDocType());
//		cov2.getCoverageClaimDocTypes().get(0).setClaimDocTypeId("DT001");
//		cov2.getCoverageClaimDocTypes().get(0).setIsMandatory(false);
//		cov2.getCoverageClaimDocTypes().get(0).setClaimDocType(new ClaimDocType());
//		cov2.getCoverageClaimDocTypes().get(0).getClaimDocType().setClaimDocTypeId("DT001");
//		cov2.getCoverageClaimDocTypes().get(0).getClaimDocType().setUsageType(ClaimDocUsageType.FAMILY_CARD);
//		cov2.getCoverageClaimDocTypes().add(new CoverageClaimDocType());
//		cov2.getCoverageClaimDocTypes().get(1).setClaimDocTypeId("DT002");
//		cov2.getCoverageClaimDocTypes().get(1).setIsMandatory(false);
//		cov2.getCoverageClaimDocTypes().get(1).setClaimDocType(new ClaimDocType());
//		cov2.getCoverageClaimDocTypes().get(1).getClaimDocType().setClaimDocTypeId("DT002");
//		cov2.getCoverageClaimDocTypes().get(1).getClaimDocType().setUsageType(ClaimDocUsageType.REGULAR);
//		
//		//test input
//		List<ClaimCoverageDto> dtos = new ArrayList<>();
//		dtos.add(new ClaimCoverageDto());
//		dtos.get(0).setCoverage(new CoverageDto());
//		dtos.get(0).getCoverage().setCoverageId("101001");
//		dtos.add(new ClaimCoverageDto());
//		dtos.get(1).setCoverage(new CoverageDto());
//		dtos.get(1).getCoverage().setCoverageId("102001");
//		
//		boolean isOrderHasFamily = true;
//		
//		//expected output: family is included
//		Map<String, Boolean> expectedResultMap = new HashMap<>();
//		expectedResultMap.put("DT001", true);
//		expectedResultMap.put("DT002", true);
//		
//		ApiClaimService claimService = new ApiClaimService();
//		claimService.productService = Mockito.mock(ProductService.class);
//		when(claimService.productService.fetchCoverageByCoverageId("101001")).thenReturn(cov1);
//		when(claimService.productService.fetchCoverageByCoverageId("102001")).thenReturn(cov2);
//		
//		Map<String, Boolean> resultMap = claimService.extractDocTypeMap(dtos, isOrderHasFamily);
//		
//		assertThat(resultMap, is(expectedResultMap));
//	}
//	
//	@Test
//	public void testFamilyCardExcludedFromDocMap(){
//		
//		//coverage-1 with 1 mandatory KK, 1 mandatory regular
//		Coverage cov1 = new Coverage();
//		cov1.setCoverageId("101001");
//		cov1.setCoverageClaimDocTypes(new ArrayList<>());
//		cov1.getCoverageClaimDocTypes().add(new CoverageClaimDocType());
//		cov1.getCoverageClaimDocTypes().get(0).setClaimDocTypeId("DT001");
//		cov1.getCoverageClaimDocTypes().get(0).setIsMandatory(true);
//		cov1.getCoverageClaimDocTypes().get(0).setClaimDocType(new ClaimDocType());
//		cov1.getCoverageClaimDocTypes().get(0).getClaimDocType().setClaimDocTypeId("DT001");
//		cov1.getCoverageClaimDocTypes().get(0).getClaimDocType().setUsageType(ClaimDocUsageType.FAMILY_CARD);
//		cov1.getCoverageClaimDocTypes().add(new CoverageClaimDocType());
//		cov1.getCoverageClaimDocTypes().get(1).setClaimDocTypeId("DT002");
//		cov1.getCoverageClaimDocTypes().get(1).setIsMandatory(true);
//		cov1.getCoverageClaimDocTypes().get(1).setClaimDocType(new ClaimDocType());
//		cov1.getCoverageClaimDocTypes().get(1).getClaimDocType().setClaimDocTypeId("DT002");
//		cov1.getCoverageClaimDocTypes().get(1).getClaimDocType().setUsageType(ClaimDocUsageType.REGULAR);
//
//		//coverage-2 with 1 optional KK, 1 optional regular
//		Coverage cov2 = new Coverage();
//		cov2.setCoverageId("102001");
//		cov2.setCoverageClaimDocTypes(new ArrayList<>());
//		cov2.getCoverageClaimDocTypes().add(new CoverageClaimDocType());
//		cov2.getCoverageClaimDocTypes().get(0).setClaimDocTypeId("DT001");
//		cov2.getCoverageClaimDocTypes().get(0).setIsMandatory(false);
//		cov2.getCoverageClaimDocTypes().get(0).setClaimDocType(new ClaimDocType());
//		cov2.getCoverageClaimDocTypes().get(0).getClaimDocType().setClaimDocTypeId("DT001");
//		cov2.getCoverageClaimDocTypes().get(0).getClaimDocType().setUsageType(ClaimDocUsageType.FAMILY_CARD);
//		cov2.getCoverageClaimDocTypes().add(new CoverageClaimDocType());
//		cov2.getCoverageClaimDocTypes().get(1).setClaimDocTypeId("DT003");
//		cov2.getCoverageClaimDocTypes().get(1).setIsMandatory(false);
//		cov2.getCoverageClaimDocTypes().get(1).setClaimDocType(new ClaimDocType());
//		cov2.getCoverageClaimDocTypes().get(1).getClaimDocType().setClaimDocTypeId("DT003");
//		cov2.getCoverageClaimDocTypes().get(1).getClaimDocType().setUsageType(ClaimDocUsageType.REGULAR);
//		
//		//test input
//		List<ClaimCoverageDto> dtos = new ArrayList<>();
//		dtos.add(new ClaimCoverageDto());
//		dtos.get(0).setCoverage(new CoverageDto());
//		dtos.get(0).getCoverage().setCoverageId("101001");
//		dtos.add(new ClaimCoverageDto());
//		dtos.get(1).setCoverage(new CoverageDto());
//		dtos.get(1).getCoverage().setCoverageId("102001");
//		
//		boolean isOrderHasFamily = false;
//		
//		//expected output: family is excluded
//		Map<String, Boolean> expectedResultMap = new HashMap<>();
//		expectedResultMap.put("DT002", true);
//		expectedResultMap.put("DT003", false);
//		
//		ApiClaimService claimService = new ApiClaimService();
//		claimService.productService = Mockito.mock(ProductService.class);
//		when(claimService.productService.fetchCoverageByCoverageId("101001")).thenReturn(cov1);
//		when(claimService.productService.fetchCoverageByCoverageId("102001")).thenReturn(cov2);
//		
//		Map<String, Boolean> resultMap = claimService.extractDocTypeMap(dtos, isOrderHasFamily);
//		
//		assertThat(resultMap, is(expectedResultMap));
//	}
}
