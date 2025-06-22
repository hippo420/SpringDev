package app.springdev.excel.repository;

import app.springdev.excel.entity.MedicalRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRepository  {
    int batchInsert(List<MedicalRecord> entities);

    int batchInsertMedicalRecords(@Param("list")List<MedicalRecord> records);

    int batchInsertMedicalRecord(MedicalRecord record);
}
