package spring.securitystudy.like.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.securitystudy.exception.UserNotFoundException;
import spring.securitystudy.like.service.LikeService;
import spring.securitystudy.user.CustomUserDetails;
import spring.securitystudy.user.entity.User;

import java.util.List;

@Controller
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/{postId}")
    public String viewUserPostLike(@AuthenticationPrincipal CustomUserDetails loginUser, @PathVariable Long postId, Model model){

        if (loginUser == null) throw new UserNotFoundException("로그인을 해주세요");
        List<User> users = likeService.likeUserPost(postId);
        List<String> usernames = users.stream().map(User :: getUsername).toList();

        model.addAttribute("usernames", usernames);
        return "/like/user";
    }

    @PostMapping("/{postId}")
    public String handleLike(@AuthenticationPrincipal CustomUserDetails loginUser, @PathVariable Long postId){

        if (loginUser == null) throw new UserNotFoundException("로그인을 해주세요");
        likeService.handleLike(postId, loginUser.getUser());

        return "redirect:/";
    }
}
