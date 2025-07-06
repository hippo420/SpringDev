package app.springdev.redis.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisHyperLogLogService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void logVisitor(String date, String userId) {
        redisTemplate.opsForHyperLogLog().add("UV:" + date, userId);
    }

    public Long getVisitorCount(String date) {
        return redisTemplate.opsForHyperLogLog().size("UV:" + date);
    }


}
