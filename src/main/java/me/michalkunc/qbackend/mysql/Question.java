package me.michalkunc.qbackend.mysql;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "qbac_questions")
@Data
public class Question {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String question;
    private String ansCorr;
    private String ans1;
    private String ans2;
    private String ans3;

    private int level;
}
