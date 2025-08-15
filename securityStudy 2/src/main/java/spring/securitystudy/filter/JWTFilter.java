package spring.securitystudy.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.securitystudy.user.exception.TokenExpiredException;
import spring.securitystudy.util.JWTUtil;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessToken = null;
        String refreshToken = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Authentication".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                } else if ("RefreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        // 1. Access Token 이 없는 경우
        if (accessToken == null){
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Access Token 검증
        try{
            jwtUtil.validateToken(accessToken);

            // 2-1. 유효하다면
            setAuthentication(accessToken);

            // 2-2 Access Token 유효기간이 지났다면
        } catch (TokenExpiredException e){
            // 2-3 refreshToken이 없다면 다시 로그인
            if (refreshToken == null){
                reLogin(request, response, filterChain);
                return;
            }

            // 2-4 존재한다면 refresh 검증
            try{
                jwtUtil.validateToken(refreshToken);
                String username = jwtUtil.getUsername(refreshToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                String newAccessToken =
                        jwtUtil.createAccessToken(userDetails.getUsername(), userDetails.getAuthorities().iterator().next().getAuthority());

                Cookie cookie = setCookie("Authentication", newAccessToken);
                response.addCookie(cookie);

                setAuthentication(newAccessToken);

                response.sendRedirect(request.getRequestURI());
                return;

            } catch (TokenExpiredException ex){
                reLogin(request, response, filterChain);
            }
        }

        filterChain.doFilter(request, response);
//        if (jwtUtil.validateToken(accessToken)) {
//            // 2-1. Refresh Token 이 있는지 확인, Refresh Token 이 유효한지 확인
//            if (refreshToken == null || jwtUtil.validateToken(refreshToken)){
//                // 없으면 재로그인 필요
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
//                filterChain.doFilter(request, response);
//                return;
//            }
//
//            logger.debug("토큰 갱신");
//            String usernameFromRefreshToken = jwtUtil.getUsername(refreshToken);
//            CustomUserDetails userDetails;
//
//            try {
//                userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(usernameFromRefreshToken);
//            } catch (UsernameNotFoundException e){
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                filterChain.doFilter(request, response);
//                return;
//            }
//
//            String newAccessToken = jwtUtil.createAccessToken(
//                    userDetails.getUsername(),
//                    userDetails.getAuthorities().iterator().next().getAuthority()
//            );
//
//            Cookie accessTokenCookie = new Cookie("Authentication", newAccessToken);
//            accessTokenCookie.setHttpOnly(true);
//            accessTokenCookie.setSecure(false); // HTTPS 사용 시 true로 설정하기
//            accessTokenCookie.setPath("/");
//            accessTokenCookie.setMaxAge(jwtUtil.getAccessTokenExpiration() + 60);
//
//            response.addCookie(accessTokenCookie);
//
//            Authentication auth =
//                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//
//            SecurityContext context = SecurityContextHolder.createEmptyContext();
//            context.setAuthentication(auth);
//            SecurityContextHolder.setContext(context);
//
//            response.sendRedirect(request.getRequestURI());
//            return;
//        }
    }

    private void setAuthentication(String token) {
        String username = jwtUtil.getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
    }

    private Cookie setCookie(String header, String newAccessToken) {
        Cookie cookie = new Cookie(header, newAccessToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // HTTPS 사용 시 true로 설정하기
        cookie.setPath("/");
        cookie.setMaxAge(jwtUtil.getAccessTokenExpiration() + 60);

        return cookie;
    }

    private static void reLogin(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        filterChain.doFilter(request, response);
    }
}