package app.springdev.elastic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(indexName = "notice_index", createIndex = true) // Elasticsearch의 인덱스 이름 지정
public class NoticeDocument {

    @Id // Elasticsearch 도큐먼트의 ID (PostgreSQL의 BOARD_ID를 사용)
    private Long id;

    // 제목 필드: 텍스트 분석하여 검색 (Analyzer 설정 가능)
    @Field(type = FieldType.Text, name = "title")
    private String title;

    // 내용 필드: 텍스트 분석하여 검색
    @Field(type = FieldType.Text, name = "content")
    private String content;

    // 작성자, 카테고리 필드: 정확한 매칭/필터링 (분석 X)
    @Field(type = FieldType.Keyword, name = "writer")
    private String writer;

    @Field(type = FieldType.Keyword, name = "category")
    private String category;

    // 조회수, 좋아요는 검색 시 정렬 기준으로 사용 가능
    @Field(type = FieldType.Integer, name = "views")
    private Integer views;

    @Field(type = FieldType.Date, name = "created_at")
    private String createdAt;

    @Override
    public String toString() {
        return "NoticeDocument{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", writer='" + writer + '\'' +
                ", category='" + category + '\'' +
                ", views=" + views +
                ", createdAt=" + createdAt +
                '}';
    }
}
