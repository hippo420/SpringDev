package app.springdev.redis.svc;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;

import java.util.List;

@Service
public class RedisGeospatialService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void addStore(String storeId, double lon, double lat) {
        redisTemplate.opsForGeo().add("STORE", new Point(lon, lat), storeId);
    }

    public List<Point> findNearby(double lon, double lat, double radiusKm) {
        GeoResults<RedisGeoCommands.GeoLocation<String>> results =
                redisTemplate.opsForGeo().radius("STORE"
                                , new Circle(new Point(lon, lat)
                                , new Distance(radiusKm, Metrics.KILOMETERS)));

        return results.getContent().stream()
                .map(GeoResult::getContent)
                .map(RedisGeoCommands.GeoLocation::getPoint)
                .collect(Collectors.toList());
    }

}
