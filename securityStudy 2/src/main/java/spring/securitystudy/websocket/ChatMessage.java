package spring.securitystudy.websocket;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    public enum MessageType {
        ENTER, CHAT, LEAVE
    }

    private String sender;       // 보내는 사용자
    private String content;
    private MessageType type;
}

