package me.michalkunc.qbackend.mysql;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "qbac_cookies")
@Data
public class Cookie {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String cookie;

    @OneToOne
    private User user;

}
