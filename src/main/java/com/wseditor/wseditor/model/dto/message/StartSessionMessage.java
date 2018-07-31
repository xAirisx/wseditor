package com.wseditor.wseditor.model.dto.message;


public class StartSessionMessage extends MessageDto {

    private Integer documentId;

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }
}
