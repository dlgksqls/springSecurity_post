package spring.securitystudy.util;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 1209600) // Redis Lettuce 를 사용하기 위해
public class RefreshToken {

    @Id // jpa id 말고, spring.data.annotation의 id
    private String username;
    private String refreshToken;

    public RefreshToken(String username, String refreshToken) {
        this.username = username;
        this.refreshToken = refreshToken;
    }
}
