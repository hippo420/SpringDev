package app.springdev.objectmapping.dto;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class SourceDto {
    private Long id;
    private String title;
    private String contents;
    private String sender;
    private String receiver;
    private Integer views;
    private String uniqueValue;
    private String company;

    @Override
    public String toString() {
        return "SourceDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", views=" + views +
                ", uniqueValue='" + uniqueValue + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
}
