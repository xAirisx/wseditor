package com.wseditor.wseditor.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NameUpdateMessage {

    private String documentName;

    @JsonProperty("docName")
    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

}
