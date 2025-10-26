package app.springdev.excel.cache.repo;

import app.springdev.excel.cache.entity.AccountBalance;
import app.springdev.excel.cache.entity.FinancialProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FinancialProductRepository extends JpaRepository<FinancialProduct, Long> {
    Optional<FinancialProduct> findById(Long id);
}
