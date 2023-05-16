package me.michalkunc.qbackend.mysql;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "qbac_testHistory")
@Data
public class TestHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User user;

    private int recivesAns;

    private int corrCount;

    private String allowIds;

}
