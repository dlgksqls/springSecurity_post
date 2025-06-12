package spring.securitystudy.friendship.dto;

import lombok.Data;

@Data
public class FriendShipReturnDto {
    String username;

    public FriendShipReturnDto(String username) {
        this.username = username;
    }
}
