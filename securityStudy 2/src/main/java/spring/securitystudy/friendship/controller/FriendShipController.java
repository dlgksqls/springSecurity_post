package spring.securitystudy.friendship.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.securitystudy.friendship.dto.FriendShipReturnDto;
import spring.securitystudy.friendship.service.FriendShipServiceImpl;
import spring.securitystudy.user.CustomUserDetails;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/friendship")
@Slf4j
public class FriendShipController {

    private final FriendShipServiceImpl friendShipService;

    @GetMapping("")
    public String receiveFriendView(Principal principal, Model model){
        List<FriendShipReturnDto> receiveList = friendShipService.fineReceiveByUserName(principal.getName());

        model.addAttribute("receiveList", receiveList);
        return "friendship/receive";
    }

    @PostMapping("")
    public String handleFriendRequest(@AuthenticationPrincipal CustomUserDetails memberDetails,
                                      String requestUsername,
                                      String action) {

        String loginUsername = memberDetails.getUsername();

        if (action.equals("accept")) {
            friendShipService.acceptFriend(loginUsername, requestUsername);
        } else if (action.equals("reject")) {
            friendShipService.rejectFriend(loginUsername, requestUsername);
        }

        return "redirect:/friendship";
    }

    @PostMapping("/request")
    public String requestFriend(@AuthenticationPrincipal CustomUserDetails memberDetails,
                                String receiveUserName,
                                String param){

        friendShipService.request(memberDetails.getUsername(), receiveUserName);

        return "redirect:/member/find?username=" + param;
    }

    @GetMapping("/allFriends")
    public String allFriend(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        List<FriendShipReturnDto> allFriend = friendShipService.findAllFriendsByUsername(customUserDetails.getUsername());

        model.addAttribute("friendList", allFriend);
        return "friendship/allFriend";
    }
}
