package app.springdev.virtualthread.svc;

import app.springdev.system.util.MemoryUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class SimpleThreadService {
    public void execute() {
        try{

            int threadCount = 10_000;
            List<Thread> threads = new ArrayList<>(threadCount);

            long startMem = MemoryUtil.usedMemoryInMB();
            long start = System.currentTimeMillis();

            for (int i = 0; i < threadCount; i++) {
                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(100); // 100ms
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                threads.add(thread);
                thread.start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            long end = System.currentTimeMillis();
            long endMem = MemoryUtil.usedMemoryInMB();
            log.info("🧵 Platform threads elapsed time: {} ms",(end - start));
            log.info("🧵 Platform threads memory usage: {} MB",(endMem - startMem));

        }catch (InterruptedException e) {
            log.error("Thread 실행중 오류발생 => {}",e.getMessage());
        }

    }
}
