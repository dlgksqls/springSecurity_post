package spring.securitystudy.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.securitystudy.comment.service.CommentServiceImpl;
import spring.securitystudy.user.UserDetailsImpl;

import java.nio.file.AccessDeniedException;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceImpl commentService;

    @PostMapping("/create")
    public String createComment(Long postId, String content,
                                @AuthenticationPrincipal UserDetails loginMember){
        commentService.create(postId, content, loginMember.getUsername());

        return "redirect:/post/detail/" + postId;
    }

    @PostMapping("/edit")
    public String editComment(Long commentId,
                              String content,
                              @AuthenticationPrincipal UserDetails loginMember) throws AccessDeniedException {
        String currentUsername = loginMember.getUsername();
        Long postId = commentService.update(commentId, content, currentUsername);
        return "redirect:/post/detail/" + postId;
    }
}
