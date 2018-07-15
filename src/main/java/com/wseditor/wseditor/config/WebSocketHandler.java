package com.wseditor.wseditor.config;

import com.wseditor.wseditor.domain.Document;
import com.wseditor.wseditor.domain.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    @Autowired
    DocumentRepository repository;

    List<WebSocketSession> peers = new ArrayList<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(String.format("Received message %s", message.getPayload()));
        repository.save(new Document(1, "no", message.getPayload()));
        session.sendMessage(new TextMessage(message.getPayload()));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        peers.add(session);
        System.out.println("New peer");
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        peers.remove(session);
        System.out.println("Peer gone");
        super.afterConnectionClosed(session, status);
    }
}
