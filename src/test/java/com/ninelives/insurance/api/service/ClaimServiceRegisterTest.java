package com.ninelives.insurance.api.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.ninelives.insurance.api.dto.AccidentClaimDto;
import com.ninelives.insurance.api.dto.ClaimCoverageDto;
import com.ninelives.insurance.api.dto.ClaimDocTypeDto;
import com.ninelives.insurance.api.dto.ClaimDocumentDto;
import com.ninelives.insurance.api.dto.CoverageDto;
import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.UserFileDto;
import com.ninelives.insurance.api.exception.ApiException;
import com.ninelives.insurance.api.model.Coverage;
import com.ninelives.insurance.api.model.CoverageClaimDocType;
import com.ninelives.insurance.api.model.PolicyOrder;
import com.ninelives.insurance.api.model.PolicyOrderProduct;
import com.ninelives.insurance.api.ref.ErrorCode;
import com.ninelives.insurance.api.ref.PolicyStatus;

@RunWith(SpringRunner.class)
public class ClaimServiceRegisterTest {
	
	
	@Test(expected=ApiException.class)
	public void testMandatoryDocument() throws ApiException{
		String userId = "uid1";
		String orderId = "oid1";
		
		ClaimService claimService = new ClaimService();
		Coverage cov1 = new Coverage();
		cov1.setCoverageClaimDocTypes(new ArrayList<>());
		cov1.getCoverageClaimDocTypes().add(new CoverageClaimDocType());
		cov1.getCoverageClaimDocTypes().get(0).setClaimDocTypeId("DT001");
		cov1.getCoverageClaimDocTypes().get(0).setIsMandatory(true);
		cov1.getCoverageClaimDocTypes().add(new CoverageClaimDocType());
		cov1.getCoverageClaimDocTypes().get(1).setClaimDocTypeId("DT002");
		cov1.getCoverageClaimDocTypes().get(1).setIsMandatory(true);
		
		PolicyOrder order = new PolicyOrder();
		order.setOrderId(orderId);
		order.setStatus(PolicyStatus.ACTIVE);
		order.setPolicyOrderProducts(new ArrayList<>());
		order.getPolicyOrderProducts().add(new PolicyOrderProduct());
		order.getPolicyOrderProducts().get(0).setCoverageId("101001");
//		order.getPolicyOrderProducts().get(0).setCoverageClaimDocTypes(new ArrayList<CoverageClaimDocType>());
//		order.getPolicyOrderProducts().get(0).getCoverageClaimDocTypes().add(new CoverageClaimDocType());
//		order.getPolicyOrderProducts().get(0).getCoverageClaimDocTypes().get(0).setClaimDocTypeId("DT001");
//		order.getPolicyOrderProducts().get(0).getCoverageClaimDocTypes().get(0).setIsMandatory(true);
//		
		claimService.orderService = Mockito.mock(OrderService.class);
		when(claimService.orderService.fetchOrderByOrderId(userId, orderId)).thenReturn(order);
		claimService.productService = Mockito.mock(ProductService.class);
		when(claimService.productService.fetchCoverageByCoverageId("101001")).thenReturn(cov1);
			
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setOrder(new OrderDto());
		claimDto.getOrder().setOrderId(orderId);		
		claimDto.setClaimCoverages(new ArrayList<>());
		claimDto.getClaimCoverages().add(new ClaimCoverageDto());
		claimDto.getClaimCoverages().get(0).setCoverage(new CoverageDto());
		claimDto.getClaimCoverages().get(0).getCoverage().setCoverageId("101001");		
		claimDto.setClaimDocuments(new ArrayList<>());
		claimDto.getClaimDocuments().add(new ClaimDocumentDto());
		claimDto.getClaimDocuments().get(0).setClaimDocType(new ClaimDocTypeDto());
		claimDto.getClaimDocuments().get(0).getClaimDocType().setClaimDocTypeId("DT001");
		claimDto.getClaimDocuments().get(0).setFile(new UserFileDto());
		claimDto.getClaimDocuments().get(0).getFile().setFileId(101L);
		
		try{
			claimService.registerAccidentalClaim(userId, claimDto, true);
			
		}catch(ApiException e){
			assertThat(e.getCode(), is(ErrorCode.ERR7006_CLAIM_DOCUMENT_MANDATORY));
			throw new ApiException(ErrorCode.ERR1001_GENERIC_ERROR,"");
		}
		
	}

