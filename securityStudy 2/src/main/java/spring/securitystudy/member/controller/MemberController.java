package spring.securitystudy.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.securitystudy.friendship.entity.Status;
import spring.securitystudy.friendship.service.FriendShipService;
import spring.securitystudy.member.dto.MemberRegisterDto;
import spring.securitystudy.member.entity.Member;
import spring.securitystudy.member.service.MemberService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final FriendShipService friendShipService;

    @GetMapping("/register")
    public String registerView(){
        return "member/register";
    }

    @PostMapping("/register")
    public String register(MemberRegisterDto dto){
        memberService.register(dto);
        return "redirect:member/login";
    }

    @GetMapping("/login")
    public String loginView(){
        return "member/login";
    }

//    @GetMapping("/find")
//    public String findView(){
//        return "member/find";
//    }

    @GetMapping("/find")
    public String find(String username, Principal principal, Model model) {
        List<Member> findMemberList = memberService.findByUsernamePrefix(username);
        Map<String, Status> findMemberStringList = new HashMap<>();

        for (Member member : findMemberList) {
            findMemberStringList.put(member.getUsername(), friendShipService.isFriendStatus(principal.getName(), member));
        }

        model.addAttribute("memberList", findMemberStringList);
        return "member/find";
    }

}
