package com.ninelives.insurance.core.exception;

import com.ninelives.insurance.ref.ErrorCode;

public class AppBadRequestException extends AppException {

	private static final long serialVersionUID = 1L;

	public AppBadRequestException(ErrorCode code, String message) {
		super(code, message);
	}

}
