package com.ninelives.insurance.model;

import java.io.Serializable;

public class Insurer implements Serializable{
	private static final long serialVersionUID = -7020239016298676425L;
	private Integer id;
	private String code;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
