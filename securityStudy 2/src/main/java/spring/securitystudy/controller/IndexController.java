    package spring.securitystudy.controller;

    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import spring.securitystudy.post.dto.PostViewDto;
    import spring.securitystudy.post.entity.Post;
    import spring.securitystudy.post.service.PostService;

    import java.security.Principal;
    import java.util.ArrayList;
    import java.util.List;

    @Controller
    @RequiredArgsConstructor
    public class IndexController {

        private final PostService postService;

        @GetMapping("/")
        public String index(Model model, Principal principal) {
            if (principal != null) {
                List<PostViewDto> allPost = postService.findAll();

                model.addAttribute("username", principal.getName());
                model.addAttribute("posts", allPost);
            }

            return "index";
        }
    }
