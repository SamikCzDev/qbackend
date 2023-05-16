package me.michalkunc.qbackend.cons;

import lombok.Data;

@Data
public class AnswerCon {
    private int questionsID;
    private int testID;
    private String answer;
    private boolean training;
}
