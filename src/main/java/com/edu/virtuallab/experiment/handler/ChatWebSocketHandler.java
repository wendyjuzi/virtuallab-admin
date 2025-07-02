package com.edu.virtuallab.experiment.handler;

import com.edu.virtuallab.experiment.dto.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    // 存所有会话，key是sessionId（或你自定义的唯一id）
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    // 按teamId管理的会话列表，方便广播消息
    private final Map<Long, List<WebSocketSession>> teamSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Long getTeamIdFromQueryParam(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query == null || query.isEmpty()) return null;

        String[] params = query.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2 && "teamId".equals(keyValue[0])) {
                try {
                    return Long.parseLong(keyValue[1]);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionMap.put(session.getId(), session);

        Long teamId = getTeamIdFromQueryParam(session); // 从连接参数取teamId
        if (teamId != null) {
            teamSessions.computeIfAbsent(teamId, k -> new ArrayList<>()).add(session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionMap.remove(session.getId());

        Long teamId = getTeamIdFromQueryParam(session);
        if (teamId != null && teamSessions.containsKey(teamId)) {
            teamSessions.get(teamId).remove(session);
            if (teamSessions.get(teamId).isEmpty()) {
                teamSessions.remove(teamId);
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatMessage chatMessage = objectMapper.readValue(message.getPayload(), ChatMessage.class);
        Long teamId = chatMessage.getTeamId();

        if (teamId != null && teamSessions.containsKey(teamId)) {
            List<WebSocketSession> sessions = teamSessions.get(teamId);
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    s.sendMessage(message);
                }
            }
        }
    }


    private String getUserIdFromParams(WebSocketSession session) {
        return session.getUri().getQuery().split("=")[1]; // 简化处理 userId=xx
    }
}
