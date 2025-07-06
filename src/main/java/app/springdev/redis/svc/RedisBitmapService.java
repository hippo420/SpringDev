package app.springdev.redis.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisBitmapService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void markAttendance(String date, int userIndex) {
        redisTemplate.opsForValue().setBit("ATTEND:" + date, userIndex, true);
    }

    public boolean hasAttended(String date, int userIndex) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().getBit("ATTEND:" + date, userIndex));
    }


}
