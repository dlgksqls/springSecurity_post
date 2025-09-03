package spring.securitystudy.verificationToken.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.securitystudy.user.entity.User;
import spring.securitystudy.util.mail.EmailSender;
import spring.securitystudy.verificationToken.entity.VerificationToken;
import spring.securitystudy.verificationToken.repository.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService{

    private final VerificationTokenRepository tokenRepository;
    private final EmailSender emailSender;

    @Override
    public void sendVerificationEmail(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(
                token, user, LocalDateTime.now().plusHours(24)
        );

        tokenRepository.save(verificationToken);

        String link = "http://localhost:8080/verify?token=" + token;
        String subject = "이메일 인증";
        String htmlText = "<p>아래 링크를 클릭하여 인증을 완료하세요:</p>" +
                "<a href=\"" + link + "\">인증하기</a>";

        emailSender.send(user.getEmail(), subject, htmlText);
    }

    @Override
    public boolean verifyToken(String token) {
        return tokenRepository.findByToken(token)
                .filter(t -> !t.isExpired())
                .map(t -> {
                    User user = t.getUser();
                    user.enable();
                    tokenRepository.delete(t);
                    return true;
                })
                .orElse(false);
    }
}
