package me.michalkunc.qbackend.cons;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QuestionCon {

    private int id;
    private String question;
    private List<String> answers;
}
