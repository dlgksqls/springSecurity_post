package spring.securitystudy.websocket;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    public enum MessageType {
        JOIN, CHAT, LEAVE
    }

    private Long chatRoomId;
    private String sender;       // 보내는 사용자
    private String receiver;     // 받는 사용자 (추가)
    private String message;
    private MessageType messageType;
}

