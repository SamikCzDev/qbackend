package me.michalkunc.qbackend.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BadAnsRepository extends JpaRepository<BadAns,Integer> {
    List<BadAns> findTop3ByUserId(int userId);
}
