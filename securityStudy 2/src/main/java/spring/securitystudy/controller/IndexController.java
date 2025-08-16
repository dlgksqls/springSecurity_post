    package spring.securitystudy.controller;

    import jakarta.servlet.http.Cookie;
    import jakarta.servlet.http.HttpServletRequest;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.data.domain.Page;
    import org.springframework.security.core.annotation.AuthenticationPrincipal;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import spring.securitystudy.friendship.service.FriendShipServiceImpl;
    import spring.securitystudy.user.CustomUserDetails;
    import spring.securitystudy.user.service.UserService;
    import spring.securitystudy.post.dto.PostViewDto;
    import spring.securitystudy.post.service.PostService;
    import spring.securitystudy.util.JWTUtil;

    import java.net.http.HttpRequest;
    import java.util.List;

    @Controller
    @RequiredArgsConstructor
    public class IndexController {

        private final UserService userService;
        private final PostService postService;
        private final FriendShipServiceImpl friendShipService;

        @GetMapping("/")
        public String index(@AuthenticationPrincipal CustomUserDetails loginUser,
                            @RequestParam(defaultValue = "0") int page,
                            Model model){

            if (loginUser != null) {
                CustomUserDetails findMember = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//                List<PostViewDto> allPost = postService.findAll();
                Page<PostViewDto> pagePosts = postService.findAllByPage(page, 10);
                List<String> friendList = friendShipService.findFriendShipList(findMember.getUser());

                model.addAttribute("username", loginUser.getUsername());
                model.addAttribute("posts", pagePosts.getContent());
                model.addAttribute("friendList", friendList);
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", pagePosts.getTotalPages());
            }

            return "index";
        }
    }
