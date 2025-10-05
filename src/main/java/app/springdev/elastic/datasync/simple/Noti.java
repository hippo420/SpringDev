package app.springdev.elastic.datasync.simple;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class Noti {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String content;

    private String writer;

    private String category;

    private Integer views;

    private String createdAt;

    @Override
    public String toString() {
        return "Noti{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", writer='" + writer + '\'' +
                ", category='" + category + '\'' +
                ", views='" + views + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
