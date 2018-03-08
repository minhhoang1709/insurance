package com.ninelives.insurance.core.exception;

import com.ninelives.insurance.ref.ErrorCode;

public class AppInternalServerErrorException extends AppException{
	public AppInternalServerErrorException(ErrorCode code, String message) {
		super(code, message);
	}

	private static final long serialVersionUID = 1L;

}
