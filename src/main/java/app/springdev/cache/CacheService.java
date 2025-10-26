package app.springdev.cache;

import app.springdev.cache.simple.SimpleCache;
import app.springdev.excel.cache.entity.StockStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class CacheService {

    private final CacheRepository cacheRepository;


    /**Cache 직접 만들기 **/
    public void simpleCache(String key) {
        log.info("simpleCache적용 데이터 조회: {}", key);
        List<StockStatus> res = simpleCache.getData(key);
        log.info("Fetch 건수: {} 건", res.size());
    }

    public void simpleNoCache(String key) {
        log.info("simpleCache미적용 데이터 조회: {}", key);
        List<StockStatus> res = cacheRepository.findByStockName(key);
        log.info("Fetch 건수: {} 건", res.size());
    }

    /** Spring Cache 적용 **/
    private final SimpleCache simpleCache;
    //객체
    @Cacheable(value="stocks", key="#stockNm")
    public void springCache1(String stockNm) {
        log.info("SpringCache적용 데이터 조회 -  조건1:{}", stockNm);
        List<StockStatus> res = cacheRepository.findByStockName(stockNm);
        log.info("Fetch 건수: {} 건", res.size());
    }

    //객체 필드
    @Cacheable(value="stocks", key="#stock.stockName" ,condition = "#stock.id >= 100")
    public void springCache2(StockStatus stock) {
        log.info("SpringCache2적용 데이터 조회 -  조건1:{}", stock.getStockName());
        List<StockStatus> res = cacheRepository.findByStockName(stock.getStockName());
        log.info("Fetch 건수: {} 건", res.size());
    }

    //@Cacheable(value="stocks", key="#p0" ) //#a0 파라미터 순서로 제어
    @Cacheable(value="stocks", key="#p0" )
    public void springCache3(BigDecimal fromRate, BigDecimal toRate) {
        log.info("SpringCache3적용 데이터 조회 -  객체조건1:{}, 조건2:{}", fromRate,toRate);
        List<StockStatus> res = cacheRepository.findByChangeRateBetween(fromRate,toRate);
        log.info("Fetch 건수: {} 건", res.size());
    }

    //unless true일 때는 캐시하지 않음 (결과 기반)
    @Cacheable(value="stocks", key="#stock" ,unless = "#result == null or !#result.active")
    public void springCache4(BigDecimal fromRate, BigDecimal toRate) {
        log.info("SpringCache4적용 데이터 조회 -  객체조건1:{}, 조건2:{}", fromRate,toRate);
        List<StockStatus> res = cacheRepository.findByChangeRateBetween(fromRate,toRate);
        log.info("Fetch 건수: {} 건", res.size());
    }

    //@Cacheable(value = "users", key = "#userId + '_' + #region")
    @Cacheable(value="stocks", key="#stockNm + '_' + #region")
    public void springCache5(String StockNm,String region) {
        log.info("SpringCache5적용 데이터 조회 -  객체조건1:{}, 지역:{}", StockNm,region);
        List<StockStatus> res = cacheRepository.findByStockName(StockNm);
        log.info("Fetch 건수: {} 건", res.size());
    }


    @CacheEvict(value = "stocks", key = "#stockName")
    public void springCacheClear() {

    }

    @CacheEvict(value = "stocks", allEntries = true)
    public void springCacheClearAll() {

    }

    @CacheEvict(value = "stocks", key = "#stock.stockName")
    public void springCacheClear1(StockStatus stock) {
        cacheRepository.save(stock);
    }
}
