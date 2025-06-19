package spring.securitystudy.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        Member loginMember = memberService.findByUsername(memberDetails.getUsername());
        commentService.create(postId, content, loginMember);

        return "redirect:/post/detail/" + postId;
    }

    @PostMapping("/edit")
    public String editComment(Long commentId,
                              String content,
                              @AuthenticationPrincipal MemberDetails memberDetails) throws AccessDeniedException {
        String currentUsername = memberDetails.getUsername(); // 로그인한 사용자 이름
        Long postId = commentService.update(commentId, content, currentUsername);
        return "redirect:/post/detail/" + postId;
    }
}
