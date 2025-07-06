package app.springdev.redis.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RedisSetService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void likePost(String postId, String userId) {
        redisTemplate.opsForSet().add("LIKES:" + postId, userId);
    }

    public boolean hasLiked(String postId, String userId) {
        return redisTemplate.opsForSet().isMember("LIKES:" + postId, userId);
    }

}
