package spring.securitystudy.friendship.dto;

import lombok.Data;
import spring.securitystudy.friendship.entity.Status;

@Data
public class FriendShipReturnDto {
    String username;
    String status;

    public FriendShipReturnDto(String username, Status status) {
        this.username = username;
        this.status = (status != null) ? status.name() : null;
    }
}
