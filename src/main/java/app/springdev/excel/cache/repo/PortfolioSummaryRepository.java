package app.springdev.excel.cache.repo;

import app.springdev.excel.cache.entity.FinancialProduct;
import app.springdev.excel.cache.entity.PortfolioSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioSummaryRepository extends JpaRepository<PortfolioSummary, Long> {
    Optional<PortfolioSummary> findById(Long id);
}
