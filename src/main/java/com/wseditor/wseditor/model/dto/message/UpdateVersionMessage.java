package com.wseditor.wseditor.model.dto.message;

public class UpdateVersionMessage extends MessageDto{

    private String versionName;
    private Integer versionId;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }
}
