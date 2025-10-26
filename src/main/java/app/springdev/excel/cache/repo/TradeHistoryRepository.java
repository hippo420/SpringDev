package app.springdev.excel.cache.repo;

import app.springdev.excel.cache.entity.FinancialProduct;
import app.springdev.excel.cache.entity.StockStatus;
import app.springdev.excel.cache.entity.TradeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TradeHistoryRepository extends JpaRepository<TradeHistory, Integer> {
    Optional<TradeHistory> findById(Long id);
}
