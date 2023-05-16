package me.michalkunc.qbackend;

import jakarta.servlet.http.HttpServletResponse;
import me.michalkunc.qbackend.dtos.ResponseDTO;
import me.michalkunc.qbackend.mysql.*;
import me.michalkunc.qbackend.utils.CookieUtils;
import me.michalkunc.qbackend.utils.Sha256;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.Cookie;


import java.net.http.HttpResponse;

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
}
