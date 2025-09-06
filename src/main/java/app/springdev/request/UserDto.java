package app.springdev.request;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
