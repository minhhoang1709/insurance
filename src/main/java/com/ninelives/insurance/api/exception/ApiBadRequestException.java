package com.ninelives.insurance.api.exception;

import com.ninelives.insurance.api.ref.ErrorCode;

public class ApiBadRequestException extends ApiException {

	private static final long serialVersionUID = 1L;

	public ApiBadRequestException(ErrorCode code, String message) {
		super(code, message);
	}

}
