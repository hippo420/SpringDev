package app.springdev.excel.repository;

import app.springdev.excel.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalJpaRepository extends JpaRepository<MedicalRecord,Long> {
}
