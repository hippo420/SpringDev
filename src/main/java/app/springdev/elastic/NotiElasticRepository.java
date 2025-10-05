package app.springdev.elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface NotiElasticRepository  extends ElasticsearchRepository<NoticeDocument, Long> {
}
