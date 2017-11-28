package com.ninelives.insurance.api.model;

public class PolicyClaimDetailAccident extends PolicyClaimDetail{

    private String accidentAddressCountry;

    private String accidentAddressProvince;

    private String accidentAddressCity;

    private String accidentAddress;


    public String getAccidentAddressCountry() {
        return accidentAddressCountry;
    }

    public void setAccidentAddressCountry(String accidentAddressCountry) {
        this.accidentAddressCountry = accidentAddressCountry;
    }

    public String getAccidentAddressProvince() {
        return accidentAddressProvince;
    }

    public void setAccidentAddressProvince(String accidentAddressProvince) {
        this.accidentAddressProvince = accidentAddressProvince;
    }

    public String getAccidentAddressCity() {
        return accidentAddressCity;
    }

    public void setAccidentAddressCity(String accidentAddressCity) {
        this.accidentAddressCity = accidentAddressCity;
    }

    public String getAccidentAddress() {
        return accidentAddress;
    }

    public void setAccidentAddress(String accidentAddress) {
        this.accidentAddress = accidentAddress;
    }

}