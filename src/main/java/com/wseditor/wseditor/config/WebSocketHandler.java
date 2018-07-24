package com.wseditor.wseditor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wseditor.wseditor.model.NameUpdateMessage;
import com.wseditor.wseditor.model.SessionMessage;
import com.wseditor.wseditor.util.Utils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private Map<WebSocketSession, Integer> peers = new HashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String payload = message.getPayload();

        NameUpdateMessage nameUpdateMessage = null;
        try {
            nameUpdateMessage = mapper.readValue(payload, NameUpdateMessage.class);
        } catch (Exception e) {
            //ignored
        }
        //Can serialize as SessionMessage?
        SessionMessage sessionMessage = null;
        try {
            sessionMessage = mapper.readValue(payload, SessionMessage.class);
        } catch (Exception e) {
            //ignored
        }

        if (sessionMessage != null) {
            if(sessionMessage.getType() == SessionMessage.MessageType.START_MESSAGE) {
                //Add peer to map and update page
                final Integer docId = sessionMessage.getDocId();
                peers.put(session, docId);
                peers.entrySet().stream().filter(entry -> entry.getValue().equals(docId)).map(Map.Entry::getKey)
                        .forEach(peer ->
                                Utils.trySilently(() -> peer.sendMessage(new TextMessage("PEERS_UPDATED"))));
            } else if (sessionMessage.getType() == SessionMessage.MessageType.GET_USERS_MESSAGE) {
                //Update users in document page
                session.sendMessage(new TextMessage("users:" + String.join(",", getUsersNamesByDocId(
                        sessionMessage.getDocId()))));
            }

        }else if(nameUpdateMessage!=null)
        {
            Integer docId = peers.get(session);
            final String documentName = nameUpdateMessage.getDocumentName();
            peers.entrySet().stream().filter(entry -> entry.getValue().equals(docId)).map(Map.Entry::getKey)
                    .forEach(peer ->
                            Utils.trySilently(() -> peer.sendMessage(new TextMessage("NAME_UPDATED:" + documentName))));
        } else {
            if (!peers.containsKey(session)) return;
            //send text to all document peers
            Integer docId = peers.get(session);
            peers.entrySet().stream().filter(entry -> entry.getValue().equals(docId))
                    .filter(entry -> !entry.getKey().equals(session))
                    .map(Map.Entry::getKey)
                    .forEach(peer -> Utils.trySilently(() ->
                            peer.sendMessage(new TextMessage(payload))));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Integer docId = peers.get(session);
        peers.remove(session);
        //update users in document page
        peers.entrySet().stream().filter(entry -> entry.getValue().equals(docId)).map(Map.Entry::getKey)
                .forEach(peer ->
                        Utils.trySilently(() -> peer.sendMessage(new TextMessage("PEERS_UPDATED"))));
        super.afterConnectionClosed(session, status);
    }


    public List<String> getUsersNamesByDocId(Integer docId) {

        return peers.entrySet().stream().filter(entry -> entry.getValue().equals(docId)).map(Map.Entry::getKey)
                .map(WebSocketSession::getPrincipal).map(Principal::getName)
                .distinct().collect(Collectors.toList());
    }

}
