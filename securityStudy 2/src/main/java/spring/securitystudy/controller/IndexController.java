    package spring.securitystudy.controller;

    import jakarta.servlet.http.Cookie;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import lombok.RequiredArgsConstructor;
    import org.springframework.data.domain.Page;
    import org.springframework.security.core.annotation.AuthenticationPrincipal;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import spring.securitystudy.friendship.service.FriendShipService;
    import spring.securitystudy.like.service.LikeService;
    import spring.securitystudy.user.CustomUserDetails;
    import spring.securitystudy.user.service.UserService;
    import spring.securitystudy.post.dto.PostViewDto;
    import spring.securitystudy.post.service.PostService;
    import spring.securitystudy.util.cookie.CreateTokenAndCookie;

    import java.util.List;

    @Controller
    @RequiredArgsConstructor
    public class IndexController {

        private final PostService postService;
        private final FriendShipService friendShipService;

        private final CreateTokenAndCookie createTokenAndCookie;

        @GetMapping("/")
        public String index(@AuthenticationPrincipal CustomUserDetails loginUser,
                            @RequestParam(defaultValue = "0") int page,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            Model model){

            boolean isRefresh = false;

            if (loginUser != null) {
                if (!loginUser.isEnabled()){
                    return "redirect:/user/check-email";
                }

                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals("RefreshToken")) isRefresh = true;
                    }
                    if (!isRefresh){
                        String role = loginUser.getAuthorities().iterator().next().getAuthority();
                        String accessToken = createTokenAndCookie.createToken(loginUser.getUsername(), role, "access");
                        String refreshToken = createTokenAndCookie.createToken(loginUser.getUsername(), null, "refresh");

                        Cookie accessCookie = createTokenAndCookie.createCookie("Authentication", accessToken);
                        Cookie refreshCookie = createTokenAndCookie.createCookie("RefreshToken", refreshToken);

                        response.addCookie(accessCookie);
                        response.addCookie(refreshCookie);
                    }
                }

                Page<PostViewDto> pagePosts = postService.findAllByPage(page, 10, loginUser.getUser());
                List<String> friendList = friendShipService.findFriendShipList(loginUser.getUser());

                model.addAttribute("username", loginUser.getUsername());
                model.addAttribute("posts", pagePosts.getContent());
                model.addAttribute("friendList", friendList);
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", pagePosts.getTotalPages());
            }

            return "index";
        }
    }
