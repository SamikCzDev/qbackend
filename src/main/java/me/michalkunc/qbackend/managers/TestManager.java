package me.michalkunc.qbackend.managers;

import lombok.Data;
import me.michalkunc.qbackend.mysql.*;
import org.json.JSONArray;
import org.json.JSONObject;


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

    public TestManager(User user, BadAnsRepository badAnsRepository, GoodAnsRepository goodAnsRepository, TestHistoryRepository testHistoryRepository, List<Question> questions) {
        testHistory = new TestHistory();

        testHistory.setUser(user);
        testHistory.setCorrCount(0);
        testHistory.setRecivesAns(0);



        bad = badAnsRepository.findTop3ByUserId(user.getId());
        expire = goodAnsRepository.findTop3ByUserIdOrderByReturnTimeAsc(user.getId());
        this.questions = questions;
        Collections.shuffle(this.questions);

        for (BadAns badAns : bad) {
            toSend.add(badAns.getQuestion());
        }
        for (GoodAns goodAns : expire) {
            if (ChronoUnit.DAYS.between(goodAns.getReturnTime(), LocalDate.now()) > 7) {
                toSend.add(goodAns.getQuestion());
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

        JSONArray ja = new JSONArray();
        int count = 1;
        for (Question q : toSend) {
            JSONObject ids = new JSONObject();
            ids.put(String.valueOf(count),q.getId());
            count++;
            ja.put(ids);
        }
        testHistory.setAllowIds(ja.toString());
        testHistoryRepository.save(testHistory);

    }
}
