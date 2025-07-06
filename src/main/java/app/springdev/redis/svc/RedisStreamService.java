package app.springdev.redis.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RedisStreamService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void sendMessage(String roomId, String userId, String message) {
        Map<String, String> map = new HashMap<>();
        map.put("user", userId);
        map.put("message", message);
        redisTemplate.opsForStream().add(StreamRecords.mapBacked(map).withStreamKey("STREAM:ROOM:" + roomId));
    }

}
