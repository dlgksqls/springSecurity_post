package spring.securitystudy.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage chatMessage){
        return chatMessage;
    }

    @MessageMapping("/chat.newUser")
    @SendTo("/topic/public")
    public ChatMessage newUser(ChatMessage chatMessage){
        chatMessage.setType(ChatMessage.MessageType.ENTER);
        chatMessage.setContent(chatMessage.getSender() + "님이 입장했습니다.");
        return chatMessage;
    }
}
