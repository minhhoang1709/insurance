package com.ninelives.insurance.model;

import java.time.LocalDateTime;

public class EmailLog {
    private Long id;

    private String email;

    private String param;

    private String responseHttpStatus;

    private String responseHttpBody;

    private LocalDateTime createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getResponseHttpStatus() {
        return responseHttpStatus;
    }

    public void setResponseHttpStatus(String responseHttpStatus) {
        this.responseHttpStatus = responseHttpStatus;
    }

    public String getResponseHttpBody() {
        return responseHttpBody;
    }

    public void setResponseHttpBody(String responseHttpBody) {
        this.responseHttpBody = responseHttpBody;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}