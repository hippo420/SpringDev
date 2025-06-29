package app.springdev.virtualthread.svc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Slf4j
@Service
public class VirtualThreadService {
    public void execute() {
        int taskCount = 10_000;
        long start = System.currentTimeMillis();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            CountDownLatch latch = new CountDownLatch(taskCount);

            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    try {
                        Thread.sleep(100); // 100ms
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();

        }catch (InterruptedException e) {
            log.error("Thread 실행중 오류발생 => {}",e.getMessage());
        }

        long end = System.currentTimeMillis();
        System.out.println("🪶 Virtual threads elapsed time: " + (end - start) + " ms");
    }
}
