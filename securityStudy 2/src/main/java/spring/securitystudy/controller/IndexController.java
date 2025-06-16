    package spring.securitystudy.controller;

    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import spring.securitystudy.friendship.service.FriendShipService;
    import spring.securitystudy.member.entity.Member;
    import spring.securitystudy.member.service.MemberService;
    import spring.securitystudy.post.dto.PostViewDto;
    import spring.securitystudy.post.entity.Post;
    import spring.securitystudy.post.service.PostService;

    import java.security.Principal;
    import java.util.ArrayList;
    import java.util.List;

    @Controller
    @RequiredArgsConstructor
    public class IndexController {

        private final MemberService memberService;
        private final PostService postService;
        private final FriendShipService friendShipService;

        @GetMapping("/")
        public String index(Model model, Principal principal) {
            if (principal != null) {
                List<PostViewDto> allPost = postService.findAll();
                Member loginUser = memberService.findByUsername(principal.getName());
                List<String> friendList = friendShipService.findFriendShipList(loginUser);

                model.addAttribute("username", principal.getName());
                model.addAttribute("posts", allPost);
                model.addAttribute("friendList", friendList);
            }

            return "index";
        }
    }
