package sesac.bookmanager.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    private final StringRedisTemplate redisTemplate;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpirationTime;


    private static final String PREFIX = "RT:";

    public void save(int userId, String token) {
        String key = PREFIX + userId;
        redisTemplate.opsForValue().set(key, token, refreshExpirationTime, TimeUnit.SECONDS);
    }

    public String findByUserId(int userId) {
        String key = PREFIX + userId;
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteByUserId(int userId) {
        String key = PREFIX + userId;
        redisTemplate.delete(key);
    }


}
