package com.ninelives.insurance.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.ninelives.insurance.api.dto.OrderDocTypeDto;
import com.ninelives.insurance.api.dto.OrderDocumentDto;
import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.UserFileDto;
import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.core.service.FileUploadService;
import com.ninelives.insurance.model.PolicyOrderDocument;
import com.ninelives.insurance.model.UserFile;
import com.ninelives.insurance.ref.ErrorCode;

/*
 *	 
 */
public class ApiOrderServiceValidateOrderDocTest {
	ApiOrderService apiOrderService;
	FileUploadService fileUploadService;
	List<OrderDocumentDto> orderDocListStub;

	@Test
	public void testSuccess() {		
		OrderDto inputOrder = new OrderDto();
		inputOrder.setOrderDocuments(new ArrayList<OrderDocumentDto>());
		inputOrder.getOrderDocuments().add(new OrderDocumentDto());
		inputOrder.getOrderDocuments().get(0).setOrderDocType(new OrderDocTypeDto());
		inputOrder.getOrderDocuments().get(0).getOrderDocType().setOrderDocTypeId("DOC-1");
		inputOrder.getOrderDocuments().get(0).setFile(new UserFileDto());
		inputOrder.getOrderDocuments().get(0).getFile().setFileId(1L);
		
		inputOrder.getOrderDocuments().add(new OrderDocumentDto());
		inputOrder.getOrderDocuments().get(1).setOrderDocType(new OrderDocTypeDto());
		inputOrder.getOrderDocuments().get(1).getOrderDocType().setOrderDocTypeId("DOC-2");
		inputOrder.getOrderDocuments().get(1).setFile(new UserFileDto());
		inputOrder.getOrderDocuments().get(1).getFile().setFileId(2L);
		
		List<PolicyOrderDocument> result = null;
		AppBadRequestException exp = null;
		try {
			result = apiOrderService.validateOrderDocuments("userid", "orderid", inputOrder);
		} catch (AppBadRequestException e) {
			exp = e;
		}
		assertNotNull(result);
		assertEquals(result.size(), inputOrder.getOrderDocuments().size());
	}
	
	@Test
	public void testSubmitFileIdNotUnique() {		
		OrderDto inputOrder = new OrderDto();
		inputOrder.setOrderDocuments(new ArrayList<OrderDocumentDto>());
		inputOrder.getOrderDocuments().add(new OrderDocumentDto());
		inputOrder.getOrderDocuments().get(0).setOrderDocType(new OrderDocTypeDto());
		inputOrder.getOrderDocuments().get(0).getOrderDocType().setOrderDocTypeId("DOC-1");
		inputOrder.getOrderDocuments().get(0).setFile(new UserFileDto());
		inputOrder.getOrderDocuments().get(0).getFile().setFileId(1L);
		
		inputOrder.getOrderDocuments().add(new OrderDocumentDto());
		inputOrder.getOrderDocuments().get(1).setOrderDocType(new OrderDocTypeDto());
		inputOrder.getOrderDocuments().get(1).getOrderDocType().setOrderDocTypeId("DOC-2");
		inputOrder.getOrderDocuments().get(1).setFile(new UserFileDto());
		inputOrder.getOrderDocuments().get(1).getFile().setFileId(1L);
		
		List<PolicyOrderDocument> result = null;
		AppBadRequestException exp = null;
		try {
			result = apiOrderService.validateOrderDocuments("userid", "orderid", inputOrder);
		} catch (AppBadRequestException e) {
			exp = e;
		}
		assertNull(result);
		assertEquals(ErrorCode.ERR4028_ORDER_DOCUMENT_MANDATORY, exp.getCode());
	}
	
	@Test
	public void testSubmitNotMatch() {		
		OrderDto inputOrder = new OrderDto();
		inputOrder.setOrderDocuments(new ArrayList<OrderDocumentDto>());
		inputOrder.getOrderDocuments().add(new OrderDocumentDto());
		inputOrder.getOrderDocuments().get(0).setOrderDocType(new OrderDocTypeDto());
		inputOrder.getOrderDocuments().get(0).getOrderDocType().setOrderDocTypeId("DOC-1");
		inputOrder.getOrderDocuments().get(0).setFile(new UserFileDto());
		inputOrder.getOrderDocuments().get(0).getFile().setFileId(1L);
		
		inputOrder.getOrderDocuments().add(new OrderDocumentDto());
		inputOrder.getOrderDocuments().get(1).setOrderDocType(new OrderDocTypeDto());
		inputOrder.getOrderDocuments().get(1).getOrderDocType().setOrderDocTypeId("DOC-3");
		inputOrder.getOrderDocuments().get(1).setFile(new UserFileDto());
		inputOrder.getOrderDocuments().get(1).getFile().setFileId(3L);
		
		List<PolicyOrderDocument> result = null;
		AppBadRequestException exp = null;
		try {
			result = apiOrderService.validateOrderDocuments("userid", "orderid", inputOrder);
		} catch (AppBadRequestException e) {
			exp = e;
		}
		assertNull(result);
		assertEquals(ErrorCode.ERR4028_ORDER_DOCUMENT_MANDATORY, exp.getCode());
	}
	
