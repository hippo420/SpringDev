package app.springdev.session.redis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id",unique = true,nullable = false)
    private String userId;

    @Column(name="password",unique = true, nullable = false)
    private String password;

    @Column(name = "user_name")
    private String userName;

}
