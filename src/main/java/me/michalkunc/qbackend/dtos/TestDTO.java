package me.michalkunc.qbackend.dtos;

import lombok.Data;
import me.michalkunc.qbackend.cons.QuestionCon;
import me.michalkunc.qbackend.managers.TestManager;
import me.michalkunc.qbackend.mysql.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class TestDTO {
    private int status;

    private int testID;

    private List<QuestionCon> questionList = new ArrayList<>();

    public TestDTO(int status) {
        this.status = status;
    }
    public TestDTO(int status, TestManager testManager) {
        this.testID = testManager.getTestHistory().getId();
        this.status = status;
        for(Question question : testManager.getToSend()) {
            List<String> answers = new ArrayList<>();
            answers.add(question.getAns1());
            answers.add(question.getAns2());
            answers.add(question.getAns3());
            answers.add(question.getAnsCorr());
            Collections.shuffle(answers);
            questionList.add(new QuestionCon(question.getId(),question.getQuestion(),question.getLevel(),answers));
        }
    }
}
