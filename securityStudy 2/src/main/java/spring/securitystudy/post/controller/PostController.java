package spring.securitystudy.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.securitystudy.post.dto.PostCreateDto;
import spring.securitystudy.post.service.PostService;

import java.security.Principal;

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

    @GetMapping("/update")
    public String postUpdateView(){
        return "post/update";
    }

    @PatchMapping("/update")
    public String postUpdate(){

    }
}
