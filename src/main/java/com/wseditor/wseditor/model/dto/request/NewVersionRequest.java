package com.wseditor.wseditor.model.dto.request;

public class NewVersionRequest {

    private String versionText;
    private String versionName;

    public String getVersionText() {
        return versionText;
    }

    public void setVersionText(String versionText) {
        this.versionText = versionText;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
