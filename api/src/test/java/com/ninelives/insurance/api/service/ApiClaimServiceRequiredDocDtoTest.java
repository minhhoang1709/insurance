package com.ninelives.insurance.api.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.ninelives.insurance.api.adapter.ModelMapperAdapter;
import com.ninelives.insurance.api.dto.AccidentClaimDto;
import com.ninelives.insurance.api.dto.ClaimCoverageDto;
import com.ninelives.insurance.api.dto.ClaimDocumentDto;
import com.ninelives.insurance.api.dto.ClaimDocumentExtraDto;
import com.ninelives.insurance.api.dto.CoverageDto;
import com.ninelives.insurance.api.dto.PolicyClaimFamilyDto;
import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.mybatis.mapper.TranslateMapper;
import com.ninelives.insurance.core.service.ClaimService;
import com.ninelives.insurance.core.service.ProductService;
import com.ninelives.insurance.core.service.TranslationService;
import com.ninelives.insurance.model.ClaimDocType;
import com.ninelives.insurance.model.Coverage;
import com.ninelives.insurance.model.CoverageClaimDocType;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderFamily;
import com.ninelives.insurance.ref.ClaimDocUsageType;
import com.ninelives.insurance.ref.CoverageCategoryId;
import com.ninelives.insurance.ref.FamilyRelationship;
import com.ninelives.insurance.ref.Gender;


/*
 * Case:
 *  v Non travel with 2 coverages, one with mandatory, one with optional
 *  v Travel with order no family, domestic
 *  v Travel with order family, domestic, 1 coverage lumpsum, family 1 adult 1 child, no family selection
 *  v Travel with order family, domestic, 1 coverage lumpsum 1 coverage is notlumpsum, family 1 adult 1 child, family selection 1 adult
 *  v Travel with order family, domestic, 1 coverage lumpsum 1 coverage is notlumpsum, family 1 adult 1 child, family selection 1 child
 *  v Travel with order family, domestic, 1 coverage notlumpsum, family order 1 adult 1 child, family selection 1 child
 *  v Travel with order family, domestic, 1 coverage notlumpsum, family order 1 adult 1 child, family selection 1 adult
 *  v Travel with order family, domestic, 1 coverage notlumpsum, family order 1 adult 1 child, no family selection
 *  - Travel with order family, international ...
 *  ...
 *  
 */
@RunWith(SpringRunner.class)
public class ApiClaimServiceRequiredDocDtoTest {
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	ApiClaimService claimService = new ApiClaimService();
	ModelMapperAdapter modelMapperAdapter = new ModelMapperAdapter();
	
	ClaimDocType claimDocTypeRegular1;
	ClaimDocType claimDocTypeRegular2;
	ClaimDocType claimDocTypeFamilyId;
	ClaimDocType claimDocTypeFamilyPassport;
	ClaimDocType claimDocTypeFamilyCard;

		
	/*
	 * Travel with order family, international, 1 coverage nonlumpsum, family 1 adult 1 child, no family selection
	 * expected: the regular doctype
	 */
	@Test
	public void testTravel_International_OrderFamily_CoverageNonLumpsum_NoClaimFamily(){
		//input
		List<ClaimCoverageDto> claimCoverageDtos = new ArrayList<>();
		claimCoverageDtos.add(new ClaimCoverageDto());
		claimCoverageDtos.get(0).setCoverage(new CoverageDto());
		claimCoverageDtos.get(0).getCoverage().setCoverageId("101001");
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimCoverages(claimCoverageDtos);
		
		PolicyOrder order = new PolicyOrder();
		order.setCoverageCategoryId(CoverageCategoryId.TRAVEL_INTERNATIONAL);
		order.setIsFamily(true);
		order.setPolicyStartDate(LocalDate.parse("2018-10-30",dateFormatter));
		order.setPolicyOrderFamilies(new ArrayList<PolicyOrderFamily>());
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(0).setSubId(1);
		order.getPolicyOrderFamilies().get(0).setName("Suami 1");
		order.getPolicyOrderFamilies().get(0).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(0).setRelationship(FamilyRelationship.PASANGAN);
		order.getPolicyOrderFamilies().get(0).setBirthDate(LocalDate.parse("2000-01-01",dateFormatter));
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(1).setSubId(2);
		order.getPolicyOrderFamilies().get(1).setName("Anak 1");
		order.getPolicyOrderFamilies().get(1).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(1).setRelationship(FamilyRelationship.ANAK);
		order.getPolicyOrderFamilies().get(1).setBirthDate(LocalDate.parse("2010-01-01",dateFormatter));
			
		//expected
		List<ClaimDocumentDto> expectedResult = new ArrayList<>();
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		expectedResult.get(0).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		expectedResult.get(1).setIsMandatory(false);
		
		List<ClaimDocumentDto> result = claimService.requiredClaimDocumentDtos(claimDto, order);
		
		assertThat(result, is(expectedResult));			
	}
	
	/*
	 * Travel with order family, international, 1 coverage nonlumpsum, family 1 adult 1 child, family selection adult
	 * expected: required kk and pasport for 1 adult plus the regular doctype
	 */
	@Test
	public void testTravel_International_OrderFamily_CoverageNonLumpsum_ClaimFamilyAdult(){
		//input
		List<ClaimCoverageDto> claimCoverageDtos = new ArrayList<>();
		claimCoverageDtos.add(new ClaimCoverageDto());
		claimCoverageDtos.get(0).setCoverage(new CoverageDto());
		claimCoverageDtos.get(0).getCoverage().setCoverageId("101001");
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimCoverages(claimCoverageDtos);
		
		PolicyOrder order = new PolicyOrder();
		order.setCoverageCategoryId(CoverageCategoryId.TRAVEL_INTERNATIONAL);
		order.setIsFamily(true);
		order.setPolicyStartDate(LocalDate.parse("2018-10-30",dateFormatter));
		order.setPolicyOrderFamilies(new ArrayList<PolicyOrderFamily>());
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(0).setSubId(1);
		order.getPolicyOrderFamilies().get(0).setName("Suami 1");
		order.getPolicyOrderFamilies().get(0).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(0).setRelationship(FamilyRelationship.PASANGAN);
		order.getPolicyOrderFamilies().get(0).setBirthDate(LocalDate.parse("2000-01-01",dateFormatter));
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(1).setSubId(2);
		order.getPolicyOrderFamilies().get(1).setName("Anak 1");
		order.getPolicyOrderFamilies().get(1).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(1).setRelationship(FamilyRelationship.ANAK);
		order.getPolicyOrderFamilies().get(1).setBirthDate(LocalDate.parse("2010-01-01",dateFormatter));
		
		claimDto.setFamilies(new ArrayList<PolicyClaimFamilyDto>());
		claimDto.getFamilies().add(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(0))); //claimant is adult
		
