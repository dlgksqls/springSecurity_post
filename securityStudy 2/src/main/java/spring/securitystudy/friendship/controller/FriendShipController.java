package spring.securitystudy.friendship.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.securitystudy.friendship.dto.FriendShipReturnDto;
import spring.securitystudy.friendship.entity.FriendShip;
import spring.securitystudy.friendship.service.FriendShipService;
import spring.securitystudy.member.entity.Member;
import spring.securitystudy.member.service.MemberService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/friendship")
public class FriendShipController {
    private final FriendShipService friendShipService;
    private final MemberService memberService;

    @GetMapping("")
    public String receiveFriendView(Principal principal, Model model){
        List<FriendShip> receiveHistory = friendShipService.findByUsername(principal.getName());
        List<FriendShipReturnDto> receiveList = receiveHistory.stream()
                .map(friendShip -> new FriendShipReturnDto(
                        friendShip.getSendMember().getUsername(),
                        friendShip.getStatus()))
                .toList();

        model.addAttribute("receiveList", receiveList);

        return "friendship/receive";
    }

    @PostMapping("")
    public String handleFriendRequest(Principal principal, String requestUsername, String action) {
        Member loginMember = memberService.findByUsername(principal.getName());
        Member requestMember = memberService.findByUsername(requestUsername);

        if ("accept".equals(action)) {
            friendShipService.acceptFriend(loginMember, requestMember);
        } else if ("reject".equals(action)) {
            friendShipService.rejectFriend(loginMember, requestMember);
        }

        return "redirect:/friendship/receive";
    }


    @PostMapping("/request")
    public String requestFriend(Principal principal, String username, String param){
        Member loginUser = memberService.findByUsername(principal.getName());
        Member receiveUser = memberService.findByUsername(username);

        friendShipService.add(loginUser, receiveUser);

        return "redirect:/member/find?username=" + param;
    }
}
