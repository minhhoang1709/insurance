package com.ninelives.insurance.api.model;

import java.util.Date;

public class Period {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.period.period_id
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private String periodId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.period.name
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.period.value
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private Integer value;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.period.unit
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private String unit;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.period.created_date
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private Date createdDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.period.update_date
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    private Date updateDate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.period.period_id
     *
     * @return the value of public.period.period_id
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public String getPeriodId() {
        return periodId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.period.period_id
     *
     * @param periodId the value for public.period.period_id
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.period.name
     *
     * @return the value of public.period.name
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.period.name
     *
     * @param name the value for public.period.name
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.period.value
     *
     * @return the value of public.period.value
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public Integer getValue() {
        return value;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.period.value
     *
     * @param value the value for public.period.value
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setValue(Integer value) {
        this.value = value;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.period.unit
     *
     * @return the value of public.period.unit
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public String getUnit() {
        return unit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.period.unit
     *
     * @param unit the value for public.period.unit
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.period.created_date
     *
     * @return the value of public.period.created_date
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.period.created_date
     *
     * @param createdDate the value for public.period.created_date
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.period.update_date
     *
     * @return the value of public.period.update_date
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.period.update_date
     *
     * @param updateDate the value for public.period.update_date
     *
     * @mbg.generated Wed Nov 08 14:20:50 ICT 2017
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}