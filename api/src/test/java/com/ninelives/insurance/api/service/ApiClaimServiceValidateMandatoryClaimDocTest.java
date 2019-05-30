package com.ninelives.insurance.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.time.LocalDateTime;
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
import com.ninelives.insurance.api.dto.ClaimDocumentDto;
import com.ninelives.insurance.api.dto.ClaimDocumentExtraDto;
import com.ninelives.insurance.api.dto.PolicyClaimFamilyDto;
import com.ninelives.insurance.api.dto.UserFileDto;
import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.core.mybatis.mapper.TranslateMapper;
import com.ninelives.insurance.core.service.TranslationService;
import com.ninelives.insurance.model.ClaimDocType;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.ref.ClaimDocUsageType;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.FamilyRelationship;
import com.ninelives.insurance.ref.Gender;

/*
 * Case:
 *  - null clientdoc
 *  - empty requireddoc?
 *  v requireddoc is A+B mandatory + C optional, clientdoc is A+B+C
 *  v requireddoc is A+B mandatory + C optional, clientdoc is A+B
 *  v requireddoc is A+B mandatory + C optional, clientdoc is A+C
 *  v requireddoc is A mandatory + B optional, clientdoc is A (no file)
 *  x requireddoc is A optional, clientdoc is empty
 *  x requireddoc is A optional, clientdoc is B
 *  v requireddoc is A mandatory fam1 + B mandatory + C optional, clientdoc is A (without fam) + B
 *  v requireddoc is A mandatory fam1 + B mandatory + C optional, clientdoc is A (with fam) + B
 */
@RunWith(SpringRunner.class)
public class ApiClaimServiceValidateMandatoryClaimDocTest {
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	ApiClaimService claimService;
	ModelMapperAdapter modelMapperAdapter = new ModelMapperAdapter();
	
	ClaimDocType claimDocTypeRegular1;
	ClaimDocType claimDocTypeRegular2;
	ClaimDocType claimDocTypeRegular3;
	ClaimDocType claimDocTypeId1;
	
	List<ClaimDocumentDto> requiredDocList;
	
