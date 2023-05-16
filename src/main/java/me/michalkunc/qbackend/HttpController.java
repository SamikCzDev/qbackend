package me.michalkunc.qbackend;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.michalkunc.qbackend.cons.AnswerCon;
import me.michalkunc.qbackend.dtos.ResponseDTO;
import me.michalkunc.qbackend.dtos.TestDTO;
import me.michalkunc.qbackend.managers.AnswerManager;
import me.michalkunc.qbackend.managers.TestManager;
import me.michalkunc.qbackend.mysql.*;
import me.michalkunc.qbackend.utils.CookieUtils;
import me.michalkunc.qbackend.utils.Sha256;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HttpController {
    private final UserRepository userRepository;
    private final CookieRepository cookieRepository;

    private final BadAnsRepository badAnsRepository;

    private final GoodAnsRepository goodAnsRepository;

    private final QuestionRepository questionRepository;

    private final TestHistoryRepository testHistoryRepository;

    private final AnswerManager answerManager;

    private final List<Question> lvl1Q;

    private final List<Question> lvl2Q;


    public HttpController(UserRepository userRepository, CookieRepository cookieRepository, BadAnsRepository badAnsRepository, GoodAnsRepository goodAnsRepository, QuestionRepository questionRepository, TestHistoryRepository testHistoryRepository) {
        this.userRepository = userRepository;
        this.cookieRepository = cookieRepository;
        this.badAnsRepository = badAnsRepository;
        this.goodAnsRepository = goodAnsRepository;
        this.questionRepository = questionRepository;
        this.testHistoryRepository = testHistoryRepository;

        this.answerManager = new AnswerManager(badAnsRepository,goodAnsRepository,questionRepository,testHistoryRepository);

        lvl1Q = questionRepository.findAllByLevel(1);
        lvl2Q = questionRepository.findAllByLevel(2);
    }

    @PostMapping(path = "/login")
    public ResponseDTO login(@RequestBody User userProvide, HttpServletResponse response) {
        User user = userRepository.findByUserName(userProvide.getUserName());
        System.out.println(Sha256.getSha256("123"));
        if(user == null || !user.getPass().equals(Sha256.getSha256(userProvide.getPass()))) {
            response.setStatus(404);
            return new ResponseDTO(404, "Not login!");
        }
        String cookieStr = CookieUtils.generateCookieForUser();

        Cookie cookie = new Cookie("session", cookieStr);

        me.michalkunc.qbackend.mysql.Cookie cookie1 = new me.michalkunc.qbackend.mysql.Cookie();
        cookie1.setCookie(cookieStr);
        cookie1.setUser(user);

        cookieRepository.deleteAllByUserId(user.getId());

        cookieRepository.save(cookie1);

        response.addCookie(cookie);
        response.setStatus(200);
        return new ResponseDTO(200, "Log sec!");
    }

    @GetMapping(path = "/genTest")
    public TestDTO genTest(HttpServletResponse response) {
        User user = userRepository.findById(2).orElseThrow();
        response.setStatus(200);
        if (user.getLevel() == 1) {
            return new TestDTO(200,new TestManager(user,badAnsRepository,goodAnsRepository,testHistoryRepository,lvl1Q));

        } else {
            return new TestDTO(200,new TestManager(user,badAnsRepository,goodAnsRepository,testHistoryRepository,lvl2Q));
        }
    }
    @PostMapping(path = "/sendAnswer")
    public ResponseDTO sendAnswer(@RequestBody AnswerCon answerCon, HttpServletResponse response, HttpServletRequest request) {
        if(request.getCookies() == null) {
            response.setStatus(401);
            return new ResponseDTO(401,"Not login!");
        }
        String cookie = CookieUtils.getCookieValue(request,"session");
        if(cookieRepository.findByCookie(cookie) == null) {
            response.setStatus(401);
            return new ResponseDTO(401,"Not login!");
        }
        int userId = cookieRepository.findByCookie(cookie).getUser().getId();
        if(userId == 0) {
            response.setStatus(401);
            return new ResponseDTO(401,"Not login!");
        }

        User user = userRepository.findById(userId).orElseThrow();

        if(answerManager.checkAns(answerCon,user)) {
            response.setStatus(200);
            return new ResponseDTO(200,"OK!");
        } else {
            response.setStatus(406);
            return new ResponseDTO(406,"Not accetable!");
        }


    }
}
