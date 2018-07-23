package com.wseditor.wseditor.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetUsersNamesRequest {

    private String docName;

    public GetUsersNamesRequest() {
    }

    @JsonProperty("documentName")
    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }
}
