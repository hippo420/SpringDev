package app.springdev.redis.svc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Slf4j
@Service
public class RedisStringService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public ResponseEntity saveToken(String userId, String token) {
        log.info("KEY:{}, VALUE:{}", "TOKEN:"+userId, token);
        redisTemplate.opsForValue().set("TOKEN:" + userId, token, Duration.ofMinutes(30));
        return ResponseEntity.ok("Token saved");
    }

    public List<Map<String, String>> getToken(String tokenKey)
    {
        Set<String> keys = redisTemplate.keys("TOKEN:"+tokenKey);
        List<Map<String, String>> results = new ArrayList<>();
        Map<String, String> entry = null;
        if (keys != null) {
            for (String key : keys) {
                String token = redisTemplate.opsForValue().get(key);
                entry = new HashMap<>();
                entry.put("userId", key.replace("TOKEN:", ""));
                entry.put("token", token);
                results.add(entry);
            }
        }
        if (entry != null) {
            entry.entrySet().stream().forEach(entry1 -> {log.info("KEY:{}, VALUE:{}", entry1.getKey(), entry1.getValue());});
        }else{
            log.info("KEY:{}, VALUE:{}", tokenKey, "null");
        }

        return results;
    }
}
