package spring.securitystudy;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class SecurityStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityStudyApplication.class, args);
    }
}
