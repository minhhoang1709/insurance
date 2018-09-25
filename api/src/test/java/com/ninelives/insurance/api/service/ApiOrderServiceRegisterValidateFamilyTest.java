package com.ninelives.insurance.api.service;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.PolicyOrderFamilyDto;
import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.FamilyRelationship;
import com.ninelives.insurance.ref.Gender;

public class ApiOrderServiceRegisterValidateFamilyTest {
	ApiOrderService orderService;
	AppBadRequestException exp = null;
	
	@Before
	public void before(){
		orderService = new ApiOrderService();
		orderService.config = new NinelivesConfigProperties();
		exp = null;
	}
	
	@Test
	public void testEmptyFamily(){
		OrderDto submitOrderDto = new OrderDto();
		submitOrderDto.setPolicyStartDate(LocalDateTime.now());
		
		AppBadRequestException exp = null;
		try {
			orderService.validateFamilyMember("userIdA", submitOrderDto);
		} catch (AppBadRequestException e) {
			exp = e;
		}
		
		assertNotNull(exp);
		assertEquals(ErrorCode.ERR4021_ORDER_FAMILY_EMPTY, exp.getCode());
	}
	
	@Test
	public void testEmptyFamilyData(){
		OrderDto submitOrderDto = new OrderDto();
		submitOrderDto.setPolicyStartDate(LocalDateTime.now());
		
		submitOrderDto.setFamilies(new ArrayList<PolicyOrderFamilyDto>());
		submitOrderDto.getFamilies().add(new PolicyOrderFamilyDto());				
		
		try {
			orderService.validateFamilyMember("userIdA", submitOrderDto);
		} catch (AppBadRequestException e) {
			exp = e;
		}
		
		assertNotNull(exp);
		assertEquals(ErrorCode.ERR4022_ORDER_FAMILY_INVALID, exp.getCode());
	}
	
//	@Test
//	public void testChildAgeInvalid(){
//		OrderDto submitOrderDto = new OrderDto();
//		
//		submitOrderDto.setFamilies(new ArrayList<PolicyOrderFamilyDto>());
//		submitOrderDto.getFamilies().add(new PolicyOrderFamilyDto());
//		submitOrderDto.getFamilies().get(0).setName("test");
//		submitOrderDto.getFamilies().get(0).setBirthDate(LocalDateTime.now().minusYears(17));
//		submitOrderDto.getFamilies().get(0).setRelationship(FamilyRelationship.ANAK);
//		submitOrderDto.getFamilies().get(0).setGender(Gender.MALE);
//				
//		try {
//			orderService.validateFamilyMember("userIdA", submitOrderDto);
//		} catch (AppBadRequestException e) {
//			exp = e;
//		}
//		
//		assertNotNull(exp);
//		assertEquals(ErrorCode.ERR4023_ORDER_FAMILY_AGE_INVALID, exp.getCode());
//		
//	}
	
//	@Test
//	public void testChildAgeValid(){
//		OrderDto submitOrderDto = new OrderDto();
//		
//		submitOrderDto.setFamilies(new ArrayList<PolicyOrderFamilyDto>());
//		submitOrderDto.getFamilies().add(new PolicyOrderFamilyDto());
//		submitOrderDto.getFamilies().get(0).setName("test");
//		submitOrderDto.getFamilies().get(0).setBirthDate(LocalDateTime.now().minusYears(16));
//		submitOrderDto.getFamilies().get(0).setRelationship(FamilyRelationship.ANAK);
//		submitOrderDto.getFamilies().get(0).setGender(Gender.MALE);
//				
//		try {
//			orderService.validateFamilyMember("userIdA", submitOrderDto);
//		} catch (AppBadRequestException e) {
//			exp = e;
//		}
//		
//		assertNull(exp);
//		
//		submitOrderDto.getFamilies().get(0).setBirthDate(LocalDateTime.now().minusDays(1));
//		try {
//			orderService.validateFamilyMember("userIdA", submitOrderDto);
//		} catch (AppBadRequestException e) {
//			exp = e;
//		}
//		
//		assertNull(exp);
//		
//	}
	
//	@Test
//	public void testAdultAgeInvalidTooYoung(){
//		OrderDto submitOrderDto = new OrderDto();
//		
//		submitOrderDto.setFamilies(new ArrayList<PolicyOrderFamilyDto>());
//		submitOrderDto.getFamilies().add(new PolicyOrderFamilyDto());
//		submitOrderDto.getFamilies().get(0).setName("test");
//		submitOrderDto.getFamilies().get(0).setBirthDate(LocalDateTime.now().minusYears(16));
//		submitOrderDto.getFamilies().get(0).setRelationship(FamilyRelationship.ORANGTUA);
//		submitOrderDto.getFamilies().get(0).setGender(Gender.MALE);
//				
//		try {
//			orderService.validateFamilyMember("userIdA", submitOrderDto);
//		} catch (AppBadRequestException e) {
//			exp = e;
//		}
//		
//		assertNotNull(exp);
//		assertEquals(ErrorCode.ERR4023_ORDER_FAMILY_AGE_INVALID, exp.getCode());
//		
//	}
	
//	@Test
//	public void testAdultAgeInvalidTooOld(){
//		OrderDto submitOrderDto = new OrderDto();
//		
//		submitOrderDto.setFamilies(new ArrayList<PolicyOrderFamilyDto>());
//		submitOrderDto.getFamilies().add(new PolicyOrderFamilyDto());
//		submitOrderDto.getFamilies().get(0).setName("test");
//		submitOrderDto.getFamilies().get(0).setBirthDate(LocalDateTime.now().minusYears(84));
//		submitOrderDto.getFamilies().get(0).setRelationship(FamilyRelationship.SAUDARA);
//		submitOrderDto.getFamilies().get(0).setGender(Gender.MALE);
//				
//		try {
//			orderService.validateFamilyMember("userIdA", submitOrderDto);
//		} catch (AppBadRequestException e) {
//			exp = e;
//		}
//		
//		assertNotNull(exp);
//		assertEquals(ErrorCode.ERR4023_ORDER_FAMILY_AGE_INVALID, exp.getCode());
//		
//	}
	
//	@Test
//	public void testAdultAgeValid(){
//		OrderDto submitOrderDto = new OrderDto();
//		
//		submitOrderDto.setFamilies(new ArrayList<PolicyOrderFamilyDto>());
//		submitOrderDto.getFamilies().add(new PolicyOrderFamilyDto());
//		submitOrderDto.getFamilies().get(0).setName("test");
//		submitOrderDto.getFamilies().get(0).setBirthDate(LocalDateTime.now().minusYears(17));
//		submitOrderDto.getFamilies().get(0).setRelationship(FamilyRelationship.PASANGAN);
//		submitOrderDto.getFamilies().get(0).setGender(Gender.MALE);
//				
//		try {
//			orderService.validateFamilyMember("userIdA", submitOrderDto);
//		} catch (AppBadRequestException e) {
//			exp = e;
//		}
//		
//		assertNull(exp);
//		
//		submitOrderDto.getFamilies().get(0).setBirthDate(LocalDateTime.now().minusYears(75));
//		try {
//			orderService.validateFamilyMember("userIdA", submitOrderDto);
//		} catch (AppBadRequestException e) {
//			exp = e;
//		}
//		
//		assertNull(exp);
//		
//	}
	@Test
	public void testMinAgeInvalid(){
		OrderDto submitOrderDto = new OrderDto();
		submitOrderDto.setPolicyStartDate(LocalDateTime.now());
		
		submitOrderDto.setFamilies(new ArrayList<PolicyOrderFamilyDto>());
		submitOrderDto.getFamilies().add(new PolicyOrderFamilyDto());
		submitOrderDto.getFamilies().get(0).setName("test");
		submitOrderDto.getFamilies().get(0).setBirthDate(LocalDateTime.now().minusYears(1));
		submitOrderDto.getFamilies().get(0).setRelationship(FamilyRelationship.ORANGLAIN);
		submitOrderDto.getFamilies().get(0).setGender(Gender.MALE);				
		try {
			orderService.validateFamilyMember("userIdA", submitOrderDto);
		} catch (AppBadRequestException e) {
			exp = e;
		}
		
		assertNotNull(exp);
		assertEquals(ErrorCode.ERR4023_ORDER_FAMILY_AGE_INVALID, exp.getCode());
	}
	
