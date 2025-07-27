package app.springdev.session.redis.ctl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserSessionRegistry {

    private final RedisTemplate<String, String> redisTemplate;

    public UserSessionRegistry(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void registerSession(String userId, String sessionId) {
        redisTemplate.opsForValue().set("user:session:" + userId, sessionId);
    }

    public String getSessionId(String userId) {
        return redisTemplate.opsForValue().get("user:session:" + userId);
    }

    public void removeSession(String userId) {
        redisTemplate.delete("user:session:" + userId);
    }
}