		//expected
		List<ClaimDocumentDto> expectedResult = new ArrayList<>();
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyPassport));
		expectedResult.get(0).setIsMandatory(true);
		expectedResult.get(0).setExtra(new ClaimDocumentExtraDto());
		expectedResult.get(0).getExtra().setFamily(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(0))); //the adult
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyCard));
		expectedResult.get(1).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(2).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		expectedResult.get(2).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(3).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		expectedResult.get(3).setIsMandatory(false);
		
		List<ClaimDocumentDto> result = claimService.requiredClaimDocumentDtos(claimDto, order);
		
		assertThat(result, is(expectedResult));				
	}
	
	/*
	 * Travel with order family, international, 1 coverage nonlumpsum, family 1 adult 1 child, family selection child
	 * expected: required kk and pasport for 1 child plus the regular doctype
	 */
	@Test
	public void testTravel_International_OrderFamily_CoverageNonLumpsum_ClaimFamilyChild(){
		//input
		List<ClaimCoverageDto> claimCoverageDtos = new ArrayList<>();
		claimCoverageDtos.add(new ClaimCoverageDto());
		claimCoverageDtos.get(0).setCoverage(new CoverageDto());
		claimCoverageDtos.get(0).getCoverage().setCoverageId("101001");
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimCoverages(claimCoverageDtos);
		
		PolicyOrder order = new PolicyOrder();
		order.setCoverageCategoryId(CoverageCategoryId.TRAVEL_INTERNATIONAL);
		order.setIsFamily(true);
		order.setPolicyStartDate(LocalDate.parse("2018-10-30",dateFormatter));
		order.setPolicyOrderFamilies(new ArrayList<PolicyOrderFamily>());
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(0).setSubId(1);
		order.getPolicyOrderFamilies().get(0).setName("Suami 1");
		order.getPolicyOrderFamilies().get(0).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(0).setRelationship(FamilyRelationship.PASANGAN);
		order.getPolicyOrderFamilies().get(0).setBirthDate(LocalDate.parse("2000-01-01",dateFormatter));
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(1).setSubId(2);
		order.getPolicyOrderFamilies().get(1).setName("Anak 1");
		order.getPolicyOrderFamilies().get(1).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(1).setRelationship(FamilyRelationship.ANAK);
		order.getPolicyOrderFamilies().get(1).setBirthDate(LocalDate.parse("2010-01-01",dateFormatter));
		
		claimDto.setFamilies(new ArrayList<PolicyClaimFamilyDto>());
		claimDto.getFamilies().add(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(1))); //claimant is child
		
		//expected
		List<ClaimDocumentDto> expectedResult = new ArrayList<>();
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyPassport));
		expectedResult.get(0).setIsMandatory(true);
		expectedResult.get(0).setExtra(new ClaimDocumentExtraDto());
		expectedResult.get(0).getExtra().setFamily(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(1))); //the child
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyCard));
		expectedResult.get(1).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(2).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		expectedResult.get(2).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(3).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		expectedResult.get(3).setIsMandatory(false);
		
		List<ClaimDocumentDto> result = claimService.requiredClaimDocumentDtos(claimDto, order);
		
		assertThat(result, is(expectedResult));			
	}
	
	/*
	 * Travel with order family, international, 1 coverage lumpsum + 1 nonlumpsum, family 1 adult 1 child, no family selection
	 * expected: required kk and pasport for 1 adult and for 1 child plus the regular doctype
	 */
	@Test
	public void testTravel_International_OrderFamily_CoverageLumpsumAndNonLumpsum_NoClaimFamily(){
		//input
		List<ClaimCoverageDto> claimCoverageDtos = new ArrayList<>();
		claimCoverageDtos.add(new ClaimCoverageDto());
		claimCoverageDtos.get(0).setCoverage(new CoverageDto());
		claimCoverageDtos.get(0).getCoverage().setCoverageId("103001");
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimCoverages(claimCoverageDtos);
		
		PolicyOrder order = new PolicyOrder();
		order.setCoverageCategoryId(CoverageCategoryId.TRAVEL_INTERNATIONAL);
		order.setIsFamily(true);
		order.setPolicyStartDate(LocalDate.parse("2018-10-30",dateFormatter));
		order.setPolicyOrderFamilies(new ArrayList<PolicyOrderFamily>());
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(0).setSubId(1);
		order.getPolicyOrderFamilies().get(0).setName("Suami 1");
		order.getPolicyOrderFamilies().get(0).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(0).setRelationship(FamilyRelationship.PASANGAN);
		order.getPolicyOrderFamilies().get(0).setBirthDate(LocalDate.parse("2000-01-01",dateFormatter));
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(1).setSubId(2);
		order.getPolicyOrderFamilies().get(1).setName("Anak 1");
		order.getPolicyOrderFamilies().get(1).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(1).setRelationship(FamilyRelationship.ANAK);
		order.getPolicyOrderFamilies().get(1).setBirthDate(LocalDate.parse("2010-01-01",dateFormatter));
		
		//expected
		List<ClaimDocumentDto> expectedResult = new ArrayList<>();
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyPassport));
		expectedResult.get(0).setIsMandatory(true);
		expectedResult.get(0).setExtra(new ClaimDocumentExtraDto());
		expectedResult.get(0).getExtra().setFamily(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(0))); //the adult
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyPassport));
		expectedResult.get(1).setIsMandatory(true);
		expectedResult.get(1).setExtra(new ClaimDocumentExtraDto());
		expectedResult.get(1).getExtra().setFamily(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(1))); //the child
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(2).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyCard));
		expectedResult.get(2).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(3).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		expectedResult.get(3).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(4).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		expectedResult.get(4).setIsMandatory(false);
		
		List<ClaimDocumentDto> result = claimService.requiredClaimDocumentDtos(claimDto, order);
		
		assertThat(result, is(expectedResult));		
	}
	
	/*
	 * Travel with order family, international, 1 coverage lumpsum + 1 nonlumpsum, family 1 adult 1 child, family selection adult
	 * expected: required kk and pasport for 1 adult and for 1 child plus the regular doctype
	 */
	@Test
	public void testTravel_International_OrderFamily_CoverageLumpsumAndNonLumpsum_ClaimFamilyAdult(){
		//input
		List<ClaimCoverageDto> claimCoverageDtos = new ArrayList<>();
		claimCoverageDtos.add(new ClaimCoverageDto());
		claimCoverageDtos.get(0).setCoverage(new CoverageDto());
		claimCoverageDtos.get(0).getCoverage().setCoverageId("103001");
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimCoverages(claimCoverageDtos);
		
		PolicyOrder order = new PolicyOrder();
		order.setCoverageCategoryId(CoverageCategoryId.TRAVEL_INTERNATIONAL);
		order.setIsFamily(true);
		order.setPolicyStartDate(LocalDate.parse("2018-10-30",dateFormatter));
		order.setPolicyOrderFamilies(new ArrayList<PolicyOrderFamily>());
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(0).setSubId(1);
		order.getPolicyOrderFamilies().get(0).setName("Suami 1");
		order.getPolicyOrderFamilies().get(0).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(0).setRelationship(FamilyRelationship.PASANGAN);
		order.getPolicyOrderFamilies().get(0).setBirthDate(LocalDate.parse("2000-01-01",dateFormatter));
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(1).setSubId(2);
		order.getPolicyOrderFamilies().get(1).setName("Anak 1");
		order.getPolicyOrderFamilies().get(1).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(1).setRelationship(FamilyRelationship.ANAK);
		order.getPolicyOrderFamilies().get(1).setBirthDate(LocalDate.parse("2010-01-01",dateFormatter));
		
		claimDto.setFamilies(new ArrayList<PolicyClaimFamilyDto>());
		claimDto.getFamilies().add(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(0))); //adult
		
		//expected
		List<ClaimDocumentDto> expectedResult = new ArrayList<>();
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyPassport));
		expectedResult.get(0).setIsMandatory(true);
		expectedResult.get(0).setExtra(new ClaimDocumentExtraDto());
		expectedResult.get(0).getExtra().setFamily(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(0))); //the adult
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyPassport));
		expectedResult.get(1).setIsMandatory(true);
		expectedResult.get(1).setExtra(new ClaimDocumentExtraDto());
		expectedResult.get(1).getExtra().setFamily(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(1))); //the child
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(2).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyCard));
		expectedResult.get(2).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(3).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		expectedResult.get(3).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(4).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		expectedResult.get(4).setIsMandatory(false);
		
		List<ClaimDocumentDto> result = claimService.requiredClaimDocumentDtos(claimDto, order);
		
		assertThat(result, is(expectedResult));
	}

	/*
	 * Travel with order family, international, 1 coverage lumpsum + 1 nonlumpsum, family 1 adult 1 child, family selection child
	 * expected: required kk and pasport for 1 adult and for 1 child plus the regular doctype
	 */
	@Test
	public void testTravel_International_OrderFamily_CoverageLumpsumAndNonLumpsum_ClaimFamilyChild(){
		//input
		List<ClaimCoverageDto> claimCoverageDtos = new ArrayList<>();
		claimCoverageDtos.add(new ClaimCoverageDto());
		claimCoverageDtos.get(0).setCoverage(new CoverageDto());
		claimCoverageDtos.get(0).getCoverage().setCoverageId("103001");
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimCoverages(claimCoverageDtos);
		
		PolicyOrder order = new PolicyOrder();
		order.setCoverageCategoryId(CoverageCategoryId.TRAVEL_INTERNATIONAL);
		order.setIsFamily(true);
		order.setPolicyStartDate(LocalDate.parse("2018-10-30",dateFormatter));
		order.setPolicyOrderFamilies(new ArrayList<PolicyOrderFamily>());
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(0).setSubId(1);
		order.getPolicyOrderFamilies().get(0).setName("Suami 1");
		order.getPolicyOrderFamilies().get(0).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(0).setRelationship(FamilyRelationship.PASANGAN);
		order.getPolicyOrderFamilies().get(0).setBirthDate(LocalDate.parse("2000-01-01",dateFormatter));
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(1).setSubId(2);
		order.getPolicyOrderFamilies().get(1).setName("Anak 1");
		order.getPolicyOrderFamilies().get(1).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(1).setRelationship(FamilyRelationship.ANAK);
		order.getPolicyOrderFamilies().get(1).setBirthDate(LocalDate.parse("2010-01-01",dateFormatter));
		
		claimDto.setFamilies(new ArrayList<PolicyClaimFamilyDto>());
		claimDto.getFamilies().add(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(1))); //claimant is child
		
		//expected
		List<ClaimDocumentDto> expectedResult = new ArrayList<>();
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyPassport));
		expectedResult.get(0).setIsMandatory(true);
		expectedResult.get(0).setExtra(new ClaimDocumentExtraDto());
		expectedResult.get(0).getExtra().setFamily(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(0))); //the adult
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyPassport));
		expectedResult.get(1).setIsMandatory(true);
		expectedResult.get(1).setExtra(new ClaimDocumentExtraDto());
		expectedResult.get(1).getExtra().setFamily(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(1))); //the child
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(2).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyCard));
		expectedResult.get(2).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(3).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		expectedResult.get(3).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(4).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		expectedResult.get(4).setIsMandatory(false);
		
		List<ClaimDocumentDto> result = claimService.requiredClaimDocumentDtos(claimDto, order);
		
		assertThat(result, is(expectedResult));		
	}
	
	/*
	 * Travel with order family, international, 1 coverage lumpsum, family 1 adult 1 child, no family selection
	 * expected: required kk and pasport for 1 adult and for 1 child plus the regular doctype
	 */
	@Test
	public void testTravel_International_OrderFamily_CoverageLumpsum_NoClaimFamily(){
		//input
		List<ClaimCoverageDto> claimCoverageDtos = new ArrayList<>();
		claimCoverageDtos.add(new ClaimCoverageDto());
		claimCoverageDtos.get(0).setCoverage(new CoverageDto());
		claimCoverageDtos.get(0).getCoverage().setCoverageId("103001");
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimCoverages(claimCoverageDtos);
		
		PolicyOrder order = new PolicyOrder();
		order.setCoverageCategoryId(CoverageCategoryId.TRAVEL_INTERNATIONAL);
		order.setIsFamily(true);
		order.setPolicyStartDate(LocalDate.parse("2018-10-30",dateFormatter));
		order.setPolicyOrderFamilies(new ArrayList<PolicyOrderFamily>());
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(0).setSubId(1);
		order.getPolicyOrderFamilies().get(0).setName("Suami 1");
		order.getPolicyOrderFamilies().get(0).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(0).setRelationship(FamilyRelationship.PASANGAN);
		order.getPolicyOrderFamilies().get(0).setBirthDate(LocalDate.parse("2000-01-01",dateFormatter));
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(1).setSubId(2);
		order.getPolicyOrderFamilies().get(1).setName("Anak 1");
		order.getPolicyOrderFamilies().get(1).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(1).setRelationship(FamilyRelationship.ANAK);
		order.getPolicyOrderFamilies().get(1).setBirthDate(LocalDate.parse("2010-01-01",dateFormatter));
		
		//expected
		List<ClaimDocumentDto> expectedResult = new ArrayList<>();
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyPassport));
		expectedResult.get(0).setIsMandatory(true);
		expectedResult.get(0).setExtra(new ClaimDocumentExtraDto());
		expectedResult.get(0).getExtra().setFamily(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(0))); //the adult
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyPassport));
		expectedResult.get(1).setIsMandatory(true);
		expectedResult.get(1).setExtra(new ClaimDocumentExtraDto());
		expectedResult.get(1).getExtra().setFamily(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(1))); //the child
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(2).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyCard));
		expectedResult.get(2).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(3).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		expectedResult.get(3).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(4).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		expectedResult.get(4).setIsMandatory(false);
		
		List<ClaimDocumentDto> result = claimService.requiredClaimDocumentDtos(claimDto, order);
		
		assertThat(result, is(expectedResult));
	}
	
	/*
	 * Travel with order family, domestic, coverage not lumpsum, family 1 adult 1 child, no family selection
	 * expected: required the regular doctype
	 */
	@Test
	public void testTravel_Domestic_OrderFamily_CoverageNonLumpsum_NoClaimFamily(){
		//input
		List<ClaimCoverageDto> claimCoverageDtos = new ArrayList<>();
		claimCoverageDtos.add(new ClaimCoverageDto());
		claimCoverageDtos.get(0).setCoverage(new CoverageDto());
		claimCoverageDtos.get(0).getCoverage().setCoverageId("101001");
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimCoverages(claimCoverageDtos);
		
		PolicyOrder order = new PolicyOrder();
		order.setCoverageCategoryId(CoverageCategoryId.TRAVEL_DOMESTIC);
		order.setIsFamily(true);
		order.setPolicyStartDate(LocalDate.parse("2018-10-30",dateFormatter));
		order.setPolicyOrderFamilies(new ArrayList<PolicyOrderFamily>());
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(0).setSubId(1);
		order.getPolicyOrderFamilies().get(0).setName("Suami 1");
		order.getPolicyOrderFamilies().get(0).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(0).setRelationship(FamilyRelationship.PASANGAN);
		order.getPolicyOrderFamilies().get(0).setBirthDate(LocalDate.parse("2000-01-01",dateFormatter));
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(1).setSubId(2);
		order.getPolicyOrderFamilies().get(1).setName("Anak 1");
		order.getPolicyOrderFamilies().get(1).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(1).setRelationship(FamilyRelationship.ANAK);
		order.getPolicyOrderFamilies().get(1).setBirthDate(LocalDate.parse("2010-01-01",dateFormatter));
		
		//expected
		List<ClaimDocumentDto> expectedResult = new ArrayList<>();
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		expectedResult.get(0).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		expectedResult.get(1).setIsMandatory(false);
		
		List<ClaimDocumentDto> result = claimService.requiredClaimDocumentDtos(claimDto, order);
		
		assertThat(result, is(expectedResult));
	}
	
	/*
	 * Travel with order family, domestic, coverage not lumpsum, family 1 adult 1 child, family selection 1 adult
	 * expected: required kk + ktp for 1 adult + the regular doctype
	 */
	@Test
	public void testTravel_Domestic_OrderFamily_CoverageNonLumpsum_ClaimFamilyAdult(){
		//input
		List<ClaimCoverageDto> claimCoverageDtos = new ArrayList<>();
		claimCoverageDtos.add(new ClaimCoverageDto());
		claimCoverageDtos.get(0).setCoverage(new CoverageDto());
		claimCoverageDtos.get(0).getCoverage().setCoverageId("101001");
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimCoverages(claimCoverageDtos);
		
		PolicyOrder order = new PolicyOrder();
		order.setCoverageCategoryId(CoverageCategoryId.TRAVEL_DOMESTIC);
		order.setIsFamily(true);
		order.setPolicyStartDate(LocalDate.parse("2018-10-30",dateFormatter));
		order.setPolicyOrderFamilies(new ArrayList<PolicyOrderFamily>());
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(0).setSubId(1);
		order.getPolicyOrderFamilies().get(0).setName("Suami 1");
		order.getPolicyOrderFamilies().get(0).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(0).setRelationship(FamilyRelationship.PASANGAN);
		order.getPolicyOrderFamilies().get(0).setBirthDate(LocalDate.parse("2000-01-01",dateFormatter));
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(1).setSubId(2);
		order.getPolicyOrderFamilies().get(1).setName("Anak 1");
		order.getPolicyOrderFamilies().get(1).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(1).setRelationship(FamilyRelationship.ANAK);
		order.getPolicyOrderFamilies().get(1).setBirthDate(LocalDate.parse("2010-01-01",dateFormatter));
		
		claimDto.setFamilies(new ArrayList<PolicyClaimFamilyDto>());
		claimDto.getFamilies().add(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(0))); //claimant is adult
		
		//expected
		List<ClaimDocumentDto> expectedResult = new ArrayList<>();
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyId));
		expectedResult.get(0).setIsMandatory(true);
		expectedResult.get(0).setExtra(new ClaimDocumentExtraDto());
		expectedResult.get(0).getExtra().setFamily(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(0))); //the adult
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyCard));
		expectedResult.get(1).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(2).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		expectedResult.get(2).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(3).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		expectedResult.get(3).setIsMandatory(false);
		
		List<ClaimDocumentDto> result = claimService.requiredClaimDocumentDtos(claimDto, order);
		
		assertThat(result, is(expectedResult));
	}
	

	
	/*
	 * Travel with order family, domestic, coverage not lumpsum, family 1 adult 1 child, family selection 1 child
	 * expected: required kk + the regular doctype (no ktp)
	 */
	@Test
	public void testTravel_Domestic_OrderFamily_CoverageNonLumpsum_ClaimFamilyChild(){
		//input
		List<ClaimCoverageDto> claimCoverageDtos = new ArrayList<>();
		claimCoverageDtos.add(new ClaimCoverageDto());
		claimCoverageDtos.get(0).setCoverage(new CoverageDto());
		claimCoverageDtos.get(0).getCoverage().setCoverageId("101001");
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimCoverages(claimCoverageDtos);
		
		PolicyOrder order = new PolicyOrder();
		order.setCoverageCategoryId(CoverageCategoryId.TRAVEL_DOMESTIC);
		order.setIsFamily(true);
		order.setPolicyStartDate(LocalDate.parse("2018-10-30",dateFormatter));
		order.setPolicyOrderFamilies(new ArrayList<PolicyOrderFamily>());
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(0).setSubId(1);
		order.getPolicyOrderFamilies().get(0).setName("Suami 1");
		order.getPolicyOrderFamilies().get(0).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(0).setRelationship(FamilyRelationship.PASANGAN);
		order.getPolicyOrderFamilies().get(0).setBirthDate(LocalDate.parse("2000-01-01",dateFormatter));
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(1).setSubId(2);
		order.getPolicyOrderFamilies().get(1).setName("Anak 1");
		order.getPolicyOrderFamilies().get(1).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(1).setRelationship(FamilyRelationship.ANAK);
		order.getPolicyOrderFamilies().get(1).setBirthDate(LocalDate.parse("2010-01-01",dateFormatter));
		
		claimDto.setFamilies(new ArrayList<PolicyClaimFamilyDto>());
		claimDto.getFamilies().add(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(1))); //claimant is child
		
		//expected
		List<ClaimDocumentDto> expectedResult = new ArrayList<>();
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyCard));
		expectedResult.get(0).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		expectedResult.get(1).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(2).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		expectedResult.get(2).setIsMandatory(false);
		
		List<ClaimDocumentDto> result = claimService.requiredClaimDocumentDtos(claimDto, order);
		
		assertThat(result, is(expectedResult));
	}
	
	/*
	 * Travel with order family, domestic, 1 coverage lumpsum, family 1 adult 1 child, family selection adult
	 * expected: required kk and ktp for 1 adult plus the regular doctype
	 */
	@Test
	public void testTravel_Domestic_OrderFamily_CoverageLumpsumAndNonLumpsum_NoClaimFamily(){
		//input
		List<ClaimCoverageDto> claimCoverageDtos = new ArrayList<>();
		claimCoverageDtos.add(new ClaimCoverageDto());
		claimCoverageDtos.get(0).setCoverage(new CoverageDto());
		claimCoverageDtos.get(0).getCoverage().setCoverageId("103001");
		claimCoverageDtos.add(new ClaimCoverageDto());
		claimCoverageDtos.get(1).setCoverage(new CoverageDto());
		claimCoverageDtos.get(1).getCoverage().setCoverageId("101001");
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimCoverages(claimCoverageDtos);
		
		PolicyOrder order = new PolicyOrder();
		order.setCoverageCategoryId(CoverageCategoryId.TRAVEL_DOMESTIC);
		order.setIsFamily(true);
		order.setPolicyStartDate(LocalDate.parse("2018-10-30",dateFormatter));
		order.setPolicyOrderFamilies(new ArrayList<PolicyOrderFamily>());
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(0).setSubId(1);
		order.getPolicyOrderFamilies().get(0).setName("Suami 1");
		order.getPolicyOrderFamilies().get(0).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(0).setRelationship(FamilyRelationship.PASANGAN);
		order.getPolicyOrderFamilies().get(0).setBirthDate(LocalDate.parse("2000-01-01",dateFormatter));
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(1).setSubId(2);
		order.getPolicyOrderFamilies().get(1).setName("Anak 1");
		order.getPolicyOrderFamilies().get(1).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(1).setRelationship(FamilyRelationship.ANAK);
		order.getPolicyOrderFamilies().get(1).setBirthDate(LocalDate.parse("2010-01-01",dateFormatter));
			
		//expected
		List<ClaimDocumentDto> expectedResult = new ArrayList<>();
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyId));
		expectedResult.get(0).setIsMandatory(true);
		expectedResult.get(0).setExtra(new ClaimDocumentExtraDto());
		expectedResult.get(0).getExtra().setFamily(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(0))); //the adult
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyCard));
		expectedResult.get(1).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(2).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		expectedResult.get(2).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(3).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		expectedResult.get(3).setIsMandatory(false);
		
		List<ClaimDocumentDto> result = claimService.requiredClaimDocumentDtos(claimDto, order);
		
		assertThat(result, is(expectedResult));
	}
	
	/*
	 * Travel with order family, domestic, 1 coverage lumpsum, family 1 adult 1 child, family selection adult
	 * expected: required kk and ktp for 1 adult plus the regular doctype
	 */
	@Test
	public void testTravel_Domestic_OrderFamily_CoverageLumpsumAndNonLumpsum_ClaimFamilyAdult(){
		//input
		List<ClaimCoverageDto> claimCoverageDtos = new ArrayList<>();
		claimCoverageDtos.add(new ClaimCoverageDto());
		claimCoverageDtos.get(0).setCoverage(new CoverageDto());
		claimCoverageDtos.get(0).getCoverage().setCoverageId("103001");
		claimCoverageDtos.add(new ClaimCoverageDto());
		claimCoverageDtos.get(1).setCoverage(new CoverageDto());
		claimCoverageDtos.get(1).getCoverage().setCoverageId("101001");
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimCoverages(claimCoverageDtos);
		
		PolicyOrder order = new PolicyOrder();
		order.setCoverageCategoryId(CoverageCategoryId.TRAVEL_DOMESTIC);
		order.setIsFamily(true);
		order.setPolicyStartDate(LocalDate.parse("2018-10-30",dateFormatter));
		order.setPolicyOrderFamilies(new ArrayList<PolicyOrderFamily>());
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(0).setSubId(1);
		order.getPolicyOrderFamilies().get(0).setName("Suami 1");
		order.getPolicyOrderFamilies().get(0).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(0).setRelationship(FamilyRelationship.PASANGAN);
		order.getPolicyOrderFamilies().get(0).setBirthDate(LocalDate.parse("2000-01-01",dateFormatter));
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(1).setSubId(2);
		order.getPolicyOrderFamilies().get(1).setName("Anak 1");
		order.getPolicyOrderFamilies().get(1).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(1).setRelationship(FamilyRelationship.ANAK);
		order.getPolicyOrderFamilies().get(1).setBirthDate(LocalDate.parse("2010-01-01",dateFormatter));
		
		claimDto.setFamilies(new ArrayList<PolicyClaimFamilyDto>());
		claimDto.getFamilies().add(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(0))); //adult
		
		//expected
		List<ClaimDocumentDto> expectedResult = new ArrayList<>();
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyId));
		expectedResult.get(0).setIsMandatory(true);
		expectedResult.get(0).setExtra(new ClaimDocumentExtraDto());
		expectedResult.get(0).getExtra().setFamily(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(0))); //the adult
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyCard));
		expectedResult.get(1).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(2).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		expectedResult.get(2).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(3).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		expectedResult.get(3).setIsMandatory(false);
		
		List<ClaimDocumentDto> result = claimService.requiredClaimDocumentDtos(claimDto, order);
		
		assertThat(result, is(expectedResult));
	}
	
	/*
	 * Travel with order family, domestic, 1 coverage lumpsum, family 1 adult 1 child, family selection child
	 * expected: required kk and ktp for 1 adult plus the regular doctype
	 */
	@Test
	public void testTravel_Domestic_OrderFamily_CoverageLumpsumAndNonLumpsum_ClaimFamilyChild(){
		//input
		List<ClaimCoverageDto> claimCoverageDtos = new ArrayList<>();
		claimCoverageDtos.add(new ClaimCoverageDto());
		claimCoverageDtos.get(0).setCoverage(new CoverageDto());
		claimCoverageDtos.get(0).getCoverage().setCoverageId("103001");
		claimCoverageDtos.add(new ClaimCoverageDto());
		claimCoverageDtos.get(1).setCoverage(new CoverageDto());
		claimCoverageDtos.get(1).getCoverage().setCoverageId("101001");
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimCoverages(claimCoverageDtos);
		
		PolicyOrder order = new PolicyOrder();
		order.setCoverageCategoryId(CoverageCategoryId.TRAVEL_DOMESTIC);
		order.setIsFamily(true);
		order.setPolicyStartDate(LocalDate.parse("2018-10-30",dateFormatter));
		order.setPolicyOrderFamilies(new ArrayList<PolicyOrderFamily>());
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(0).setSubId(1);
		order.getPolicyOrderFamilies().get(0).setName("Suami 1");
		order.getPolicyOrderFamilies().get(0).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(0).setRelationship(FamilyRelationship.PASANGAN);
		order.getPolicyOrderFamilies().get(0).setBirthDate(LocalDate.parse("2000-01-01",dateFormatter));
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(1).setSubId(2);
		order.getPolicyOrderFamilies().get(1).setName("Anak 1");
		order.getPolicyOrderFamilies().get(1).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(1).setRelationship(FamilyRelationship.ANAK);
		order.getPolicyOrderFamilies().get(1).setBirthDate(LocalDate.parse("2010-01-01",dateFormatter));
		
		claimDto.setFamilies(new ArrayList<PolicyClaimFamilyDto>());
		claimDto.getFamilies().add(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(1))); //claimant is child
		
		//expected
		List<ClaimDocumentDto> expectedResult = new ArrayList<>();
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyId));
		expectedResult.get(0).setIsMandatory(true);
		expectedResult.get(0).setExtra(new ClaimDocumentExtraDto());
		expectedResult.get(0).getExtra().setFamily(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(0))); //the adult
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyCard));
		expectedResult.get(1).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(2).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		expectedResult.get(2).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(3).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		expectedResult.get(3).setIsMandatory(false);
		
		List<ClaimDocumentDto> result = claimService.requiredClaimDocumentDtos(claimDto, order);
		
		assertThat(result, is(expectedResult));
	}
	
	/*
	 * Travel with order family, domestic, 1 coverage lumpsum, family 1 adult 1 child, no family selection
	 * expected: required kk and ktp for 1 adult plus the regular doctype
	 */
	@Test
	public void testTravel_Domestic_OrderFamily_CoverageLumpsum_NoClaimFamily(){
		//input
		List<ClaimCoverageDto> claimCoverageDtos = new ArrayList<>();
		claimCoverageDtos.add(new ClaimCoverageDto());
		claimCoverageDtos.get(0).setCoverage(new CoverageDto());
		claimCoverageDtos.get(0).getCoverage().setCoverageId("103001");
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimCoverages(claimCoverageDtos);
		
		PolicyOrder order = new PolicyOrder();
		order.setCoverageCategoryId(CoverageCategoryId.TRAVEL_DOMESTIC);
		order.setIsFamily(true);
		order.setPolicyStartDate(LocalDate.parse("2018-10-30",dateFormatter));
		order.setPolicyOrderFamilies(new ArrayList<PolicyOrderFamily>());
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(0).setSubId(1);
		order.getPolicyOrderFamilies().get(0).setName("Suami 1");
		order.getPolicyOrderFamilies().get(0).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(0).setRelationship(FamilyRelationship.PASANGAN);
		order.getPolicyOrderFamilies().get(0).setBirthDate(LocalDate.parse("2000-01-01",dateFormatter));
		order.getPolicyOrderFamilies().add(new PolicyOrderFamily());
		order.getPolicyOrderFamilies().get(1).setSubId(2);
		order.getPolicyOrderFamilies().get(1).setName("Anak 1");
		order.getPolicyOrderFamilies().get(1).setGender(Gender.MALE);
		order.getPolicyOrderFamilies().get(1).setRelationship(FamilyRelationship.ANAK);
		order.getPolicyOrderFamilies().get(1).setBirthDate(LocalDate.parse("2010-01-01",dateFormatter));
		
		//expected
		List<ClaimDocumentDto> expectedResult = new ArrayList<>();
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyId));
		expectedResult.get(0).setIsMandatory(true);
		expectedResult.get(0).setExtra(new ClaimDocumentExtraDto());
		expectedResult.get(0).getExtra().setFamily(modelMapperAdapter.toPolicyClaimFamilyDto(order.getPolicyOrderFamilies().get(0))); //the adult
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeFamilyCard));
		expectedResult.get(1).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(2).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		expectedResult.get(2).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(3).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		expectedResult.get(3).setIsMandatory(false);
		
		List<ClaimDocumentDto> result = claimService.requiredClaimDocumentDtos(claimDto, order);
		
		assertThat(result, is(expectedResult));
	}
	
	/*
	 * Travel with order no family, domestic, 
	 * expected: no kk and no ktp/pasport, return list as if from coverage claim doc
	 */
	@Test
	public void testTravel_OrderNonFamily(){
		//input
		List<ClaimCoverageDto> claimCoverageDtos = new ArrayList<>();
		claimCoverageDtos.add(new ClaimCoverageDto());
		claimCoverageDtos.get(0).setCoverage(new CoverageDto());
		claimCoverageDtos.get(0).getCoverage().setCoverageId("101001");
		claimCoverageDtos.add(new ClaimCoverageDto());
		claimCoverageDtos.get(1).setCoverage(new CoverageDto());
		claimCoverageDtos.get(1).getCoverage().setCoverageId("101002");
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimCoverages(claimCoverageDtos);
		
		PolicyOrder order = new PolicyOrder();
		order.setCoverageCategoryId(CoverageCategoryId.TRAVEL_DOMESTIC);
		order.setIsFamily(false);
		
		//expected
		List<ClaimDocumentDto> expectedResult = new ArrayList<>();
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		expectedResult.get(0).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		expectedResult.get(1).setIsMandatory(false);

		List<ClaimDocumentDto> result = claimService.requiredClaimDocumentDtos(claimDto, order);
		
		assertThat(result, is(expectedResult));
	}
		
	/*
	 * Non travel with 2 coverages, one with mandatory doc, one with optional doc
	 * expected: doc becomes mandatory
	 */
	@Test
	public void testNonTravel(){
		//input
		List<ClaimCoverageDto> claimCoverageDtos = new ArrayList<>();
		claimCoverageDtos.add(new ClaimCoverageDto());
		claimCoverageDtos.get(0).setCoverage(new CoverageDto());
		claimCoverageDtos.get(0).getCoverage().setCoverageId("101001");
		claimCoverageDtos.add(new ClaimCoverageDto());
		claimCoverageDtos.get(1).setCoverage(new CoverageDto());
		claimCoverageDtos.get(1).getCoverage().setCoverageId("101002");
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimCoverages(claimCoverageDtos);
		
		PolicyOrder order = new PolicyOrder();
		order.setCoverageCategoryId(CoverageCategoryId.PERSONAL_ACCIDENT);

		//expected
		List<ClaimDocumentDto> expectedResult = new ArrayList<>();
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		expectedResult.get(0).setIsMandatory(true);
		expectedResult.add(new ClaimDocumentDto());
		expectedResult.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		expectedResult.get(1).setIsMandatory(false);
		
		List<ClaimDocumentDto> result = claimService.requiredClaimDocumentDtos(claimDto, order);

		assertThat(result, is(expectedResult));
	}
	
	@Before
	public void setup(){
		claimDocTypeRegular1 = new ClaimDocType();
		claimDocTypeRegular1.setClaimDocTypeId("DT001");
		claimDocTypeRegular1.setUsageType(ClaimDocUsageType.REGULAR);
		claimDocTypeRegular1.setName("Regular 1");

		claimDocTypeRegular2 = new ClaimDocType();
		claimDocTypeRegular2.setClaimDocTypeId("DT002");
		claimDocTypeRegular2.setUsageType(ClaimDocUsageType.REGULAR);
		claimDocTypeRegular2.setName("Regular 2");
			
		claimDocTypeFamilyId = new ClaimDocType();
		claimDocTypeFamilyId.setClaimDocTypeId(ClaimService.CLAIM_DOC_TYPE_FAMILY_ID_CARD);
		claimDocTypeFamilyId.setUsageType(ClaimDocUsageType.FAMILY_ID_CARD);
		claimDocTypeFamilyId.setName("KTP");

		claimDocTypeFamilyPassport = new ClaimDocType();
		claimDocTypeFamilyPassport.setClaimDocTypeId(ClaimService.CLAIM_DOC_TYPE_FAMILY_PASSPORT);
		claimDocTypeFamilyPassport.setUsageType(ClaimDocUsageType.FAMILY_PASSPORT);
		claimDocTypeFamilyPassport.setName("Paspor");
		
		claimDocTypeFamilyCard = new ClaimDocType();
		claimDocTypeFamilyCard.setClaimDocTypeId(ClaimService.CLAIM_DOC_TYPE_FAMILY_CARD);
		claimDocTypeFamilyCard.setUsageType(ClaimDocUsageType.FAMILY_CARD);
		claimDocTypeFamilyCard.setName("Kartu Keluarga");

		//coverage 101001 with 1 mandatory regular, 1 optional regular, not lumpsum
		Coverage cov1 = new Coverage();
		cov1.setCoverageId("101001");
		cov1.setIsLumpSum(false);
		cov1.setCoverageClaimDocTypes(new ArrayList<>());
		cov1.getCoverageClaimDocTypes().add(new CoverageClaimDocType());
		cov1.getCoverageClaimDocTypes().get(0).setClaimDocTypeId(claimDocTypeRegular1.getClaimDocTypeId());
		cov1.getCoverageClaimDocTypes().get(0).setClaimDocType(claimDocTypeRegular1);
		cov1.getCoverageClaimDocTypes().get(0).setIsMandatory(true);
		cov1.getCoverageClaimDocTypes().add(new CoverageClaimDocType());
		cov1.getCoverageClaimDocTypes().get(1).setClaimDocTypeId(claimDocTypeRegular2.getClaimDocTypeId());
		cov1.getCoverageClaimDocTypes().get(1).setClaimDocType(claimDocTypeRegular2);
		cov1.getCoverageClaimDocTypes().get(1).setIsMandatory(false);
		
		//coverage 101002 with 2 optional regular, not lumpsum
		Coverage cov2 = new Coverage();
		cov2.setCoverageId("101002");
		cov2.setIsLumpSum(false);
		cov2.setCoverageClaimDocTypes(new ArrayList<>());
		cov2.getCoverageClaimDocTypes().add(new CoverageClaimDocType());
		cov2.getCoverageClaimDocTypes().get(0).setClaimDocTypeId(claimDocTypeRegular1.getClaimDocTypeId());
		cov2.getCoverageClaimDocTypes().get(0).setClaimDocType(claimDocTypeRegular1);
		cov2.getCoverageClaimDocTypes().get(0).setIsMandatory(false);
		cov2.getCoverageClaimDocTypes().add(new CoverageClaimDocType());
		cov2.getCoverageClaimDocTypes().get(1).setClaimDocTypeId(claimDocTypeRegular2.getClaimDocTypeId());
		cov2.getCoverageClaimDocTypes().get(1).setClaimDocType(claimDocTypeRegular2);
		cov2.getCoverageClaimDocTypes().get(1).setIsMandatory(false);
				
		//coverage 103001 with 2 optional regular and islumpsum
		Coverage cov3 = new Coverage();
		cov3.setCoverageId("103001");
		cov3.setIsLumpSum(true);
		cov3.setCoverageClaimDocTypes(new ArrayList<>());
		cov3.getCoverageClaimDocTypes().add(new CoverageClaimDocType());
		cov3.getCoverageClaimDocTypes().get(0).setClaimDocTypeId(claimDocTypeRegular1.getClaimDocTypeId());
		cov3.getCoverageClaimDocTypes().get(0).setClaimDocType(claimDocTypeRegular1);
		cov3.getCoverageClaimDocTypes().get(0).setIsMandatory(true);
		cov3.getCoverageClaimDocTypes().add(new CoverageClaimDocType());
		cov3.getCoverageClaimDocTypes().get(1).setClaimDocTypeId(claimDocTypeRegular2.getClaimDocTypeId());
		cov3.getCoverageClaimDocTypes().get(1).setClaimDocType(claimDocTypeRegular2);
		cov3.getCoverageClaimDocTypes().get(1).setIsMandatory(false);

		
		claimService.productService = Mockito.mock(ProductService.class);
		when(claimService.productService.fetchCoverageByCoverageId("101001")).thenReturn(cov1);
		when(claimService.productService.fetchCoverageByCoverageId("101002")).thenReturn(cov2);
		when(claimService.productService.fetchCoverageByCoverageId("103001")).thenReturn(cov3);
		when(claimService.productService.fetchClaimDocTypeByClaimDocTypeId(ClaimService.CLAIM_DOC_TYPE_FAMILY_CARD)).thenReturn(claimDocTypeFamilyCard);
		when(claimService.productService.fetchClaimDocTypeByClaimDocTypeId(ClaimService.CLAIM_DOC_TYPE_FAMILY_ID_CARD)).thenReturn(claimDocTypeFamilyId);
		when(claimService.productService.fetchClaimDocTypeByClaimDocTypeId(ClaimService.CLAIM_DOC_TYPE_FAMILY_PASSPORT)).thenReturn(claimDocTypeFamilyPassport);
		
		modelMapperAdapter.setTranslationService(new TranslationService());		
		modelMapperAdapter.getTranslationService().setTranslateMapper(Mockito.mock(TranslateMapper.class));
		
		claimService.modelMapperAdapter = this.modelMapperAdapter;
		
		claimService.config = new NinelivesConfigProperties();
	}
}
