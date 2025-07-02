package app.springdev.virtualthread.svc;

import app.springdev.system.util.MemoryUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Slf4j
@Service
public class VirtualThreadService {
    public void execute() {
        int taskCount = 100;

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            CountDownLatch latch = new CountDownLatch(taskCount);

            long startMem = MemoryUtil.usedMemoryInMB();
            long start = System.currentTimeMillis();

            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    try {
                        Thread.sleep(1000); // 100ms
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();

            long end = System.currentTimeMillis();
            long endMem = MemoryUtil.usedMemoryInMB();
            log.info("🪶 Virtual threads elapsed time: {} ms", (end - start));
            log.info("🪶 Virtual threads memory usage: {} MB",(endMem - startMem));
        }catch (InterruptedException e) {
            log.error("Thread 실행중 오류발생 => {}",e.getMessage());
        }


    }
}
