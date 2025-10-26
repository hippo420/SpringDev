package app.springdev.cache.simple;

import app.springdev.cache.CacheRepository;
import app.springdev.excel.cache.entity.StockStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Component
@AllArgsConstructor
public class SimpleCache {
    private final Map<String, List<StockStatus>> cache = new HashMap<>();

    private final CacheRepository cacheRepository;

    // DB 조회 시뮬레이션
    private List<StockStatus> queryFromDB(String key) {
        log.info("[DB 조회] key={}", key);
        return cacheRepository.findByStockName(key);

    }


    // 캐시 조회 메서드
    public List<StockStatus> getData(String key) {
        if (cache.containsKey(key)) {
            log.info("[Cache Hit] key={}", key);
            log.info("[Cache Hit] data={}", cache.get(key));
            return cache.get(key);
        }

        log.info("[Cache Miss] key={}", key);
        log.info("[Cache  Set] key={}", key);
        log.info("[Cache  Content]");
        List<StockStatus> value = cacheRepository.findByStockName(key);
        value.stream().forEach(stockStatus -> {log.info("stockStatus={}", stockStatus);});
        cache.put(key, value);
        return value;
    }

    // 캐시 삭제
    public void updData(String stockNm, Long updPrice)
    {
        if (cache.containsKey(stockNm)) {
            log.info("변경전 [Cache data] = {}", cache.get(stockNm));
            log.info("[Cache 삭제] key = {}", stockNm);
            cache.remove(stockNm);
        }

        List<StockStatus> value = cacheRepository.findByStockName(stockNm);
        value.get(0).setCurrentPrice(updPrice);
        cacheRepository.saveAll(value);
    }

}
