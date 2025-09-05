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
    import spring.securitystudy.user.entity.User;
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
                User user = loginUser.getUser();
                if (!user.isEnable()){
                    return "redirect:/verified/check-email?error=check_email";
                }

                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals("RefreshToken")) isRefresh = true;
                    }
                    if (!isRefresh){
                        String role = user.getRole().toString();
                        String accessToken = createTokenAndCookie.createToken(user.getUsername(), role, "access");
                        String refreshToken = createTokenAndCookie.createToken(user.getUsername(), null, "refresh");

                        Cookie accessCookie = createTokenAndCookie.createCookie("Authentication", accessToken);
                        Cookie refreshCookie = createTokenAndCookie.createCookie("RefreshToken", refreshToken);

                        response.addCookie(accessCookie);
                        response.addCookie(refreshCookie);
                    }
                }

                Page<PostViewDto> pagePosts = postService.findAllByPage(page, 10, user);
                List<String> friendList = friendShipService.findFriendShipList(user);

                model.addAttribute("isEnable", user.isEnable());
                model.addAttribute("username", user.getUsername());
                model.addAttribute("posts", pagePosts.getContent());
                model.addAttribute("friendList", friendList);
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", pagePosts.getTotalPages());
            }

            return "index";
        }
    }
