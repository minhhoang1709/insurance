package com.ninelives.insurance.api.model;

public class PolicyOrderClaimDoc {
    private Long claimDocumentId;

    private String fileId;

    private String docTypeId;

    public Long getClaimDocumentId() {
        return claimDocumentId;
    }

    public void setClaimDocumentId(Long claimDocumentId) {
        this.claimDocumentId = claimDocumentId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeId(String docTypeId) {
        this.docTypeId = docTypeId;
    }
}