package app.springdev.excel.svc;

import app.springdev.excel.entity.MedicalRecord;
import app.springdev.excel.repository.MedicalJpaRepository;
import app.springdev.excel.repository.MedicalRepository;
import app.springdev.system.batch.MyBatisBatchExecutor;
import com.github.pjfanning.xlsx.StreamingReader;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelStreamService {

    private final EntityManager em;
    private final MedicalRepository medicalRepository;
    private final MedicalJpaRepository repository;
    private final SqlSessionFactory sqlSessionFactory;
    private final MyBatisBatchExecutor batchExecutor;
    @Transactional
    public void bulkInsert(List<MedicalRecord> entities) {

        int batchSize = 1000;

        for (int i = 0; i < entities.size(); i++) {
            em.persist(entities.get(i));

            if (i % batchSize == 0 && i > 0) {
                em.flush();     // DB 반영
                em.clear();     // 영속성 컨텍스트 비움 (메모리 줄임)
            }
        }

        // 마지막 flush
        em.flush();
        em.clear();
    }

    public void saveAll(List<MedicalRecord> entities) {
        repository.saveAll(entities);
    }

    public void  batchInsertMybatis(List<MedicalRecord> records) {
        int batchSize = 1000;

        for (int i = 0; i < records.size(); i += batchSize) {
            int end = Math.min(i + batchSize, records.size());
            List<MedicalRecord> subList = records.subList(i, end);
            medicalRepository.batchInsertMedicalRecords(subList);
        }
    }

    public void  batchInsertBatchExecutor(List<MedicalRecord> records) {
        int batchSize = 1000;

        try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
            MedicalRepository mapper = session.getMapper(MedicalRepository.class);

            for (int i = 0; i < records.size(); i++) {
                mapper.batchInsertMedicalRecord(records.get(i)); // 단건 호출 → 내부적으로 addBatch()

                if (i % batchSize == 0 && i > 0) {
                    session.flushStatements(); // JDBC executeBatch()
                    session.clearCache();
                    System.out.println("==> flush at i = " + i);
                }
            }

            session.flushStatements(); // 남은 거 실행
            session.commit(); // 트랜잭션 커밋
        } catch (Exception e) {
            throw new RuntimeException("MyBatis 배치 인서트 중 오류 발생", e);
        }
    }

    public void  batchInsertBatchExecutor1(List<MedicalRecord> records) {
        batchExecutor.executeBatch(
                records,
                1000,
                MedicalRepository.class,
                (repository, record) -> repository.batchInsertMedicalRecord(record)
        );


    }
}
