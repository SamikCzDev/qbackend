package me.michalkunc.qbackend.managers;

import lombok.Data;
import me.michalkunc.qbackend.mysql.*;
import org.json.JSONArray;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class TestManager {
    private List<BadAns> bad;

    private List<GoodAns> expire;

    private List<Question> questions;

    private List<Question> toSend = new ArrayList<>();

    private TestHistory testHistory;

    public TestManager(User user, BadAnsRepository badAnsRepository, GoodAnsRepository goodAnsRepository, TestHistoryRepository testHistoryRepository, List<Question> questions, boolean ignore) {

        testHistory = new TestHistory();

        testHistory.setUser(user);
        testHistory.setRecivesAns(0);



        bad = badAnsRepository.findTop3ByUserId(user.getId());
        expire = goodAnsRepository.findTop3ByUserIdOrderByReturnTimeAsc(user.getId());
        this.questions = questions;
        Collections.shuffle(this.questions);

        if(ignore) {
            int targetSize = 10;
            int missingEntities = targetSize - toSend.size();

            for (Question entity : questions) {
                if (!toSend.contains(entity)) {
                    toSend.add(entity);
                    missingEntities--;
                }

                if (missingEntities == 0) {
                    break;
                }
            }
        } else {
            for (BadAns badAns : bad) {
                toSend.add(badAns.getQuestion());
            }
            for (GoodAns goodAns : expire) {
                if(!(toSend.contains(goodAns.getQuestion()))) {
                    System.out.println(ChronoUnit.DAYS.between(goodAns.getTime(), LocalDate.now()));
                    if (ChronoUnit.DAYS.between(goodAns.getTime(), LocalDate.now()) > 30) {
                        toSend.add(goodAns.getQuestion());
                    }
                }

            }


            int targetSize = 10;
            int missingEntities = targetSize - toSend.size();

            for (Question entity : questions) {
                if (!toSend.contains(entity)) {
                    toSend.add(entity);
                    missingEntities--;
                }

                if (missingEntities == 0) {
                    break;
                }
            }
            Collections.shuffle(toSend);

            JSONArray ja = new JSONArray();
            for (Question q : toSend) {
                ja.put(String.valueOf(q.getId()));
            }
            testHistory.setAllowIds(ja.toString());
            testHistoryRepository.save(testHistory);


            int count = 0;
            for (GoodAns goodAns : goodAnsRepository.findAllByUserAndQuestionLevel(user,user.getLevel())) {
                if (!(ChronoUnit.DAYS.between(goodAns.getTime(), LocalDate.now()) > 30)) {
                    count++;
                }
            }
            if(count >= 15 && user.getLevel() == 1) {
                user.setLevel(2);
            } else if(count >= 15 && user.getLevel() == 2) {

            }
        }

    }
}
