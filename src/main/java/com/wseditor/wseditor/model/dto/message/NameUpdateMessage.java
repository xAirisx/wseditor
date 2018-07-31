package com.wseditor.wseditor.model.dto.message;

public class NameUpdateMessage extends MessageDto {

    private String documentName;

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

}