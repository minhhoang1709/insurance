package com.ninelives.insurance.api.exception;

import com.ninelives.insurance.ref.ErrorCode;

public class ApiNotAuthorizedException extends ApiException {

	private static final long serialVersionUID = 1L;

	public ApiNotAuthorizedException(ErrorCode code, String message) {
		super(code, message);
	}


}
