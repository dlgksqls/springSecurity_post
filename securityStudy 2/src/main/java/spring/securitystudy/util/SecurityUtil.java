package spring.securitystudy.util;

import spring.securitystudy.user.entity.User;

public interface SecurityUtil {
    void reAuthenticate(User member);
}
