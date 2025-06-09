package spring.securitystudy.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.securitystudy.comment.entity.Comment;
import spring.securitystudy.post.dto.PostCommentDto;
import spring.securitystudy.post.dto.PostCreateDto;
import spring.securitystudy.post.dto.PostUpdateDto;
import spring.securitystudy.post.entity.Post;
import spring.securitystudy.post.service.PostService;

import java.nio.channels.AcceptPendingException;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @GetMapping("/create")
    public String postCreateView(){
        return "post/create";
    }

    @PostMapping("/create")
    public String postCreate(PostCreateDto dto, Principal principal){
        if (principal == null){
            throw new IllegalArgumentException("로그인을 해주세요.");
        }
        postService.create(dto, principal.getName());
        return "redirect:/";
    }

    @GetMapping("/detail/{id}")
    public String postDetailView(@PathVariable Long id, Principal principal, Model model){
        Post findPost = postService.findById(id);

        if (findPost == null){
            throw new NoSuchElementException("게시글이 존재하지 않습니다.");
        }

        List<PostCommentDto> commentList = new ArrayList<>();
        for (Comment comment : findPost.getCommentList()) {
            commentList.add(new PostCommentDto(
                    comment.getId(),
                    comment.getMember().getUsername(),
                    comment.getCreatedDate(),
                    comment.getContent())
            );
        }

        model.addAttribute("post", findPost);
        model.addAttribute("username", principal.getName());
        model.addAttribute("comments", commentList);
        return "post/detail";
    }

    @GetMapping("/update/{id}")
    public String postUpdateView(@PathVariable Long id, Principal principal, Model model) throws AccessDeniedException {
        Post findPost = postService.findById(id);
        if (findPost == null) {
            throw new NoSuchElementException("게시글이 존재하지 않습니다.");
        }

        if (!findPost.getMember().getUsername().equals(principal.getName())) {
            throw new AccessDeniedException("해당 게시글의 작성자만 수정할 수 있습니다.");
        }

        model.addAttribute("post", findPost);
        return "post/update";
    }


    @PostMapping("/update")
    public String updatePost(@RequestParam Long id, PostUpdateDto dto) {
        postService.update(id, dto);
        return "redirect:/";
    }
}
