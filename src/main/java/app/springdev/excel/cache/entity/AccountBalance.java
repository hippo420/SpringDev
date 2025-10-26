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
public class AccountBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerId;    // 고객ID
    private String accountNo;     // 계좌번호
    private Long deposit;         // 예수금
    private Long evalAmount;      // 평가금액
    private String profitRate;    // 수익률 (예: "+5.2%")
}
