package com.ninelives.insurance.api.exception;

import com.ninelives.insurance.api.ref.ErrorCode;

public class ApiInternalServerErrorException extends ApiException{
	public ApiInternalServerErrorException(ErrorCode code, String message) {
		super(code, message);
	}

	private static final long serialVersionUID = 1L;

}
