package app.springdev.redis.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisSortedSetService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    public void updateScore(String gameId, String userId, double score) {
        redisTemplate.opsForZSet().add("RANK:" + gameId, userId, score);
    }

    public Set<ZSetOperations.TypedTuple<String>> getTopUsers(String gameId) {
        return redisTemplate.opsForZSet().reverseRangeWithScores("RANK:" + gameId, 0, 9);
    }

}
