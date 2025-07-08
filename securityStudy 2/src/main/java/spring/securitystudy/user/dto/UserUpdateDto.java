package spring.securitystudy.user.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserUpdateDto {
    private String username;
    private boolean friendOnly; // "is" 제거

}