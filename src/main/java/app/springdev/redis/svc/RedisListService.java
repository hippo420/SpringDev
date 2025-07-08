package app.springdev.redis.svc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
@Slf4j
@Service
public class RedisListService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void pushMessage(String message, String pushType) {
        String roomId = Math.random()%2 == 0?"1":"2";

        String key = "CHAT:" + roomId;
        log.info("CHAT [{}] - pushMessage: {} - direction [{}]" ,roomId, message, pushType);
        if("R".equals(pushType))
            redisTemplate.opsForList().rightPush(key, message);
        else
            redisTemplate.opsForList().leftPush(key, message);

        redisTemplate.opsForList().trim(key, -100, -1); // 최근 100개만 유지
    }
}
