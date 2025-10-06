package app.springdev.objectmapping.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExtraDto {
    private String created;
    private String updated;

    public ExtraDto(String created, String updated) {
        this.created = created;
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "ExtraDto{" +
                "created='" + created + '\'' +
                ", updated='" + updated + '\'' +
                '}';
    }
}
