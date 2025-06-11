package spring.securitystudy.friendship.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.securitystudy.friendship.service.FriendShipService;
import spring.securitystudy.member.entity.Member;
import spring.securitystudy.member.service.MemberService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/friendship")
public class FriendShipController {
    private final FriendShipService friendShipService;
    private final MemberService memberService;

    @PostMapping("/add")
    public String requestFriend(Principal principal, String username){
        Member loginUser = memberService.findByUsername(principal.getName());
        Member receiveUser = memberService.findByUsername(username);

        friendShipService.add(loginUser, receiveUser);

        return "redirect:/member/find";
    }
}
