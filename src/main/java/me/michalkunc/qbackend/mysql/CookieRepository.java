package me.michalkunc.qbackend.mysql;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CookieRepository extends JpaRepository<Cookie,Integer> {
    Cookie findByCookie(String cookie);
    Cookie findByUserId(int userId);
    @Transactional
    void deleteAllByUserId(int userId);
}
