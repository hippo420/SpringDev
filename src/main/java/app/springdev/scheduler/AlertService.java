package app.springdev.scheduler;

import app.springdev.scheduler.alert.AlertSender;
import app.springdev.scheduler.entity.BatchHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final List<AlertSender> senders;

    public void notifyStuckJob(BatchHistory h) {
        String msg = """
        배치명: %s
        시작시간: %s
        상태: %s
        """.formatted(
                h.getBatchJob().getJobName(),
                h.getStartTime(),
                h.getStatus()
        );

        senders.forEach(s ->
                s.send("🚨 배치 장시간 실행 감지", msg)
        );
    }

    public void notifyFailure(BatchHistory h) {
        senders.forEach(s ->
                s.send("❌ 배치 실패", h.getResultMessage())
        );
    }

    public void notifySlaBreach(BatchHistory history) {
        String msg = """
        ⚠\sSLA 초과
        - 배치명: %s
        - SLA시간: %s분
        - 실제시간: %s분
        """.formatted(
                history.getBatchJob().getJobName(),
                history.getBatchJob().getSlaMinutes(),
                Duration.between(history.getStartTime(), LocalDateTime.now()).toMinutes()
        );
    }
}