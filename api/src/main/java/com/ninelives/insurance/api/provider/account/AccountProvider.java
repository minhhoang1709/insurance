package com.ninelives.insurance.api.provider.account;

import com.ninelives.insurance.api.dto.RegistrationDto;

public interface AccountProvider {
	public String verifyEmail(RegistrationDto registrationDto);
}
