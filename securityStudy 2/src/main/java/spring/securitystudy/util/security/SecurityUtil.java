package spring.securitystudy.util.security;

import spring.securitystudy.user.entity.User;

public interface SecurityUtil {
    void reAuthenticate(User member);
}
