package com.wseditor.wseditor.model;

import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

public class DocumentPeers {

    private Integer docId;
    private List<WebSocketSession> peers = new ArrayList<>();

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public List<WebSocketSession> getPeers() {
        return peers;
    }

    public void setPeers(List<WebSocketSession> peers) {
        this.peers = peers;
    }
}