	@Test(expected=ApiException.class)
	public void testInvalidCoverage() throws ApiException{
		String userId = "uid1";
		String orderId = "oid1";
		
		ClaimService claimService = new ClaimService();
		
		PolicyOrder order = new PolicyOrder();
		order.setOrderId(orderId);
		order.setStatus(PolicyStatus.ACTIVE);
		order.setPolicyOrderProducts(new ArrayList<>());
		order.getPolicyOrderProducts().add(new PolicyOrderProduct());
		order.getPolicyOrderProducts().get(0).setCoverageId("101001");
		order.getPolicyOrderProducts().add(new PolicyOrderProduct());
		order.getPolicyOrderProducts().get(1).setCoverageId("101002");
		
		claimService.orderService = Mockito.mock(OrderService.class);
		when(claimService.orderService.fetchOrderByOrderId(userId, orderId)).thenReturn(order);
				
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setOrder(new OrderDto());
		claimDto.getOrder().setOrderId(orderId);
		
		claimDto.setClaimCoverages(new ArrayList<>());
		try{
			claimService.registerAccidentalClaim(userId, claimDto, true);
		}catch(ApiException e){
			assertThat(e.getCode(), is(ErrorCode.ERR7003_CLAIM_COVERAGE_INVALID));
			throw new ApiException(ErrorCode.ERR1001_GENERIC_ERROR,"");
		}
		
		claimDto.setClaimCoverages(new ArrayList<>());
		claimDto.getClaimCoverages().add(new ClaimCoverageDto());
		claimDto.getClaimCoverages().get(0).setCoverage(new CoverageDto());
		claimDto.getClaimCoverages().get(0).getCoverage().setCoverageId("101003");				
		try{
			claimService.registerAccidentalClaim(userId, claimDto, true);
		}catch(ApiException e){
			assertThat(e.getCode(), is(ErrorCode.ERR7003_CLAIM_COVERAGE_INVALID));
			throw new ApiException(ErrorCode.ERR1001_GENERIC_ERROR,"");
		}
				
	}
	
	@Test
	public void testInvalidOrderStatus(){
		String userId = "uid1";
		String orderId = "oid1";
		
		ClaimService claimService = new ClaimService();
		
		PolicyOrder order = new PolicyOrder();
		order.setOrderId(orderId);
		order.setPolicyOrderProducts(new ArrayList<>());
		order.getPolicyOrderProducts().add(new PolicyOrderProduct());
		order.getPolicyOrderProducts().get(0).setCoverageId("101001");
		order.getPolicyOrderProducts().add(new PolicyOrderProduct());
		order.getPolicyOrderProducts().get(1).setCoverageId("101002");
		
		claimService.orderService = Mockito.mock(OrderService.class);
		when(claimService.orderService.fetchOrderByOrderId(userId, orderId)).thenReturn(order);
				
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimCoverages(new ArrayList<>());
		claimDto.getClaimCoverages().add(new ClaimCoverageDto());
		claimDto.getClaimCoverages().get(0).setCoverage(new CoverageDto());
		claimDto.getClaimCoverages().get(0).getCoverage().setCoverageId("101003");
		claimDto.setOrder(new OrderDto());
		claimDto.getOrder().setOrderId(orderId);
		
		order.setStatus(PolicyStatus.SUBMITTED);		
		try{
			claimService.registerAccidentalClaim(userId, claimDto, true);
		}catch(ApiException e){
			assertThat(e.getCode(), is(ErrorCode.ERR7002_CLAIM_ORDER_INVALID));
		}
		
		order.setStatus(PolicyStatus.PAID);
		try{
			claimService.registerAccidentalClaim(userId, claimDto, true);
		}catch(ApiException e){
			assertThat(e.getCode(), is(ErrorCode.ERR7002_CLAIM_ORDER_INVALID));
		}
		
		order.setStatus(PolicyStatus.INPAYMENT);
		try{
			claimService.registerAccidentalClaim(userId, claimDto, true);
		}catch(ApiException e){
			assertThat(e.getCode(), is(ErrorCode.ERR7002_CLAIM_ORDER_INVALID));
		}
		
		order.setStatus(PolicyStatus.OVERDUE);
		try{
			claimService.registerAccidentalClaim(userId, claimDto, true);
		}catch(ApiException e){
			assertThat(e.getCode(), is(ErrorCode.ERR7002_CLAIM_ORDER_INVALID));
		}	
		
		order.setStatus(PolicyStatus.APPROVED);
		try{
			claimService.registerAccidentalClaim(userId, claimDto, true);
		}catch(ApiException e){
			assertThat(e.getCode(), is(ErrorCode.ERR7002_CLAIM_ORDER_INVALID));
		}
		
		order.setStatus(PolicyStatus.TERMINATED);
		try{
			claimService.registerAccidentalClaim(userId, claimDto, true);
		}catch(ApiException e){
			assertThat(e.getCode(), is(ErrorCode.ERR7002_CLAIM_ORDER_INVALID));
		}
		
		when(claimService.orderService.fetchOrderByOrderId(userId, orderId)).thenReturn(null);
		try{
			claimService.registerAccidentalClaim(userId, claimDto, true);
		}catch(ApiException e){
			assertThat(e.getCode(), is(ErrorCode.ERR7002_CLAIM_ORDER_INVALID));
		}
	}
}
