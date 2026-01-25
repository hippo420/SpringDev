package app.springdev.scheduler.log;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;
/*
 * ThreadPoolTaskExecutor에서 사용가능 -> setTaskDecorator
 * ThreadPoolTaskScheduler -> 사용불가 -> Runnable 직접 감싸기
 */
@Deprecated
public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();

        return () -> {
            if (contextMap != null) {
                MDC.setContextMap(contextMap);
            }
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}