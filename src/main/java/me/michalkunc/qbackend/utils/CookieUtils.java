package me.michalkunc.qbackend.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;


import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

public class CookieUtils {
    public static String getCookieValue(HttpServletRequest req, String cookieName) {
        return Arrays.stream(req.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
    public static String generateCookieForUser() {
        byte[] bytes = new byte[31];
        ThreadLocalRandom.current().nextBytes(bytes);
        String session = Base64.getEncoder().encodeToString(bytes);
        return Sha256.getSha256(session);
    }
}
