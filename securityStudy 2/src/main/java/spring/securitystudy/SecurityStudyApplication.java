package spring.securitystudy;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import spring.securitystudy.member.dto.MemberRegisterDto;
import spring.securitystudy.member.entity.Member;
import spring.securitystudy.member.repository.MemberRepository;
import spring.securitystudy.post.entity.Post;
import spring.securitystudy.post.repository.PostRepository;

@SpringBootApplication
@RequiredArgsConstructor
public class SecurityStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityStudyApplication.class, args);
    }
}
