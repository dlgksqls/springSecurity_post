package spring.securitystudy.util;

import spring.securitystudy.member.entity.Member;

public interface SecurityUtil {
    void reAuthenticate(Member member);
}
