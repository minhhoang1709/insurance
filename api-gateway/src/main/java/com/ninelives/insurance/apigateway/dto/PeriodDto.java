package com.ninelives.insurance.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ninelives.insurance.ref.PeriodUnit;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PeriodDto {
	private String periodId;

    private String name;

    private Integer value;

    private PeriodUnit unit;

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

	public PeriodUnit getUnit() {
		return unit;
	}

	public void setUnit(PeriodUnit unit) {
		this.unit = unit;
	}

}