	@Test
	public void testClientDocMandatoryWithFamilyComplete(){		
		//setup response
		requiredDocList.clear();
		requiredDocList.add(new ClaimDocumentDto());
		requiredDocList.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeId1));
		requiredDocList.get(0).setIsMandatory(true);
		requiredDocList.get(0).setExtra(new ClaimDocumentExtraDto());
		requiredDocList.get(0).getExtra().setFamily(new PolicyClaimFamilyDto());
		requiredDocList.get(0).getExtra().getFamily().setSubId(2);
		requiredDocList.get(0).getExtra().getFamily().setName("Anak 1");
		requiredDocList.get(0).getExtra().getFamily().setGender(Gender.MALE);
		requiredDocList.get(0).getExtra().getFamily().setRelationship(FamilyRelationship.ANAK);
		requiredDocList.get(0).getExtra().getFamily().setBirthDate(LocalDateTime.parse("2010-01-01 00:00",dateFormatter));		
		requiredDocList.add(new ClaimDocumentDto());
		requiredDocList.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		requiredDocList.get(1).setIsMandatory(true);
		requiredDocList.add(new ClaimDocumentDto());
		requiredDocList.get(2).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular3));
		requiredDocList.get(2).setIsMandatory(false);
		
		//input
		List<ClaimDocumentDto> clientDocList = new ArrayList<>();
		clientDocList.add(new ClaimDocumentDto());
		clientDocList.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeId1));
		clientDocList.get(0).setFile(new UserFileDto());
		clientDocList.get(0).getFile().setFileId(123L);
		clientDocList.get(0).setExtra(requiredDocList.get(0).getExtra());
		clientDocList.add(new ClaimDocumentDto());
		clientDocList.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		clientDocList.get(1).setFile(new UserFileDto());
		clientDocList.get(1).getFile().setFileId(123L);
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimDocuments(clientDocList);
		
		AppBadRequestException exp = null;
		try {
			claimService.validateMandatoryClaimDocument("user", claimDto, new PolicyOrder());
		} catch (AppBadRequestException e) {
			exp = e;
		}
		assertNull(exp);		
	}
	
	@Test
	public void testClientDocMandatoryNoFamily(){		
		//setup response
		requiredDocList.clear();
		requiredDocList.add(new ClaimDocumentDto());
		requiredDocList.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeId1));
		requiredDocList.get(0).setIsMandatory(true);
		requiredDocList.get(0).setExtra(new ClaimDocumentExtraDto());
		requiredDocList.get(0).getExtra().setFamily(new PolicyClaimFamilyDto());
		requiredDocList.get(0).getExtra().getFamily().setSubId(2);
		requiredDocList.get(0).getExtra().getFamily().setName("Anak 1");
		requiredDocList.get(0).getExtra().getFamily().setGender(Gender.MALE);
		requiredDocList.get(0).getExtra().getFamily().setRelationship(FamilyRelationship.ANAK);
		requiredDocList.get(0).getExtra().getFamily().setBirthDate(LocalDateTime.parse("2010-01-01 00:00",dateFormatter));		
		requiredDocList.add(new ClaimDocumentDto());
		requiredDocList.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		requiredDocList.get(1).setIsMandatory(true);
		requiredDocList.add(new ClaimDocumentDto());
		requiredDocList.get(2).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular3));
		requiredDocList.get(2).setIsMandatory(false);
		
		//input
		List<ClaimDocumentDto> clientDocList = new ArrayList<>();
		clientDocList.add(new ClaimDocumentDto());
		clientDocList.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeId1));
		clientDocList.get(0).setFile(new UserFileDto());
		clientDocList.get(0).getFile().setFileId(123L);
		clientDocList.add(new ClaimDocumentDto());
		clientDocList.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		clientDocList.get(1).setFile(new UserFileDto());
		clientDocList.get(1).getFile().setFileId(123L);
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimDocuments(clientDocList);
		
		AppBadRequestException exp = null;
		try {
			claimService.validateMandatoryClaimDocument("user", claimDto, new PolicyOrder());
		} catch (AppBadRequestException e) {
			exp = e;
		}
		assertNotNull(exp);
		assertEquals(ErrorCode.ERR7006_CLAIM_DOCUMENT_MANDATORY, exp.getCode());		
	}
	
	@Test
	public void testClientDocMandatoryCompletePlusOptional(){		
		//setup response
		requiredDocList.clear();
		requiredDocList.add(new ClaimDocumentDto());
		requiredDocList.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		requiredDocList.get(0).setIsMandatory(true);
		requiredDocList.add(new ClaimDocumentDto());
		requiredDocList.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		requiredDocList.get(1).setIsMandatory(true);
		requiredDocList.add(new ClaimDocumentDto());
		requiredDocList.get(2).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular3));
		requiredDocList.get(2).setIsMandatory(false);
		
		//input
		List<ClaimDocumentDto> clientDocList = new ArrayList<>();
		clientDocList.add(new ClaimDocumentDto());
		clientDocList.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		clientDocList.get(0).setFile(new UserFileDto());
		clientDocList.get(0).getFile().setFileId(123L);
		clientDocList.add(new ClaimDocumentDto());
		clientDocList.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		clientDocList.get(1).setFile(new UserFileDto());
		clientDocList.get(1).getFile().setFileId(123L);
		clientDocList.add(new ClaimDocumentDto());
		clientDocList.get(2).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular3));
		clientDocList.get(2).setFile(new UserFileDto());
		clientDocList.get(2).getFile().setFileId(123L);
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimDocuments(clientDocList);
		
		AppBadRequestException exp = null;
		try {
			claimService.validateMandatoryClaimDocument("user", claimDto, new PolicyOrder());
		} catch (AppBadRequestException e) {
			exp = e;
		}
		assertNull(exp);		
	}
	
	@Test
	public void testClientDocMandatoryInComplete(){		
		//setup response
		requiredDocList.clear();
		requiredDocList.add(new ClaimDocumentDto());
		requiredDocList.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		requiredDocList.get(0).setIsMandatory(true);
		requiredDocList.add(new ClaimDocumentDto());
		requiredDocList.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		requiredDocList.get(1).setIsMandatory(true);
		requiredDocList.add(new ClaimDocumentDto());
		requiredDocList.get(2).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular3));
		requiredDocList.get(2).setIsMandatory(false);
		
		//input
		List<ClaimDocumentDto> clientDocList = new ArrayList<>();
		clientDocList.add(new ClaimDocumentDto());
		clientDocList.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		clientDocList.get(0).setFile(new UserFileDto());
		clientDocList.get(0).getFile().setFileId(123L);
		clientDocList.add(new ClaimDocumentDto());
		clientDocList.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular3));
		clientDocList.get(1).setFile(new UserFileDto());
		clientDocList.get(1).getFile().setFileId(123L);
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimDocuments(clientDocList);
		
		AppBadRequestException exp = null;
		try {
			claimService.validateMandatoryClaimDocument("user", claimDto, new PolicyOrder());
		} catch (AppBadRequestException e) {
			exp = e;
		}
		assertNotNull(exp);
		assertEquals(ErrorCode.ERR7006_CLAIM_DOCUMENT_MANDATORY, exp.getCode());
	}
	
	@Test
	public void testClientDocMandatoryComplete(){		
		//setup response
		requiredDocList.clear();
		requiredDocList.add(new ClaimDocumentDto());
		requiredDocList.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		requiredDocList.get(0).setIsMandatory(true);
		requiredDocList.add(new ClaimDocumentDto());
		requiredDocList.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		requiredDocList.get(1).setIsMandatory(true);
		requiredDocList.add(new ClaimDocumentDto());
		requiredDocList.get(2).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular3));
		requiredDocList.get(2).setIsMandatory(false);
		
		//input
		List<ClaimDocumentDto> clientDocList = new ArrayList<>();
		clientDocList.add(new ClaimDocumentDto());
		clientDocList.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		clientDocList.get(0).setFile(new UserFileDto());
		clientDocList.get(0).getFile().setFileId(123L);
		clientDocList.add(new ClaimDocumentDto());
		clientDocList.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		clientDocList.get(1).setFile(new UserFileDto());
		clientDocList.get(1).getFile().setFileId(123L);
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimDocuments(clientDocList);
		
		AppBadRequestException exp = null;
		try {
			claimService.validateMandatoryClaimDocument("user", claimDto, new PolicyOrder());
		} catch (AppBadRequestException e) {
			exp = e;
		}
		assertNull(exp);
	}
	
	@Test
	public void testClientDocNoFile(){		
		//setup response
		requiredDocList.clear();
		requiredDocList.add(new ClaimDocumentDto());
		requiredDocList.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		requiredDocList.get(0).setIsMandatory(true);
		requiredDocList.add(new ClaimDocumentDto());
		requiredDocList.get(1).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular2));
		requiredDocList.get(1).setIsMandatory(false);
		
		//input
		List<ClaimDocumentDto> clientDocList = new ArrayList<>();
		clientDocList.add(new ClaimDocumentDto());
		clientDocList.get(0).setClaimDocType(modelMapperAdapter.toDto(claimDocTypeRegular1));
		
		AccidentClaimDto claimDto = new AccidentClaimDto();
		claimDto.setClaimDocuments(clientDocList);
		
		AppBadRequestException exp = null;
		try {
			claimService.validateMandatoryClaimDocument("user", claimDto, new PolicyOrder());
		} catch (AppBadRequestException e) {
			exp = e;
		}
		assertNotNull(exp);
		assertEquals(ErrorCode.ERR7006_CLAIM_DOCUMENT_MANDATORY, exp.getCode());
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
		
		claimDocTypeRegular3 = new ClaimDocType();
		claimDocTypeRegular3.setClaimDocTypeId("DT003");
		claimDocTypeRegular3.setUsageType(ClaimDocUsageType.REGULAR);
		claimDocTypeRegular3.setName("Regular 3");
		
		claimDocTypeId1 = new ClaimDocType();
		claimDocTypeId1.setClaimDocTypeId("ID001");
		claimDocTypeId1.setUsageType(ClaimDocUsageType.FAMILY_ID_CARD);
		claimDocTypeId1.setName("Id 1");
		
		requiredDocList = new ArrayList<>();		
		
		claimService = spy(ApiClaimService.class);
		doReturn(requiredDocList).when(claimService).requiredClaimDocumentDtos(any(AccidentClaimDto.class), any(PolicyOrder.class));
		
		modelMapperAdapter.setTranslationService(new TranslationService());
		modelMapperAdapter.getTranslationService().setTranslateMapper(Mockito.mock(TranslateMapper.class));
	TranslateMapper tsMapper = Mockito.mock(TranslateMapper.class);
	}
}
