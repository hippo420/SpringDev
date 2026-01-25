package app.springdev.scheduler.repository;

import app.springdev.scheduler.entity.BatchJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BatchJobRepository extends JpaRepository<BatchJob,Long> {
    @Query("""
        select h from BatchJob h
        where h.isActive = 'Y'
    """)
    List<BatchJob> findByIsActiveTrue();
}