	@Test
	public void testMaxAgeInvalid(){
		OrderDto submitOrderDto = new OrderDto();
		submitOrderDto.setPolicyStartDate(LocalDateTime.now());
		
		submitOrderDto.setFamilies(new ArrayList<PolicyOrderFamilyDto>());
		submitOrderDto.getFamilies().add(new PolicyOrderFamilyDto());
		submitOrderDto.getFamilies().get(0).setName("test");
		submitOrderDto.getFamilies().get(0).setBirthDate(LocalDateTime.now().minusYears(76));
		submitOrderDto.getFamilies().get(0).setRelationship(FamilyRelationship.ORANGLAIN);
		submitOrderDto.getFamilies().get(0).setGender(Gender.MALE);				
		try {
			orderService.validateFamilyMember("userIdA", submitOrderDto);
		} catch (AppBadRequestException e) {
			exp = e;
		}
		
		assertNotNull(exp);
		assertEquals(ErrorCode.ERR4023_ORDER_FAMILY_AGE_INVALID, exp.getCode());
	}
	
	@Test
	public void testMinorCountInvalid(){
		OrderDto submitOrderDto = new OrderDto();
		submitOrderDto.setPolicyStartDate(LocalDateTime.now());
		
		submitOrderDto.setFamilies(new ArrayList<PolicyOrderFamilyDto>());
		submitOrderDto.getFamilies().add(new PolicyOrderFamilyDto());
		submitOrderDto.getFamilies().get(0).setName("test");
		submitOrderDto.getFamilies().get(0).setBirthDate(LocalDateTime.now().minusYears(16));
		submitOrderDto.getFamilies().get(0).setRelationship(FamilyRelationship.ORANGLAIN);
		submitOrderDto.getFamilies().get(0).setGender(Gender.MALE);
		submitOrderDto.getFamilies().add(new PolicyOrderFamilyDto());
		submitOrderDto.getFamilies().get(1).setName("test");
		submitOrderDto.getFamilies().get(1).setBirthDate(LocalDateTime.now().minusYears(2));
		submitOrderDto.getFamilies().get(1).setRelationship(FamilyRelationship.ANAK);
		submitOrderDto.getFamilies().get(1).setGender(Gender.MALE);
		submitOrderDto.getFamilies().add(new PolicyOrderFamilyDto());
		submitOrderDto.getFamilies().get(2).setName("test");
		submitOrderDto.getFamilies().get(2).setBirthDate(LocalDateTime.now().minusYears(13));
		submitOrderDto.getFamilies().get(2).setRelationship(FamilyRelationship.ORANGTUA);
		submitOrderDto.getFamilies().get(2).setGender(Gender.MALE);
		submitOrderDto.getFamilies().add(new PolicyOrderFamilyDto());
		submitOrderDto.getFamilies().get(3).setName("test");
		submitOrderDto.getFamilies().get(3).setBirthDate(LocalDateTime.now().minusYears(15));
		submitOrderDto.getFamilies().get(3).setRelationship(FamilyRelationship.PASANGAN);
		submitOrderDto.getFamilies().get(3).setGender(Gender.MALE);
				
		try {
			orderService.validateFamilyMember("userIdA", submitOrderDto);
		} catch (AppBadRequestException e) {
			exp = e;
		}
		
		assertNotNull(exp);
		assertEquals(ErrorCode.ERR4024_ORDER_FAMILY_CNT_INVALID, exp.getCode());
	}
	
