package app.springdev.scheduler;

import java.util.Map;

public interface CustomBatchJob {
    // 파라미터를 받아 로직을 수행
    void execute(Map<String, Object> params) throws Exception;
}
