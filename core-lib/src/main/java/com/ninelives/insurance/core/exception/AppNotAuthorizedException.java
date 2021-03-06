package com.ninelives.insurance.core.exception;

import com.ninelives.insurance.ref.ErrorCode;

public class AppNotAuthorizedException extends AppException {

	private static final long serialVersionUID = 1L;

	public AppNotAuthorizedException(ErrorCode code, String message) {
		super(code, message);
	}
	
	public AppNotAuthorizedException(ErrorCode code, String message, String[] messageParams) {
		super(code, message, messageParams);
	}

}
