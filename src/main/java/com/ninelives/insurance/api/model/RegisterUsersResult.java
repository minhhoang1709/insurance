package com.ninelives.insurance.api.model;

import com.ninelives.insurance.api.dto.UsersDto;

public class RegisterUsersResult {
	UsersDto userDto;
	Boolean isNew;
	
	public UsersDto getUserDto() {
		return userDto;
	}
	public void setUserDto(UsersDto userDto) {
		this.userDto = userDto;
	}
	public Boolean getIsNew() {
		return isNew;
	}
	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}
	
}
