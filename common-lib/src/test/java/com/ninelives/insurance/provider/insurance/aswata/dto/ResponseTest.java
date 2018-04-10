package com.ninelives.insurance.provider.insurance.aswata.dto;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResponseTest {
	@Test
	public void testIsSuccess(){
		ResponseDto dto = new ResponseDto();
		dto.setHttpStatus(200);
		assertFalse(dto.isSuccess());
		
		ClaimResponseDto claimResponseDto = new ClaimResponseDto();
		dto.setResponse(claimResponseDto);		
		assertFalse(dto.isSuccess());
		
		claimResponseDto.setResponseCode(ResponseDto.RESPONSE_CODE_SUCCESS);
		assertFalse(dto.isSuccess());
		
		claimResponseDto.setResponseParam(new ClaimResponseDto.ResponseParam());
		assertTrue(dto.isSuccess());
		
		claimResponseDto.setResponseCode("123");
		assertFalse(dto.isSuccess());
		
		dto.setHttpStatus(500);
		assertFalse(dto.isSuccess());
	}
}
