package com.projectct.messageservice.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SignalingHandler extends TextWebSocketHandler {
    private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();
    private final Map<WebSocketSession, String> userRooms = new ConcurrentHashMap<>();
    private final Map<WebSocketSession, Map<String, Boolean>> userStates = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String room = userRooms.remove(session);
        if (room != null) {
            rooms.get(room).remove(session);
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Map<String, Object> msg = mapper.readValue(message.getPayload(), Map.class);
        String type = (String) msg.get("type");

        log.info(message.getPayload());

        switch (type) {
            case "join" -> {
                String room = ((Integer) msg.get("room")).toString();
                Map<String, Object> userInfo = (Map<String, Object>) msg.get("user");

                rooms.putIfAbsent(room, ConcurrentHashMap.newKeySet());
                rooms.get(room).add(session);
                userRooms.put(session, room);

                session.getAttributes().put("user", userInfo);

                List<Map<String, Object>> existingPeers = rooms.get(room).stream()
                        .filter(s -> s != session)
                        .map(s -> Map.of(
                                "peerId", s.getId(),
                                "user", s.getAttributes().get("user")
                        )).toList();

                Map<String, Object> peersMsg = Map.of(
                        "type", "peers",
                        "peers", existingPeers,
                        "yourId", session.getId()
                );
                session.sendMessage(new TextMessage(mapper.writeValueAsString(peersMsg)));

                for (WebSocketSession peer : rooms.get(room)) {
                    if (peer != session && peer.isOpen()) {
                        Map<String, Boolean> state = userStates.getOrDefault(peer, Map.of());

                        if (Boolean.FALSE.equals(state.get("audio"))) {
                            Map<String, Object> voiceOffMsg = Map.of(
                                    "type", "voice-off",
                                    "peerId", peer.getId()
                            );
                            session.sendMessage(new TextMessage(mapper.writeValueAsString(voiceOffMsg)));
                        }

                        if (Boolean.FALSE.equals(state.get("video"))) {
                            Map<String, Object> cameraOffMsg = Map.of(
                                    "type", "camera-off",
                                    "peerId", peer.getId()
                            );
                            session.sendMessage(new TextMessage(mapper.writeValueAsString(cameraOffMsg)));
                        }
                    }
                }

                for (WebSocketSession peer : rooms.get(room)) {
                    if (peer != session && peer.isOpen()) {
                        Map<String, Object> newPeerMsg = Map.of(
                                "type", "new-peer",
                                "peerId", session.getId(),
                                "user", userInfo
                        );
                        peer.sendMessage(new TextMessage(mapper.writeValueAsString(newPeerMsg)));
                    }
                }
            }

            case "offer", "answer", "candidate" -> {
                String to = (String) msg.get("to");
                for (WebSocketSession s : rooms.get(userRooms.get(session))) {
                    if (s.getId().equals(to)) {
                        msg.put("from", session.getId());
                        s.sendMessage(new TextMessage(mapper.writeValueAsString(msg)));
                        break;
                    }
                }
            }
            case "leave" -> {
                String room = userRooms.remove(session);
                if (room != null && rooms.containsKey(room)) {
                    rooms.get(room).remove(session);

                    for (WebSocketSession peer : rooms.get(room)) {
                        if (peer.isOpen()) {
                            try {
                                Map<String, Object> leave_msg = Map.of(
                                        "type", "peer-left",
                                        "peerId", session.getId()
                                );
                                peer.sendMessage(new TextMessage(mapper.writeValueAsString(leave_msg)));
                            } catch (Exception e) {
                                log.error("Failed to notify peer left", e);
                            }
                        }
                    }
                }
            }
            case "voice-off", "voice-on", "camera-off", "camera-on" -> {
                String room = userRooms.get(session);
                if (room == null) return;

                userStates.computeIfAbsent(session, k -> new HashMap<>()).put(
                        type.startsWith("voice") ? "audio" : "video",
                        type.endsWith("on")
                );

                Map<String, Object> statusMsg = Map.of(
                        "type", type,
                        "peerId", session.getId()
                );

                for (WebSocketSession peer : rooms.getOrDefault(room, Set.of())) {
                    if (peer != session && peer.isOpen()) {
                        peer.sendMessage(new TextMessage(mapper.writeValueAsString(statusMsg)));
                    }
                }
            }
        }
    }
}
