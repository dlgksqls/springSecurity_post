package spring.securitystudy.member.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Builder
@Data
public class MemberUpdateDto {
    private String username;
    private boolean friendOnly; // "is" 제거
}