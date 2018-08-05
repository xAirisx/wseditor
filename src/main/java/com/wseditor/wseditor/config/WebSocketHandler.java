package com.wseditor.wseditor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wseditor.wseditor.model.dto.message.*;
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
        MessageDto messageDto = mapper.readValue(payload, MessageDto.class);


        //Send text to all document peers
        if (messageDto instanceof UpdateTextMessageDto) {
            if (!peers.containsKey(session)) return;
            Integer docId = peers.get(session);
            peers.entrySet().stream().filter(entry -> entry.getValue().equals(docId))
                    .filter(entry -> !entry.getKey().equals(session))
                    .map(Map.Entry::getKey)
                    .forEach(peer -> Utils.trySilently(() ->
                            peer.sendMessage(new TextMessage("TEXT_UPDATED|" + ((UpdateTextMessageDto) messageDto).getDocumentText()))));

        }
        //Add peer to map and update ask to update user-table fro every peer
        else if (messageDto instanceof StartSessionMessage) {
            final Integer docId = ((StartSessionMessage) messageDto).getDocumentId();
            peers.put(session, docId);
            peers.entrySet().stream().filter(entry -> entry.getValue().equals(docId)).map(Map.Entry::getKey)
                    .forEach(peer ->
                            Utils.trySilently(() -> peer.sendMessage(new TextMessage("PEERS_UPDATED"))));

            //Update user-table in document page
        } else if (messageDto instanceof GetUsersNamesMessage) {
            session.sendMessage(new TextMessage("USER_TABLE_UPDATE:" + String.join(",", getUsersNamesByDocId(
                    ((GetUsersNamesMessage) messageDto).getDocumentId()))));

            //Update documentName for all peers
        } else if (messageDto instanceof NameUpdateMessage) {
            Integer docId = peers.get(session);
            final String documentName = ((NameUpdateMessage) messageDto).getDocumentName();
            peers.entrySet().stream().filter(entry -> entry.getValue().equals(docId)).map(Map.Entry::getKey)
                    .forEach(peer ->
                            Utils.trySilently(() -> peer.sendMessage(new TextMessage("NAME_UPDATED:" + documentName))));

            //Add to versionTable new version for all peers
        } else if (messageDto instanceof UpdateVersionMessage) {
            Integer docId = peers.get(session);
            final String versionName = ((UpdateVersionMessage) messageDto).getVersionName();
            final Integer versionId = ((UpdateVersionMessage) messageDto).getVersionId();
            peers.entrySet().stream().filter(entry -> entry.getValue().equals(docId)).map(Map.Entry::getKey)
                    .forEach(peer ->
                            Utils.trySilently(() -> peer.sendMessage(new TextMessage("VERSION_UPDATED:" + versionName + ":" + versionId))));

            //Delete from versionTable for all peers
        } else if (messageDto instanceof DeleteVersionMessage) {
            Integer docId = peers.get(session);
            final Integer versionId = ((DeleteVersionMessage) messageDto).getVersionId();
            peers.entrySet().stream().filter(entry -> entry.getValue().equals(docId)).map(Map.Entry::getKey)
                    .forEach(peer ->
                            Utils.trySilently(() -> peer.sendMessage(new TextMessage("VERSION_DELETED:" + versionId))));

        } else {

            throw new IllegalArgumentException("Failed to de-serialize request");
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
