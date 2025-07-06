package app.springdev.redis.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RedisHashService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void saveUserProfile(String userId, Map<String, String> profile) {
        redisTemplate.opsForHash().putAll("USER:" + userId, profile);
    }

    public Map<Object, Object> getUserProfile(String userId) {
        return redisTemplate.opsForHash().entries("USER:" + userId);
    }
}
