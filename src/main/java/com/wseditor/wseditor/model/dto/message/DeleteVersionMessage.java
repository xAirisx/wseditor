package com.wseditor.wseditor.model.dto.message;

public class DeleteVersionMessage extends MessageDto {

    private Integer versionId;

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }
}
