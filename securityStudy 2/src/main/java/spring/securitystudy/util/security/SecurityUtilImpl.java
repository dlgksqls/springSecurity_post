package spring.securitystudy.util.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import spring.securitystudy.user.CustomUserDetails;
import spring.securitystudy.user.entity.User;

@Component
public class SecurityUtilImpl implements SecurityUtil{

    @Override
    public void reAuthenticate(User member) {
        // 새로운 인증 정보 갱신 (사용자 이름이 바뀌면서,, 인증 정보를 갱신해야함) .. 왠만하면 아이디는 바꾸지 마라
        CustomUserDetails memberDetails = new CustomUserDetails(member);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        memberDetails,
                        null,
                        memberDetails.getAuthorities()
                );

        // 현재 보안 컨텍스트에 새로운 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}

