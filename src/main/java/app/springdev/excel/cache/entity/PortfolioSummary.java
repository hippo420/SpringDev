package app.springdev.excel.cache.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String assetType;       // 자산군 (예: 주식, 채권, 현금 등)
    private Double ratio;           // 비중(%)
    private Long currentValue;      // 현재가치
    private Double targetRatio;     // 목표비중
    private String rebalanceNeeded; // 리밸런싱 필요여부 (예: "필요", "필요없음")
}
