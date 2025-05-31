package spring.securitystudy.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import spring.securitystudy.member.dto.MemberRegisterDto;
import spring.securitystudy.member.service.MemberService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        return "index";
    }

    private final MemberService memberService;

    @GetMapping("/register")
    public String registerView(){
        return "register";
    }

    @PostMapping("/register")
    public String register(MemberRegisterDto dto){
        memberService.register(dto);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginView(){
        return "login";
    }
}
