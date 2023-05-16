package me.michalkunc.qbackend;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.michalkunc.qbackend.cons.AnswerCon;
import me.michalkunc.qbackend.dtos.LevelDTO;
import me.michalkunc.qbackend.dtos.ResponseDTO;
import me.michalkunc.qbackend.dtos.TestDTO;
import me.michalkunc.qbackend.dtos.UserInfoDTO;
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
    private final List<Question> lvl3Q;
    private final List<Question> lvl4Q;
    private final List<Question> lvl5Q;
    private final List<Question> lvl6Q;
    private final List<Question> lvl7Q;


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
        lvl3Q = questionRepository.findAllByLevel(3);
        lvl4Q = questionRepository.findAllByLevel(4);
        lvl5Q = questionRepository.findAllByLevel(5);
        lvl6Q = questionRepository.findAllByLevel(6);
        lvl7Q = questionRepository.findAllByLevel(7);
    }

    @PostMapping(path = "/login")
    public ResponseDTO login(@RequestBody User userProvide, HttpServletResponse response) {
        System.out.println(userProvide.toString());
        User user = userRepository.findByUserName(userProvide.getUserName());
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

    @PostMapping(path = "/genTest")
    public TestDTO genTest(HttpServletResponse response,HttpServletRequest request) {
        if(request.getCookies() == null) {
            response.setStatus(401);
            return new TestDTO(401);
        }
        String cookie = CookieUtils.getCookieValue(request,"session");
        if(cookieRepository.findByCookie(cookie) == null) {
            response.setStatus(401);
            return new TestDTO(401);
        }
        int userId = cookieRepository.findByCookie(cookie).getUser().getId();
        if(userId == 0) {
            response.setStatus(401);
            return new TestDTO(401);
        }

        User user = userRepository.findById(userId).orElseThrow();
        response.setStatus(200);
        if (user.getLevel() == 1) {
            return new TestDTO(200,new TestManager(user,badAnsRepository,goodAnsRepository,testHistoryRepository,lvl1Q,false));

        } else {
            return new TestDTO(200,new TestManager(user,badAnsRepository,goodAnsRepository,testHistoryRepository,lvl2Q,false));
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

        int resp = answerManager.checkAns(answerCon,user);

        if(resp == 0) {
            response.setStatus(406);
            return new ResponseDTO(406,"Not accetable!");
        } else if(resp == 1) {
            response.setStatus(200);
            return new ResponseDTO(200,"Good");
        } else {
            response.setStatus(200);
            return new ResponseDTO(200,"Bad");
        }
    }

   @PostMapping(path = "/genForLevel")
    public TestDTO genForLevel(@RequestBody LevelDTO levelDTO, HttpServletResponse response, HttpServletRequest request) {
        System.out.println(levelDTO);
       if (request.getCookies() == null) {
           response.setStatus(401);
           return new TestDTO(401);
       }
       String cookie = CookieUtils.getCookieValue(request, "session");
       if (cookieRepository.findByCookie(cookie) == null) {
           response.setStatus(401);
           return new TestDTO(401);
       }
       int userId = cookieRepository.findByCookie(cookie).getUser().getId();
       if (userId == 0) {
           response.setStatus(401);
           return new TestDTO(401);
       }
       User user = userRepository.findById(userId).orElseThrow();
       int level = levelDTO.getLevel();

       if (level == 1) {
           return new TestDTO(200, new TestManager(user, badAnsRepository, goodAnsRepository, testHistoryRepository, lvl1Q, true));
       } else if (level == 2) {
           return new TestDTO(200, new TestManager(user, badAnsRepository, goodAnsRepository, testHistoryRepository, lvl2Q, true));
       } else if (level == 3) {
           return new TestDTO(200, new TestManager(user, badAnsRepository, goodAnsRepository, testHistoryRepository, lvl3Q, true));
       } else if (level == 4) {
           return new TestDTO(200, new TestManager(user, badAnsRepository, goodAnsRepository, testHistoryRepository, lvl4Q, true));
       } else if (level == 5) {
           return new TestDTO(200, new TestManager(user, badAnsRepository, goodAnsRepository, testHistoryRepository, lvl5Q, true));
       } else if (level == 6) {
           return new TestDTO(200, new TestManager(user, badAnsRepository, goodAnsRepository, testHistoryRepository, lvl6Q, true));
       } else {
           return new TestDTO(200, new TestManager(user, badAnsRepository, goodAnsRepository, testHistoryRepository, lvl7Q, true));
       }
    }
    @PostMapping(path = "/userInfo")
    public UserInfoDTO userInfo(HttpServletRequest request, HttpServletResponse response) {
        if(request.getCookies() == null) {
            response.setStatus(401);
            return new UserInfoDTO(401);
        }
        String cookie = CookieUtils.getCookieValue(request,"session");
        if(cookieRepository.findByCookie(cookie) == null) {
            response.setStatus(401);
            return new UserInfoDTO(401);
        }
        int userId = cookieRepository.findByCookie(cookie).getUser().getId();
        if(userId == 0) {
            response.setStatus(401);
            return new UserInfoDTO(401);
        }

        User user = userRepository.findById(userId).orElseThrow();
        return new UserInfoDTO(200,user.getId(),user.getUserName(),user.getLevel());
    }
}
