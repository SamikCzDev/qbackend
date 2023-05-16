package me.michalkunc.qbackend.mysql;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
@Table(name = "qbac_users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String userName;

    @Column(columnDefinition = "integer default 1")
    private int level;
    private String pass;
}
