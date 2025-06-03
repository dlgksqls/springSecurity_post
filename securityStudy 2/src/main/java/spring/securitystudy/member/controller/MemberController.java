package spring.securitystudy.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.securitystudy.member.dto.MemberRegisterDto;
import spring.securitystudy.member.service.MemberService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

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
}