	@Test
	public void testAdultCountInvalid(){
		OrderDto submitOrderDto = new OrderDto();
		submitOrderDto.setPolicyStartDate(LocalDateTime.now());
		
		submitOrderDto.setFamilies(new ArrayList<PolicyOrderFamilyDto>());
		submitOrderDto.getFamilies().add(new PolicyOrderFamilyDto());
		submitOrderDto.getFamilies().get(0).setName("test");
		submitOrderDto.getFamilies().get(0).setBirthDate(LocalDateTime.now().minusYears(75));
		submitOrderDto.getFamilies().get(0).setRelationship(FamilyRelationship.ORANGTUA);
		submitOrderDto.getFamilies().get(0).setGender(Gender.MALE);
		submitOrderDto.getFamilies().add(new PolicyOrderFamilyDto());
		submitOrderDto.getFamilies().get(1).setName("test");
		submitOrderDto.getFamilies().get(1).setBirthDate(LocalDateTime.now().minusYears(17));
		submitOrderDto.getFamilies().get(1).setRelationship(FamilyRelationship.ANAK);
		submitOrderDto.getFamilies().get(1).setGender(Gender.MALE);
				
		try {
			orderService.validateFamilyMember("userIdA", submitOrderDto);
		} catch (AppBadRequestException e) {
			exp = e;
		}
		
		assertNotNull(exp);
		assertEquals(ErrorCode.ERR4024_ORDER_FAMILY_CNT_INVALID, exp.getCode());
	}
	
