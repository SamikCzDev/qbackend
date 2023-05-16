package me.michalkunc.qbackend.mysql;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "qbac_badAns")
@Data
public class BadAns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Question question;

    private LocalDate time;

    @ManyToOne
    private TestHistory testHistory;

}