package spring.securitystudy.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.securitystudy.member.entity.Member;
import spring.securitystudy.member.repository.MemberRepository;
import spring.securitystudy.member.service.MemberService;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final MemberService memberService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("")
    public String viewIndex(Principal principal, Model model) {
        String currentUsername = principal.getName();
        List<Member> members = memberService.findAll()
                .stream()
                .filter(member -> !member.getUsername().equals(currentUsername))
                .toList();

        model.addAttribute("username", currentUsername);
        model.addAttribute("memberList", members);
        return "chat";
    }

    // 클라이언트가 "/app/chat.sendMessage"로 보낸 메시지를 처리
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessage message) {
        // 받는 사람에게 메시지 전송
        messagingTemplate.convertAndSendToUser(
                message.getReceiver(), "/queue/messages", message);

        // 보내는 사람에게도 동일한 메시지 전송 (채팅창에 표시되도록)
        messagingTemplate.convertAndSendToUser(
                message.getSender(), "/queue/messages", message);
    }


//    @MessageMapping("/chat.newUser")
//    @SendTo("/topic/public")
//    public ChatMessage newUser(ChatMessage chatMessage){
//        chatMessage.setType(ChatMessage.MessageType.ENTER);
//        chatMessage.setContent(chatMessage.getSender() + "님이 입장했습니다.");
//        return chatMessage;
//    }
}
