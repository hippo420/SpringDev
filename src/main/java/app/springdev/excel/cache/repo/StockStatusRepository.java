package app.springdev.excel.cache.repo;

import app.springdev.excel.cache.entity.FinancialProduct;
import app.springdev.excel.cache.entity.StockStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockStatusRepository extends JpaRepository<StockStatus, Integer> {
    Optional<StockStatus> findById(Long id);
}
