package me.michalkunc.qbackend.managers;

import me.michalkunc.qbackend.cons.AnswerCon;
import me.michalkunc.qbackend.mysql.*;
import me.michalkunc.qbackend.utils.DateUtils;
import me.michalkunc.qbackend.utils.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;

public class AnswerManager {

    private final BadAnsRepository badAnsRepository;

    private final GoodAnsRepository goodAnsRepository;

    private final QuestionRepository questionRepository;

    private final TestHistoryRepository testHistoryRepository;

    public AnswerManager(BadAnsRepository badAnsRepository, GoodAnsRepository goodAnsRepository, QuestionRepository questionRepository, TestHistoryRepository testHistoryRepository) {
        this.badAnsRepository = badAnsRepository;
        this.goodAnsRepository = goodAnsRepository;
        this.questionRepository = questionRepository;
        this.testHistoryRepository = testHistoryRepository;
    }

    public boolean checkAns(AnswerCon answerCon, User user) {
        TestHistory testHistory = testHistoryRepository.findById(answerCon.getTestID());
        if(testHistory == null || testHistory.getRecivesAns() >= 10 || testHistory.getUser().getId() != user.getId()) {
            return false;
        }
        JSONArray ids = new JSONArray(testHistory.getAllowIds());
        if(!JsonUtils.jsonContains(String.valueOf(answerCon.getQuestionsID()),ids)) {
            return false;
        }
        Question question = questionRepository.findById(answerCon.getQuestionsID());
        if(question == null) {
            return false;
        }


        testHistory.setAllowIds(JsonUtils.removeFromJSON(String.valueOf(question.getId()),ids).toString());

        testHistory.setRecivesAns(testHistory.getRecivesAns()+1);

        if(question.getAnsCorr().equals(answerCon.getAnswer())) {
            GoodAns goodAns = goodAnsRepository.findEntityByQuestionAndUser(question,user);
            if(goodAns == null) {
                GoodAns goodAns2 = new GoodAns();
                goodAns2.setUser(user);
                goodAns2.setTime(LocalDate.now());
                goodAns2.setQuestion(question);
                goodAns2.setTestHistory(testHistory);
                goodAns2.setReturnTime(DateUtils.plusDayToToday(7));
                goodAnsRepository.save(goodAns2);
            } else {
                goodAns.setTime(LocalDate.now());
                goodAns.setTestHistory(testHistory);
                goodAns.setReturnTime(DateUtils.plusDayToToday(7));
                goodAnsRepository.save(goodAns);
            }

        } else {
            BadAns badAns = badAnsRepository.findEntityByQuestionAndUser(question,user);
            if(badAns == null) {
                BadAns badAns2 = new BadAns();
                badAns2.setUser(user);
                badAns2.setTime(LocalDate.now());
                badAns2.setQuestion(question);
                badAns2.setTestHistory(testHistory);
                badAnsRepository.save(badAns2);
            } else {
                badAns.setTime(LocalDate.now());
                badAns.setTestHistory(testHistory);
                badAnsRepository.save(badAns);
            }

        }
        testHistoryRepository.save(testHistory);
        return true;
    }
}
