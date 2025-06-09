package spring.securitystudy.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class WebSocketHandlerImpl extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final Set<WebSocketSession> sessions = new HashSet<>();
    private final Map<String, WebSocketSession> userSessions = new HashMap<>();

    // 소켓 연결 확인
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        session.sendMessage(new TextMessage("WebSocket 연결 완료"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payLoad = message.getPayload();
        MessageDto messageDto = objectMapper.readValue(payLoad, MessageDto.class);

        if (messageDto.getMessageType() == MessageDto.MessageType.JOIN) {
            userSessions.put(messageDto.getSender(), session);
            messageDto.setMessage("입장했습니다.");
        } else if (messageDto.getMessageType() == MessageDto.MessageType.CHAT) {
            WebSocketSession receiverSession = userSessions.get(messageDto.getReceiver());
            if (receiverSession != null && receiverSession.isOpen()) {
                receiverSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(messageDto)));
            }
        }

        // Optionally, 본인에게도 메시지 전송
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(messageDto)));
    }


    // 소켓 연결 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        session.sendMessage(new TextMessage("WebSocket 연결 종료"));
    }
}
