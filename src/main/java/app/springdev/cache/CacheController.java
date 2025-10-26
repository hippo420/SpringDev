package app.springdev.cache;

import app.springdev.excel.cache.entity.StockStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("cache")
@Slf4j
@AllArgsConstructor
public class CacheController {

    private final CacheService cacheService;
    private final CacheManager cacheManager;
    /**
     * 1. 캐시
     */
    @RequestMapping("/no-simple")
    public void simpleNoCache(@RequestParam String key) {
        cacheService.simpleNoCache(key);
    }

    @RequestMapping("/simple")
    public void simpleCache(@RequestParam String key) {

        cacheService.simpleCache(key);
    }

    /**
     * 2. 스프링 캐시
     */
    @RequestMapping("/spring1")
    public void springCache1(@RequestParam String key) {
        cacheService.springCache1(key);
    }

    @RequestMapping("/spring2")
    public void springCache2(@RequestParam String key) {
        StockStatus stock = new StockStatus();
        stock.setStockName(key);
        cacheService.springCache2(stock);
    }

    @RequestMapping("/spring3")
    public void springCache3(@RequestParam BigDecimal from_rate, @RequestParam BigDecimal to_rate) {
        cacheService.springCache3(from_rate, to_rate);
    }

    @RequestMapping("/spring4")
    public void springCache4(@RequestParam BigDecimal from_rate, @RequestParam BigDecimal to_rate) {
        cacheService.springCache4(from_rate, to_rate);
    }

    @RequestMapping("/spring5")
    public void springCache5(@RequestParam String stockNm, @RequestParam String region) {
        cacheService.springCache5(stockNm, region);
    }

    @RequestMapping("/clear")
    public void springCacheClear() {
        cacheService.springCacheClear();
    }

    @RequestMapping("/clearAll")
    public void springCacheClearAll() {
        cacheService.springCacheClearAll();
    }

    @RequestMapping("/clear1")
    public void springCacheClear1(@RequestBody StockStatus stock) {
        cacheService.springCacheClear1(stock);
    }

    @RequestMapping("/status")
    public void printCacheStatus() {
        log.info("=== 캐시 상태 ===");
        cacheManager.getCacheNames().forEach(name -> {
            log.info("Cache name: {}" , name);
            Object nativeCache = cacheManager.getCache(name).getNativeCache();
            log.info(" -> Native cache: {}" , nativeCache);
        });
    }
}
