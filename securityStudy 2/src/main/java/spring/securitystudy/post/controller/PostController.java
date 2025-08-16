package spring.securitystudy.post.controller;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.securitystudy.image.dto.ImageUploadDto;
import spring.securitystudy.post.exception.PostDeniedException;
import spring.securitystudy.user.CustomUserDetails;
import spring.securitystudy.post.dto.PostCommentImageDto;
import spring.securitystudy.post.dto.PostCreateDto;
import spring.securitystudy.post.dto.PostUpdateDto;
import spring.securitystudy.post.entity.Post;
import spring.securitystudy.post.service.PostServiceImpl;
import spring.securitystudy.user.exception.TokenExpiredException;
import spring.securitystudy.user.exception.UserNotAuthenticatedException;
import spring.securitystudy.util.JWTUtil;
import spring.securitystudy.util.SecurityUtil;

import java.nio.file.AccessDeniedException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostServiceImpl postService;

    @GetMapping("/create")
    public String postCreateView(@AuthenticationPrincipal CustomUserDetails loginUser){
        return "post/create";
    }

    @PostMapping("/create")
    public String postCreate(@AuthenticationPrincipal CustomUserDetails loginUser,
                             PostCreateDto postDto,
                             ImageUploadDto imageDto){
        if (loginUser == null){
            throw new UserNotAuthenticatedException("로그인을 해주세요.");
        }

        postService.create(postDto, imageDto, loginUser.getUsername());
        return "redirect:/";
    }

    @GetMapping("/detail/{id}")
    public String postDetailView(@PathVariable Long id, Principal principal, Model model){

        PostCommentImageDto postCommentList = postService.findCommentPost(id);

        model.addAttribute("postComment", postCommentList);
        model.addAttribute("username", principal.getName());
        return "post/detail";
    }

    @GetMapping("/update/{id}")
    public String postUpdateView(@PathVariable Long id, Principal principal, Model model) {

        Post findPost = postService.findById(id);

        if (!findPost.getUser().getUsername().equals(principal.getName())) {
            throw new PostDeniedException("해당 게시글의 작성자만 수정할 수 있습니다.");
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
