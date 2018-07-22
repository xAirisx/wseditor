package com.wseditor.wseditor.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    List<WebSocketSession> peers = new ArrayList<>();
    public HashSet<String> users = new HashSet<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        for (WebSocketSession peer : peers) {
            if(peer!=session) {
                peer.sendMessage(new TextMessage(message.getPayload()));
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        peers.add(session);
        super.afterConnectionEstablished(session);
        for (WebSocketSession peer : peers) {
            if(peer!=session) {
               users.add(peer.getPrincipal().getName());
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        peers.remove(session);
        logger.debug("Peer gone");
        users.remove(session.getPrincipal().getName());
        super.afterConnectionClosed(session, status);
    }
}
