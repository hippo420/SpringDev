package app.springdev.objectmapping.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TargetDto {
    private Long id;
    private String title;
    private String content;
    private String sender;
    private String receiver;
    private Integer views;
    private String created;
    private String updated;
    private String uniqueValue;

    private String add_param1;
    private String add_param2;

    private Company company;
    private String address;

    @Override
    public String toString() {
        return "TargetDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", views=" + views +
                ", created='" + created + '\'' +
                ", updated='" + updated + '\'' +
                ", uniqueValue='" + uniqueValue + '\'' +
                ", add_param1='" + add_param1 + '\'' +
                ", add_param2='" + add_param2 + '\'' +
                ", company=" + company +
                ", address=" + address +
                '}';
    }
}
