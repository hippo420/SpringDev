package app.springdev.elastic.datasync;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

    @ColumnDefault("PENDING")
    private String status;

    @Override
    public String toString() {
        return "Noti{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", writer='" + writer + '\'' +
                ", category='" + category + '\'' +
                ", views=" + views  +
                ", createdAt='" + createdAt + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
