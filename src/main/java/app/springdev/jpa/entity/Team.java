package app.springdev.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;
@Data
@Entity
public class Team {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    @BatchSize(size = 3)
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
}
