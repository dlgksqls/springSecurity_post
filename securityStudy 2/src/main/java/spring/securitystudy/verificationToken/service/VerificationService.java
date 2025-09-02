package spring.securitystudy.verificationToken.service;

import org.springframework.stereotype.Service;
import spring.securitystudy.user.entity.User;

public interface VerificationService {

    public void sendVerificationEmail(User user);
    public boolean verifyToken(String token);
}
