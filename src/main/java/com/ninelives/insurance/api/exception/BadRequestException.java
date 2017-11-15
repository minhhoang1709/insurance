package com.ninelives.insurance.api.exception;

import com.ninelives.insurance.api.ref.ErrorCode;

public class BadRequestException extends ApiException {

	private static final long serialVersionUID = 1L;

	public BadRequestException(ErrorCode code, String message) {
		super(code, message);
	}

}
