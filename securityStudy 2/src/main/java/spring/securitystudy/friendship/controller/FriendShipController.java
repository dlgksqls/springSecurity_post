package spring.securitystudy.friendship.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.securitystudy.friendship.dto.FriendShipReturnDto;
import spring.securitystudy.friendship.entity.FriendShip;
import spring.securitystudy.friendship.entity.Status;
import spring.securitystudy.friendship.service.FriendShipService;
import spring.securitystudy.member.MemberDetails;
import spring.securitystudy.member.entity.Member;
import spring.securitystudy.member.service.MemberService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/friendship")
@Slf4j
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
    public String handleFriendRequest(@AuthenticationPrincipal MemberDetails memberDetails,
                                      String requestUsername, String action) {
        Member loginMember = memberService.findByUsername(memberDetails.getUsername());
        Member requestMember = memberService.findByUsername(requestUsername);

        if (action.equals("accept")) {
            friendShipService.acceptFriend(loginMember, requestMember);
        } else if (action.equals("reject")) {
            friendShipService.rejectFriend(loginMember, requestMember);
        }

        return "redirect:/friendship";
    }

    @PostMapping("/request")
    public String requestFriend(@AuthenticationPrincipal MemberDetails memberDetails,
                                String username, String param){
        Member loginUser = memberService.findByUsername(memberDetails.getUsername());
        Member receiveUser = memberService.findByUsername(username);

        friendShipService.add(loginUser, receiveUser);

        return "redirect:/member/find?username=" + param;
    }

    @GetMapping("/allFriends")
    public String allFriend(@AuthenticationPrincipal MemberDetails memberDetails, Model model) {
        List<FriendShip> allFriend = friendShipService.findAllByUsername(memberDetails.getUsername());
        List<FriendShipReturnDto> returnDto = new ArrayList<>();

        for (FriendShip friendShip : allFriend) {
            if (!friendShip.getSendMember().getUsername().equals(memberDetails.getUsername())){
                returnDto.add(new FriendShipReturnDto(
                        friendShip.getReceiveMember().getUsername(), Status.ACCEPT)
                );
            }
            else {
                returnDto.add(new FriendShipReturnDto(
                        friendShip.getSendMember().getUsername(), Status.ACCEPT)
                );
            }
        }

        model.addAttribute("friendList", returnDto);
        return "friendship/allFriend";
    }
}
