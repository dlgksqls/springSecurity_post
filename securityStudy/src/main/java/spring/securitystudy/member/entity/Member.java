package spring.securitystudy.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.securitystudy.member.dto.MemberRegisterDto;

@Entity
@Getter
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    public void createMember(MemberRegisterDto dto, PasswordEncoder passwordEncoder) {
        this.username = dto.getUsername();
        this.password = passwordEncoder.encode(dto.getPassword());
        this.role = Role.USER;
    }
}
