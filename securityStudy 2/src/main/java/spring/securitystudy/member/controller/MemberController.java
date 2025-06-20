package spring.securitystudy.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.securitystudy.friendship.entity.Status;
import spring.securitystudy.friendship.service.FriendShipService;
import spring.securitystudy.member.MemberDetails;
import spring.securitystudy.member.dto.MemberProfile;
import spring.securitystudy.member.dto.MemberRegisterDto;
import spring.securitystudy.member.dto.MemberUpdateDto;
import spring.securitystudy.member.entity.Member;
import spring.securitystudy.member.service.MemberService;
import spring.securitystudy.post.dto.PostViewDto;
import spring.securitystudy.post.entity.Post;
import spring.securitystudy.post.service.PostService;

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
    private final PostService postService;
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

    @GetMapping("/update")
    public String updateView(Principal principal, Model model){
        Member findMember = memberService.findByUsername(principal.getName());
        MemberUpdateDto updateMember = MemberUpdateDto.builder()
                .username(findMember.getUsername())
                .friendOnly(findMember.isFriendOnly())
                .build();

        model.addAttribute("updateMember", updateMember);
        return "member/update";
    }

    @PostMapping("/update")
    public String update(MemberUpdateDto dto, Principal principal, Model model){
        memberService.update(principal.getName(), dto);
        System.out.println("friendOnly = " + dto.isFriendOnly());

        // 수정된 사용자
        Member updateMember = memberService.findByUsername(dto.getUsername());

        // 새로운 인증 정보 갱신 (사용자 이름이 바뀌면서,, 인증 정보를 갱신해야함) .. 왠만하면 아이디는 바꾸지 마라
        MemberDetails memberDetails = new MemberDetails(
                updateMember.getUsername(),
                updateMember.getPassword(),
                updateMember.getRole().name(),
                updateMember.isFriendOnly()
        );
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        memberDetails,
                        null,
                        memberDetails.getAuthorities()
                );

        // 현재 보안 컨텍스트에 새로운 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        return "redirect:/member/profile";
    }

    @GetMapping("/profile/{username}")
    public String profileView(@PathVariable String username, Model model){
        Member findMember = memberService.findByUsername(username);
        List<Post> userPost = postService.findByUsername(findMember.getUsername());
        List<PostViewDto> postDto = new ArrayList<>();
        for (Post post : userPost) {
            postDto.add(new PostViewDto(post, true));
        }

        MemberProfile memberProfile = MemberProfile.builder()
                .username(findMember.getUsername())
                .posts(postDto)
                .role(findMember.getRole())
                .isFriendOnly(findMember.isFriendOnly())
                .build();

        model.addAttribute("memberProfile", memberProfile);
        model.addAttribute("loginUser", username);
        return "member/profile";
    }

//    @GetMapping("/find")
//    public String findView(){
//        return "member/find";
//    }

    @GetMapping("/find")
    public String find(String username, Principal principal, Model model) {
        List<Member> findMemberList = memberService.findByUsernamePrefix(username);
        Map<String, String> findMemberStringList = new HashMap<>();

        for (Member member : findMemberList) {
            Status status = friendShipService.isFriendStatus(principal.getName(), member);
            if (status != null) {
                findMemberStringList.put(member.getUsername(), status.toString());
            }
            else {
                findMemberStringList.put(member.getUsername(), null);
            }
        }

        model.addAttribute("memberList", findMemberStringList);
        return "member/find";
    }

}
