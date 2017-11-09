package com.ninelives.insurance.api.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PeriodDto {
	private String periodId;

    private String name;

    private Integer value;

    private String unit;

	public String getPeriodId() {
		return periodId;
	}

	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
