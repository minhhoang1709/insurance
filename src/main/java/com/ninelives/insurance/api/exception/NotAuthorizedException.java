package com.ninelives.insurance.api.exception;

import com.ninelives.insurance.api.ref.ErrorCode;

public class NotAuthorizedException extends ApiException {

	private static final long serialVersionUID = 1L;

	public NotAuthorizedException(ErrorCode code, String message) {
		super(code, message);
	}


}
