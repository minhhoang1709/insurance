package com.ninelives.insurance.core.exception;

import com.ninelives.insurance.ref.ErrorCode;

public class AppConflictException extends AppException {
	private static final long serialVersionUID = 1L;

	public AppConflictException(ErrorCode code, String message) {
		super(code, message);
	}

}
