package com.wseditor.wseditor.model.dto.message;


public class UpdateTextMessageDto extends MessageDto {

    private String documentText;

    public String getDocumentText() {
        return documentText;
    }

    public void setDocumentText(String documentText) {
        this.documentText = documentText;
    }
}
