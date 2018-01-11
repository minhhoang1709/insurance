package com.ninelives.insurance.payment.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ninelives.insurance.payment.exception.PaymentNotificationException;
import com.ninelives.insurance.payment.exception.PaymentNotificationInternalException;
import com.ninelives.insurance.payment.exception.PaymentNotificationBadRequestException;
import com.ninelives.insurance.payment.exception.PaymentNotificationNotAuthorizedException;
import com.ninelives.insurance.provider.payment.midtrans.dto.ErrorDto;

@ControllerAdvice
public class PaymentNotificationExceptionHandler {
		
	@ExceptionHandler(PaymentNotificationNotAuthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public Map<String, ErrorDto> handleNotAuthorizedException(HttpServletRequest request, Exception ex){
		ErrorDto errorDto = new ErrorDto(HttpStatus.UNAUTHORIZED.value(), ((PaymentNotificationException) ex).getCode(), ex.getMessage());
		Map<String, ErrorDto> errorDtoResp = new HashMap<>();
		errorDtoResp.put("error", errorDto);
		
		return errorDtoResp;
	}
	
	@ExceptionHandler(PaymentNotificationBadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Map<String, ErrorDto> handleBadRequestException(HttpServletRequest request, Exception ex){
		ErrorDto errorDto = new ErrorDto(HttpStatus.BAD_REQUEST.value(), ((PaymentNotificationException) ex).getCode(), ex.getMessage());
		Map<String, ErrorDto> errorDtoResp = new HashMap<>();
		errorDtoResp.put("error", errorDto);
		
		return errorDtoResp;
	}
	
	@ExceptionHandler(PaymentNotificationInternalException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public Map<String, ErrorDto> handleInternalServerErrorException(HttpServletRequest request, Exception ex){
		ErrorDto errorDto = new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ((PaymentNotificationException) ex).getCode(), ex.getMessage());
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
