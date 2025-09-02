package spring.securitystudy.verificationToken.entity;

import jakarta.persistence.*;
import lombok.Getter;
import spring.securitystudy.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne
    @JoinColumn(name = "member_id")
    private User user;

    private LocalDateTime expiredDate;

    public VerificationToken() {
    }

    public VerificationToken(String token, User user, LocalDateTime expiredDate) {
        this.token = token;
        this.user = user;
        this.expiredDate = expiredDate;
    }

    public boolean isExpired(){
        return expiredDate.isBefore(LocalDateTime.now());
    }
}
