package spring.securitystudy.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.securitystudy.comment.service.CommentService;
import spring.securitystudy.member.MemberDetails;
import spring.securitystudy.member.entity.Member;
import spring.securitystudy.member.service.MemberService;

import java.nio.file.AccessDeniedException;
import java.security.Principal;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final MemberService memberService;
    private final CommentService commentService;

    @PostMapping("/create")
    public String createComment(Long postId, String content,
                                @AuthenticationPrincipal MemberDetails memberDetails){
        MemberDetails findMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commentService.create(postId, content, findMember.getMember());

        return "redirect:/post/detail/" + postId;
    }

    @PostMapping("/edit")
    public String editComment(Long commentId,
                              String content,
                              @AuthenticationPrincipal MemberDetails memberDetails) throws AccessDeniedException {
        MemberDetails findMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername = findMember.getUsername();
        Long postId = commentService.update(commentId, content, currentUsername);
        return "redirect:/post/detail/" + postId;
    }
}
