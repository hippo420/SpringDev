package app.springdev.scheduler.job;

import app.springdev.scheduler.CustomBatchJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
@Slf4j
@Component("testJob") // DB의 Bean_Name 컬럼에 "salesSummaryJob" 저장
public class TestJob implements CustomBatchJob {
    @Override
    public void execute(Map<String, Object> params) throws Exception {
        log.info("매출 요약 배치 시작...");
        // 비즈니스 로직 (파라미터 활용)
        String targetDate = (String) params.get("targetDate");

        // 예외 발생 시뮬레이션
        if ("error".equals(targetDate)) {
            throw new RuntimeException("데이터 처리 중 오류 발생!");
        }

        log.info("매출 요약 배치 종료...");
    }
}
