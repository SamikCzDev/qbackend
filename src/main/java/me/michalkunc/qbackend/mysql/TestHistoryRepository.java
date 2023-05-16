package me.michalkunc.qbackend.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestHistoryRepository extends JpaRepository<TestHistory,Integer> {
    TestHistory findById(int id);
    TestHistory findByUserId(int userId);
}
