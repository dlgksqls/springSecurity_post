package spring.securitystudy.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import spring.securitystudy.user.CustomUserDetails;
import spring.securitystudy.util.JWTUtil;

@Controller
@RequiredArgsConstructor
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @PostMapping("/reissue")
    public String reissue(HttpServletRequest request, HttpServletResponse response){

        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("RefreshToken")){
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null){
            return "redirect:/user/login?error";
        }

        if (jwtUtil.isExpired(refreshToken)){
            return "redirect:/user/login?error";
        }

        String username = jwtUtil.getUsername(refreshToken);

        CustomUserDetails userDetails;
        try {
            userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e){
            return "redirect:/user/login?error";
        }

        String newAccessToken = jwtUtil.createAccessToken(
                userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority()
        );

        Cookie newAccessTokenCookie = new Cookie("Authorization", newAccessToken);
        newAccessTokenCookie.setHttpOnly(true);
        newAccessTokenCookie.setSecure(false);
        newAccessTokenCookie.setPath("/");
        newAccessTokenCookie.setMaxAge(jwtUtil.getAccessTokenExpiration());
        response.addCookie(newAccessTokenCookie);

        return "redirect:/";
    }
}