	@Test
	public void testSubmitEmptyFile() {		
		OrderDto inputOrder = new OrderDto();
		inputOrder.setOrderDocuments(new ArrayList<OrderDocumentDto>());
		inputOrder.getOrderDocuments().add(new OrderDocumentDto());
		inputOrder.getOrderDocuments().get(0).setOrderDocType(new OrderDocTypeDto());
		inputOrder.getOrderDocuments().get(0).getOrderDocType().setOrderDocTypeId("DOC-1");
		inputOrder.getOrderDocuments().add(new OrderDocumentDto());
		inputOrder.getOrderDocuments().get(1).setOrderDocType(new OrderDocTypeDto());
		inputOrder.getOrderDocuments().get(1).getOrderDocType().setOrderDocTypeId("DOC-2");

		List<PolicyOrderDocument> result = null;
		AppBadRequestException exp = null;
		try {
			result = apiOrderService.validateOrderDocuments("userid", "orderid", inputOrder);
		} catch (AppBadRequestException e) {
			exp = e;
		}
		assertNull(result);
		assertEquals(ErrorCode.ERR4028_ORDER_DOCUMENT_MANDATORY, exp.getCode());
	}
	
	@Test
	public void testSubmitEmptyDocType() {		
		OrderDto inputOrder = new OrderDto();
		inputOrder.setOrderDocuments(new ArrayList<OrderDocumentDto>());
		inputOrder.getOrderDocuments().add(new OrderDocumentDto());
		inputOrder.getOrderDocuments().add(new OrderDocumentDto());

		List<PolicyOrderDocument> result = null;
		AppBadRequestException exp = null;
		try {
			result = apiOrderService.validateOrderDocuments("userid", "orderid", inputOrder);
		} catch (AppBadRequestException e) {
			exp = e;
		}
		assertNull(result);
		assertEquals(ErrorCode.ERR4028_ORDER_DOCUMENT_MANDATORY, exp.getCode());
	}

	@Test
	public void testNotMatchBySize() {
		OrderDto inputOrder = new OrderDto();
		inputOrder.setOrderDocuments(new ArrayList<OrderDocumentDto>());

		List<PolicyOrderDocument> result = null;
		AppBadRequestException exp = null;
		try {
			result = apiOrderService.validateOrderDocuments("userid", "orderid", inputOrder);
		} catch (AppBadRequestException e) {
			exp = e;
		}
		assertNull(result);
		assertEquals(ErrorCode.ERR4028_ORDER_DOCUMENT_MANDATORY, exp.getCode());
	}

	@Test
	public void testNotMatchBySizeNullSubmit() {
		OrderDto inputOrder = new OrderDto();

		List<PolicyOrderDocument> result = null;
		AppBadRequestException exp = null;
		try {
			result = apiOrderService.validateOrderDocuments("userid", "orderid", inputOrder);
		} catch (AppBadRequestException e) {
			exp = e;
		}
		assertNull(result);
		assertEquals(ErrorCode.ERR4028_ORDER_DOCUMENT_MANDATORY, exp.getCode());
	}

	@Test
	public void testNotMatchBySizeEmptySubmit() {		
		OrderDto inputOrder = new OrderDto();
		inputOrder.setOrderDocuments(new ArrayList<OrderDocumentDto>());

		List<PolicyOrderDocument> result = null;
		AppBadRequestException exp = null;
		try {
			result = apiOrderService.validateOrderDocuments("userid", "orderid", inputOrder);
		} catch (AppBadRequestException e) {
			exp = e;
		}
		assertNull(result);
		assertEquals(ErrorCode.ERR4028_ORDER_DOCUMENT_MANDATORY, exp.getCode());
	}

	@Test
	public void testEmptyRequiredDoc() {
		orderDocListStub.clear();

		List<PolicyOrderDocument> result = null;
		try {
			result = apiOrderService.validateOrderDocuments("userid", "orderid", new OrderDto());
		} catch (AppBadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNull(result);
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		orderDocListStub = new ArrayList<>();
		orderDocListStub.add(new OrderDocumentDto());
		orderDocListStub.get(0).setOrderDocType(new OrderDocTypeDto());
		orderDocListStub.get(0).getOrderDocType().setOrderDocTypeId("DOC-1");
		orderDocListStub.add(new OrderDocumentDto());
		orderDocListStub.get(1).setOrderDocType(new OrderDocTypeDto());
		orderDocListStub.get(1).getOrderDocType().setOrderDocTypeId("DOC-2");

		apiOrderService = Mockito.spy(ApiOrderService.class);
		doReturn(orderDocListStub).when(apiOrderService).requiredOrderDocumentDtos(any(OrderDto.class));
				
		apiOrderService.fileUploadService = Mockito.mock(FileUploadService.class);

		UserFile userFileStub1 = new UserFile();
		userFileStub1.setFileId(1L);
		UserFile userFileStub2 = new UserFile();
		userFileStub1.setFileId(2L);
		UserFile userFileStub3 = new UserFile();
		userFileStub1.setFileId(3L);
		
		when(apiOrderService.fileUploadService.featchUploadedTempFileById(1L)).thenReturn(userFileStub1);
		when(apiOrderService.fileUploadService.featchUploadedTempFileById(2L)).thenReturn(userFileStub2);
		when(apiOrderService.fileUploadService.featchUploadedTempFileById(3L)).thenReturn(userFileStub3);
		

	}
}
