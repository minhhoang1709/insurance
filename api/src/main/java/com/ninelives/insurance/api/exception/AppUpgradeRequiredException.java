package com.ninelives.insurance.api.exception;

import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.ref.ErrorCode;

public class AppUpgradeRequiredException extends AppException {

	private static final long serialVersionUID = 1L;

	public AppUpgradeRequiredException(ErrorCode code, String message) {
		super(code, message);
	}

}
