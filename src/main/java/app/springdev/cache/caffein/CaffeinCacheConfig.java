package app.springdev.cache.caffein;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

//@Configuration
//@EnableCaching
public class CaffeinCacheConfig {
    //@Bean
    public CacheManager CaffeinCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("caf-stocks");
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(1000)
                        .expireAfterWrite(10, TimeUnit.MINUTES)
        );
        return cacheManager;
    }
}
