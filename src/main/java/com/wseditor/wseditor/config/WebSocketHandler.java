package com.wseditor.wseditor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wseditor.wseditor.model.GetUsersNamesRequest;
import com.wseditor.wseditor.model.StartSessionMessage;
import com.wseditor.wseditor.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<WebSocketSession, String> peers = new HashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String json = message.getPayload();
        ObjectMapper mapper = new ObjectMapper(); //JACKSON
//        if(mapper.canDeserialize())
        String payload = message.getPayload();

        GetUsersNamesRequest getUsersNamesRequest = null;
        try {
            getUsersNamesRequest = mapper.readValue(payload, GetUsersNamesRequest.class);
        } catch (Exception e) {
            //ignored
        }

        StartSessionMessage startSessionMessage = null;
        try {
            startSessionMessage = mapper.readValue(payload, StartSessionMessage.class);
        } catch (Exception e) {
            //ignored
        }
        if (startSessionMessage != null) {
            final String docName = startSessionMessage.getDocName();
            peers.put(session, docName);
            peers.entrySet().stream().filter(entry -> entry.getValue().equals(docName)).map(Map.Entry::getKey)
                    .forEach(peer ->
                            Utils.trySilently(() -> peer.sendMessage(new TextMessage("PEERS_UPDATED"))));
        } else if (getUsersNamesRequest != null) {
            session.sendMessage(new TextMessage("users:" + String.join(",", getUsersNamesByDocName(
                    getUsersNamesRequest.getDocName()))));
        } else {
            if (!peers.containsKey(session)) return;
            String docName = peers.get(session);
            peers.entrySet().stream().filter(entry -> entry.getValue().equals(docName))
                    .filter(entry -> !entry.getKey().equals(session))
                    .map(Map.Entry::getKey)
                    .forEach(peer -> Utils.trySilently(() ->
                            peer.sendMessage(new TextMessage(payload))));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("New peer");
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String docName = peers.get(session);
        peers.remove(session);
        peers.entrySet().stream().filter(entry -> entry.getValue().equals(docName)).map(Map.Entry::getKey)
                .forEach(peer ->
                        Utils.trySilently(() -> peer.sendMessage(new TextMessage("PEERS_UPDATED"))));
        System.out.println("Peer gone");
        super.afterConnectionClosed(session, status);
    }

    public List<String> getUsersNames() {
        return peers.keySet().stream()
                .map(WebSocketSession::getPrincipal).map(Principal::getName)
                .distinct().collect(Collectors.toList());
    }

    public List<String> getUsersNamesByDocName(String docName) {
        return peers.entrySet().stream().filter(entry -> entry.getValue().equals(docName)).map(Map.Entry::getKey)
                .map(WebSocketSession::getPrincipal).map(Principal::getName)
                .distinct().collect(Collectors.toList());
    }

}
