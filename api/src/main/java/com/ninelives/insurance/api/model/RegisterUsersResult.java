package com.ninelives.insurance.api.model;

import com.ninelives.insurance.api.dto.UserDto;

public class RegisterUsersResult {
	UserDto userDto;
	Boolean isNew;
	
	public UserDto getUserDto() {
		return userDto;
	}
	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}
	public Boolean getIsNew() {
		return isNew;
	}
	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}
	
}
