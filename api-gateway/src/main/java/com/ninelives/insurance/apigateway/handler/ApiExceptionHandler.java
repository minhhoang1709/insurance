package com.ninelives.insurance.apigateway.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ninelives.insurance.apigateway.dto.ErrorDto;
import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.exception.AppInternalServerErrorException;
import com.ninelives.insurance.core.exception.AppNotAuthorizedException;
import com.ninelives.insurance.core.exception.AppNotFoundException;
import com.ninelives.insurance.ref.ErrorCode;;

@ControllerAdvice
public class ApiExceptionHandler {
	
	@ExceptionHandler(AppNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public Map<String, ErrorDto> handleUserNotFoundException(HttpServletRequest request, Exception ex){
		ErrorDto errorDto = new ErrorDto(HttpStatus.NOT_FOUND.value(), ((AppException) ex).getCode(), ex.getMessage());
		Map<String, ErrorDto> errorDtoResp = new HashMap<>();
		errorDtoResp.put("error", errorDto);
		
		return errorDtoResp;
	}
	
	@ExceptionHandler(AppNotAuthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public Map<String, ErrorDto> handleNotAuthorizedException(HttpServletRequest request, Exception ex){
		ErrorDto errorDto = new ErrorDto(HttpStatus.UNAUTHORIZED.value(), ((AppException) ex).getCode(), ex.getMessage());
		Map<String, ErrorDto> errorDtoResp = new HashMap<>();
		errorDtoResp.put("error", errorDto);
		
		return errorDtoResp;
	}
	
	@ExceptionHandler(AppBadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Map<String, ErrorDto> handleBadRequestException(HttpServletRequest request, Exception ex){
		ErrorDto errorDto = new ErrorDto(HttpStatus.BAD_REQUEST.value(), ((AppException) ex).getCode(), ex.getMessage());
		Map<String, ErrorDto> errorDtoResp = new HashMap<>();
		errorDtoResp.put("error", errorDto);
		
		return errorDtoResp;
	}
	
	@ExceptionHandler(AppInternalServerErrorException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public Map<String, ErrorDto> handleInternalServerErrorException(HttpServletRequest request, Exception ex){
		ErrorDto errorDto = new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ((AppException) ex).getCode(), ex.getMessage());
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
