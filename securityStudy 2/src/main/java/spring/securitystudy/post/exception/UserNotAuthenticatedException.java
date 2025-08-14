package spring.securitystudy.post.exception;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException(String msg) {
        super(msg);
    }
}
