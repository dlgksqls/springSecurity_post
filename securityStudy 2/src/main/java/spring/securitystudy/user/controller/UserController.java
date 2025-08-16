package spring.securitystudy.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.securitystudy.user.CustomUserDetails;
import spring.securitystudy.user.dto.UserProfile;
import spring.securitystudy.user.dto.UserRegisterDto;
import spring.securitystudy.user.dto.UserUpdateDto;
import spring.securitystudy.user.entity.User;
import spring.securitystudy.user.service.UserService;
import spring.securitystudy.util.SecurityUtil;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final SecurityUtil securityUtil;

    @GetMapping("/error")
    public String error(){
        return "user/error";
    }

    @GetMapping("/register")
    public String registerView(){
        return "user/register";
    }

    @PostMapping("/register")
    public String register(UserRegisterDto dto){
        userService.register(dto);
        return "redirect:/user/login"; // / 붙여주기 맨 앞에
    }

    @GetMapping("/login")
    public String loginView(){
        return "user/login";
    }

//    @GetMapping("/logout")
//    public String logout(HttpServletResponse response){
//        Cookie deleteCookie = new Cookie("Authentication", null);
//        deleteCookie.setMaxAge(0);
//        deleteCookie.setPath("/");
//        response.addCookie(deleteCookie);
//
//        return "redirect:/user/login";
//    }

    @GetMapping("/update")
    public String updateView(@AuthenticationPrincipal CustomUserDetails findUser,
                             Model model){

//        MemberDetails findMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserUpdateDto updateUser = UserUpdateDto.builder()
                .username(findUser.getUsername())
                .friendOnly(findUser.getUser().isFriendOnly())
                .build();

        model.addAttribute("updateUser", updateUser);
        return "user/update";
    }

    @PostMapping("/update")
    public String update(UserUpdateDto dto, Principal principal){

        userService.update(principal.getName(), dto);

        // 수정된 사용자
        User updateMember = userService.findByUsername(dto.getUsername());

        // 정보를 수정했으므로 새로운 인증 정보 갱신
        securityUtil.reAuthenticate(updateMember);

        return "redirect:/user/profile/" + principal.getName();
    }

    @PostMapping("/changeIsFriend")
    public String isFriendOnly(@RequestParam(defaultValue = "false") boolean isFriendOnly,
                               Principal principal){

        User loginUser = userService.changeFriendOnly(principal.getName(), isFriendOnly);
        securityUtil.reAuthenticate(loginUser);

        return "redirect:/user/profile/" + principal.getName();
    }

    @GetMapping("/profile/{username}")
    public String profileView(@AuthenticationPrincipal CustomUserDetails loginUser,
                              @PathVariable String username,
                              Model model){

        UserProfile userProfile = userService.findPostByUsername(username);

        model.addAttribute("userProfile", userProfile);
        model.addAttribute("loginUser", loginUser.getUsername());
        model.addAttribute("isFriendOnly", loginUser.getUser().isFriendOnly());

        return "user/profile";
    }

    @GetMapping("/find")
    public String find(@AuthenticationPrincipal CustomUserDetails loginUser,
                       String username,
                       Model model) {

        if (username == null || username.isEmpty()){
            return "user/find";
        }

        List<User> findUserList = userService.findByUsernamePrefix(username);

        Map<String, String> findUserStringList =
                userService.findFriendShipStatus(loginUser.getUser(), findUserList);

        model.addAttribute("userList", findUserStringList);
        return "user/find";
    }
}