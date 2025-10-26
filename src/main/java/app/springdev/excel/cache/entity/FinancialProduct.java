package app.springdev.excel.cache.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;     // 상품명
    private Double interestRate;    // 금리
    private Long minAmount;         // 최소가입금액
    private String maturity;        // 만기 (예: "1년", "6개월")
    private String riskLevel;       // 위험등급 (예: "낮음", "보통", "높음")
}
