package app.springdev.redis.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisListService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void pushMessage(String roomId, String message) {
        String key = "CHAT:" + roomId;
        redisTemplate.opsForList().rightPush(key, message);
        redisTemplate.opsForList().trim(key, -100, -1); // 최근 100개만 유지
    }
}