	@Test
	public void testFamilyCountValid(){
		OrderDto submitOrderDto = new OrderDto();
		submitOrderDto.setPolicyStartDate(LocalDateTime.now());
		
		submitOrderDto.setFamilies(new ArrayList<PolicyOrderFamilyDto>());
		submitOrderDto.getFamilies().add(new PolicyOrderFamilyDto());
		submitOrderDto.getFamilies().get(0).setName("test");
		submitOrderDto.getFamilies().get(0).setBirthDate(LocalDateTime.now().minusYears(75));
		submitOrderDto.getFamilies().get(0).setRelationship(FamilyRelationship.ORANGLAIN);
		submitOrderDto.getFamilies().get(0).setGender(Gender.MALE);
		submitOrderDto.getFamilies().add(new PolicyOrderFamilyDto());
		submitOrderDto.getFamilies().get(1).setName("test");
		submitOrderDto.getFamilies().get(1).setBirthDate(LocalDateTime.now().minusYears(12));
		submitOrderDto.getFamilies().get(1).setRelationship(FamilyRelationship.ANAK);
		submitOrderDto.getFamilies().get(1).setGender(Gender.MALE);
		submitOrderDto.getFamilies().add(new PolicyOrderFamilyDto());
		submitOrderDto.getFamilies().get(2).setName("test");
		submitOrderDto.getFamilies().get(2).setBirthDate(LocalDateTime.now().minusYears(13));
		submitOrderDto.getFamilies().get(2).setRelationship(FamilyRelationship.ORANGTUA);
		submitOrderDto.getFamilies().get(2).setGender(Gender.MALE);
		submitOrderDto.getFamilies().add(new PolicyOrderFamilyDto());
		submitOrderDto.getFamilies().get(3).setName("test");
		submitOrderDto.getFamilies().get(3).setBirthDate(LocalDateTime.now().minusYears(15));
		submitOrderDto.getFamilies().get(3).setRelationship(FamilyRelationship.PASANGAN);
		submitOrderDto.getFamilies().get(3).setGender(Gender.MALE);
				
		try {
			orderService.validateFamilyMember("userIdA", submitOrderDto);
		} catch (AppBadRequestException e) {
			exp = e;
		}
		
		assertNull(exp);
	}
}
