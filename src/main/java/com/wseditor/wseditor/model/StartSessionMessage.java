package com.wseditor.wseditor.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StartSessionMessage {

    private String docName;

    public StartSessionMessage() {
    }

    public StartSessionMessage(String docName) {
        this.docName = docName;
    }

    @JsonProperty("docName")
    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }
}
