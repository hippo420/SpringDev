package app.springdev.system.batch;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiConsumer;

@Component
@RequiredArgsConstructor
public class MyBatisBatchExecutor {
    private final SqlSessionFactory sqlSessionFactory;

    /**
     * MyBatis 배치 처리 공통 메서드
     *
     * @param list 처리할 리스트
     * @param batchSize 배치 사이즈
     * @param mapperClass Mapper 클래스
     * @param callback Mapper와 항목을 받아서 수행할 작업 (예: mapper.insert(item))
     * @param <T> 데이터 타입
     * @param <M> Mapper 타입
     */
    public <T, M> void executeBatch(
            List<T> list,
            int batchSize,
            Class<M> mapperClass,
            BiConsumer<M, T> callback
    ) {
        try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
            M mapper = session.getMapper(mapperClass);

            for (int i = 0; i < list.size(); i++) {
                callback.accept(mapper, list.get(i));

                if (i % batchSize == 0 && i > 0) {
                    session.flushStatements();
                    session.clearCache();
                    System.out.println("==> flush at i = " + i);
                }
            }

            session.flushStatements();
            session.commit();
        } catch (Exception e) {
            throw new RuntimeException("MyBatis 배치 처리 중 오류 발생", e);
        }
    }
}
