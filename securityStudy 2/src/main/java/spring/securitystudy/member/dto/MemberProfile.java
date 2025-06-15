package spring.securitystudy.member.dto;

import lombok.Builder;
import lombok.Data;
import spring.securitystudy.member.entity.Role;
import spring.securitystudy.post.dto.PostViewDto;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class MemberProfile {
    private String username;
    private List<PostViewDto> posts = new ArrayList<>();
    private boolean isFriendOnly;
    private Role role;
}
