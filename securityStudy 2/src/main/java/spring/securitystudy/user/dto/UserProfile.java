package spring.securitystudy.user.dto;

import lombok.Builder;
import lombok.Data;
import spring.securitystudy.user.entity.Role;
import spring.securitystudy.post.dto.PostViewDto;

import java.util.List;

@Data
@Builder
public class UserProfile {
    private String username;
    private List<PostViewDto> posts;
    private boolean isFriendOnly;
    private Role role;
}
