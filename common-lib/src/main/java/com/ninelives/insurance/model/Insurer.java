package com.ninelives.insurance.model;

import java.io.Serializable;

import com.ninelives.insurance.ref.InsurerCode;

public class Insurer implements Serializable{
	private static final long serialVersionUID = -7020239016298676425L;
	private Integer id;
	private InsurerCode code;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public InsurerCode getCode() {
		return code;
	}
	public void setCode(InsurerCode code) {
		this.code = code;
	}
	
}
