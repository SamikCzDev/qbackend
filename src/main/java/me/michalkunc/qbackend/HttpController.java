package me.michalkunc.qbackend;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HttpController {
    @PostMapping(path = "/login")
    public String gg() {
        return "Test";
    }
}
