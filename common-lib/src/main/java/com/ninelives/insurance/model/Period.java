package com.ninelives.insurance.model;

import java.io.Serializable;
import java.util.Date;

import com.ninelives.insurance.ref.PeriodUnit;

public class Period implements Serializable{
	private static final long serialVersionUID = 8655120355423167463L;

	private String periodId;

    private String name;
    
    private Integer value;
    
    private Integer startValue;

    private Integer endValue;

    private PeriodUnit unit;

    private Date createdDate;

    private Date updateDate;

    private String status;

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
    
    public Integer getStartValue() {
		return startValue;
	}

	public void setStartValue(Integer startValue) {
		this.startValue = startValue;
	}

	public Integer getEndValue() {
		return endValue;
	}

	public void setEndValue(Integer endValue) {
		this.endValue = endValue;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public PeriodUnit getUnit() {
		return unit;
	}

	public void setUnit(PeriodUnit unit) {
		this.unit = unit;
	}

	public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	@Override
	public String toString() {
		return "Period [" + (periodId != null ? "periodId=" + periodId + ", " : "")
				+ (name != null ? "name=" + name + ", " : "") + (value != null ? "value=" + value + ", " : "")
				+ (unit != null ? "unit=" + unit + ", " : "")
				+ (createdDate != null ? "createdDate=" + createdDate + ", " : "")
				+ (updateDate != null ? "updateDate=" + updateDate + ", " : "")
				+ (status != null ? "status=" + status : "") + "]";
	}
        
}