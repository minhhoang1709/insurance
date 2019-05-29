package com.ninelives.insurance.model.tlt;

public class CoverageOptionTlt {
    private String id;

    private String languageCode;

    private String coverageOptionName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getCoverageOptionName() {
        return coverageOptionName;
    }

    public void setCoverageOptionName(String coverageOptionName) {
        this.coverageOptionName = coverageOptionName;
    }
}