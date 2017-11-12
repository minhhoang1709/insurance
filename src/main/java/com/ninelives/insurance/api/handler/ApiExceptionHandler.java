package com.ninelives.insurance.api.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ninelives.insurance.api.dto.ErrorDto;
import com.ninelives.insurance.api.exception.ApiException;
import com.ninelives.insurance.api.exception.NotAuthorizedException;
import com.ninelives.insurance.api.exception.NotFoundException;
import com.ninelives.insurance.api.ref.ErrorCode;;

@ControllerAdvice
public class ApiExceptionHandler {
//public class ApiExceptionHandler{
	
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public Map<String, ErrorDto> handleUserNotFoundException(HttpServletRequest request, Exception ex){
		ErrorDto errorDto = new ErrorDto(HttpStatus.NOT_FOUND.value(), ((ApiException) ex).getCode(), ex.getMessage());
		Map<String, ErrorDto> errorDtoResp = new HashMap<>();
		errorDtoResp.put("error", errorDto);
		
		return errorDtoResp;
	}
	
	@ExceptionHandler(NotAuthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public Map<String, ErrorDto> handleNotAuthorizedException(HttpServletRequest request, Exception ex){
		ErrorDto errorDto = new ErrorDto(HttpStatus.UNAUTHORIZED.value(), ((ApiException) ex).getCode(), ex.getMessage());
		Map<String, ErrorDto> errorDtoResp = new HashMap<>();
		errorDtoResp.put("error", errorDto);
		
		return errorDtoResp;
	}
		
//	@ExceptionHandler(Exception.class)
//	protected ResponseEntity<Object> handleException(HttpStatus status, Exception e){
//		ErrorDto errorDto = new ErrorDto(status.value(),"E1",e.getMessage());
//		return buildResponseEntity(errorDto,status);
//		
//	}
//	private ResponseEntity<Object> buildResponseEntity(ErrorDto errorDto,HttpStatus status) {
//		return new ResponseEntity<>(errorDto, status);
//	}
}
