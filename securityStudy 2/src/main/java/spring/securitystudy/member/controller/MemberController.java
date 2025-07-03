package spring.securitystudy.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.securitystudy.member.MemberDetails;
import spring.securitystudy.member.dto.MemberProfile;
import spring.securitystudy.member.dto.MemberRegisterDto;
import spring.securitystudy.member.dto.MemberUpdateDto;
import spring.securitystudy.member.entity.Member;
import spring.securitystudy.member.service.MemberService;
import spring.securitystudy.util.SecurityUtil;
import spring.securitystudy.util.SecurityUtilImpl;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final SecurityUtil securityUtil;

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

    @GetMapping("/update")
    public String updateView(@AuthenticationPrincipal MemberDetails findMember,
                             Model model){

//        MemberDetails findMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        MemberUpdateDto updateMember = MemberUpdateDto.builder()
                .username(findMember.getUsername())
                .friendOnly(findMember.getMember().isFriendOnly())
                .build();

        model.addAttribute("updateMember", updateMember);
        return "member/update";
    }

    @PostMapping("/update")
    public String update(MemberUpdateDto dto, Principal principal){

        memberService.update(principal.getName(), dto);

        // 수정된 사용자
        Member updateMember = memberService.findByUsername(dto.getUsername());

        // 정보를 수정했으므로 새로운 인증 정보 갱신
        securityUtil.reAuthenticate(updateMember);

        return "redirect:/member/profile/" + principal.getName();
    }

    @PostMapping("/changeIsFriend")
    public String isFriendOnly(@RequestParam(defaultValue = "false") boolean isFriendOnly,
                               Principal principal){

        Member loginMember = memberService.changeFriendOnly(principal.getName(), isFriendOnly);
        securityUtil.reAuthenticate(loginMember);

        return "redirect:/member/profile/" + principal.getName();
    }

    @GetMapping("/profile/{username}")
    public String profileView(@AuthenticationPrincipal MemberDetails loginMember,
                              @PathVariable String username,
                              Model model){

        MemberProfile memberProfile = memberService.findPostByUsername(username);

        model.addAttribute("memberProfile", memberProfile);
        model.addAttribute("loginUser", loginMember.getUsername());
        model.addAttribute("isFriendOnly", loginMember.getMember().isFriendOnly());

        return "member/profile";
    }

    @GetMapping("/find")
    public String find(@AuthenticationPrincipal MemberDetails loginMember,
                       String username,
                       Model model) {

        List<Member> findMemberList = memberService.findByUsernamePrefix(username);

        Map<String, String> findMemberStringList =
                memberService.findFriendShipStatus(loginMember.getMember(), findMemberList);

        model.addAttribute("memberList", findMemberStringList);
        return "member/find";
    }
}
