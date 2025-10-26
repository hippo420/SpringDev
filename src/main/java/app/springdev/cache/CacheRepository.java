package app.springdev.cache;

import app.springdev.excel.cache.entity.StockStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface CacheRepository extends JpaRepository<StockStatus, Long> {

    List<StockStatus> findByStockName(String name);

    List<StockStatus> findByStockCode(String StockCode);

    List<StockStatus> findByChangeRateBetween(BigDecimal changeRateAfter, BigDecimal changeRateBefore);
}
