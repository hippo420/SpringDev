package app.springdev.excel.cache.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stockCode;    // 종목코드 (예: A005930)
    private String stockName;    // 종목명 (예: 삼성전자)
    private Long currentPrice;   // 현재가
    private BigDecimal changeRate;   // 전일대비 (예: "+0.73%")
    private Long volume;         // 거래량
}