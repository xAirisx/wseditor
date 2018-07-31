package com.wseditor.wseditor.model.dto.request;

public class UpdateDocumentRequest {

    private String documentText;
    private String documentName;

    public String getDocumentText() {
        return documentText;
    }

    public void setDocumentText(String documentText) {
        this.documentText = documentText;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
}
