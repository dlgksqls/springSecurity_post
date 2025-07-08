package spring.securitystudy.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.securitystudy.image.dto.ImageUploadDto;
import spring.securitystudy.user.CustomUserDetails;
import spring.securitystudy.post.dto.PostCommentImageDto;
import spring.securitystudy.post.dto.PostCreateDto;
import spring.securitystudy.post.dto.PostUpdateDto;
import spring.securitystudy.post.entity.Post;
import spring.securitystudy.post.service.PostServiceImpl;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostServiceImpl postService;

    @GetMapping("/create")
    public String postCreateView(){
        return "post/create";
    }

    @PostMapping("/create")
    public String postCreate(@AuthenticationPrincipal CustomUserDetails loginUser,
                             PostCreateDto postDto,
                             ImageUploadDto imageDto){
        if (loginUser == null){
            throw new IllegalArgumentException("로그인을 해주세요.");
        }
        postService.create(postDto, imageDto, loginUser.getUsername());
        return "redirect:/";
    }

    @GetMapping("/detail/{id}")
    public String postDetailView(@PathVariable Long id, Principal principal, Model model){
        PostCommentImageDto postCommentList = postService.findCommentPost(id);

        if (postCommentList == null){
            throw new NoSuchElementException("게시글이 존재하지 않습니다.");
        }

        model.addAttribute("postComment", postCommentList);
        model.addAttribute("username", principal.getName());
        return "post/detail";
    }

    @GetMapping("/update/{id}")
    public String postUpdateView(@PathVariable Long id, Principal principal, Model model) throws AccessDeniedException {
        Post findPost = postService.findById(id);
        if (findPost == null) {
            throw new NoSuchElementException("게시글이 존재하지 않습니다.");
        }

        if (!findPost.getUser().getUsername().equals(principal.getName())) {
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
