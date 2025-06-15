package spring.securitystudy.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberUpdateDto {
    private String username;
    private boolean isFriendOnly;
}
