package app.springdev.scheduler.svc;

import app.springdev.scheduler.entity.BatchJobLock;
import app.springdev.scheduler.repository.BatchJobLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BatchJobLockService {

    private final BatchJobLockRepository lockRepository;

    @Transactional
    public boolean tryLock(Long jobId) {
        BatchJobLock lock = lockRepository.findForUpdate(jobId)
                .orElseGet(() ->
                        lockRepository.save(
                                new BatchJobLock(jobId, false)
                        ));

        if (lock.isLocked()) {
            return false; // 이미 실행 중
        }

        String hostname ="undefined";

        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch ( UnknownHostException e) {
            throw new RuntimeException(e);
        }



        lock.setLocked(true);
        lock.setLockedAt(LocalDateTime.now());
        lock.setLockedBy(hostname);

        return true;
    }

    @Transactional
    public void unlock(Long jobId) {
        lockRepository.findById(jobId).ifPresent(lock -> {
            lock.setLocked(false);
            lock.setLockedAt(null);
            lock.setLockedBy(null);
        });
    }
}
