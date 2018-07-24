package com.wseditor.wseditor.model;
import com.fasterxml.jackson.annotation.JsonProperty;



public class SessionMessage {

    public enum MessageType {START_MESSAGE, GET_USERS_MESSAGE}

    private Integer docId;
    private MessageType type;

    public SessionMessage() {
    }

    public SessionMessage(String docId) {
        this.docId = Integer.parseInt(docId);
    }

    @JsonProperty("docId")
    public Integer getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = Integer.parseInt(docId);
    }


    public MessageType getType() {
        return type;
    }
    @JsonProperty("type")
    public void setType(String stringType) {
        MessageType type =  MessageType.valueOf(stringType);
        this.type = type;
    }
}
