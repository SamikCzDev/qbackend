package me.michalkunc.qbackend;

import me.michalkunc.qbackend.dtos.ResponseDTO;
import me.michalkunc.qbackend.mysql.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HttpController {
    private final UserRepository userRepository;
    private final CookieRepository cookieRepository;

    private final BadAnsRepository badAnsRepository;

    private final GoodAnsRepository goodAnsRepository;

    private final QuestionRepository questionRepository;

    private final TestHistoryRepository testHistoryRepository;


    public HttpController(UserRepository userRepository, CookieRepository cookieRepository, BadAnsRepository badAnsRepository, GoodAnsRepository goodAnsRepository, QuestionRepository questionRepository, TestHistoryRepository testHistoryRepository) {
        this.userRepository = userRepository;
        this.cookieRepository = cookieRepository;
        this.badAnsRepository = badAnsRepository;
        this.goodAnsRepository = goodAnsRepository;
        this.questionRepository = questionRepository;
        this.testHistoryRepository = testHistoryRepository;
    }

    @PostMapping(path = "/login")
    public ResponseDTO login(@RequestBody User user) {


        return new ResponseDTO(200,"Test");
    }
}
