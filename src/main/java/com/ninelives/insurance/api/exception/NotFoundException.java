package com.ninelives.insurance.api.exception;

import com.ninelives.insurance.api.ref.ErrorCode;

public class NotFoundException extends ApiException {

	public NotFoundException(ErrorCode code, String message) {
		super(code, message);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;



}
