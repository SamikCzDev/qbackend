package me.michalkunc.qbackend.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodAnsRepository extends JpaRepository<GoodAns,Integer> {
    GoodAns findById(int id);

    GoodAns findEntityByQuestionAndUser(Question question, User user);

    List<GoodAns> findTop3ByUserIdOrderByReturnTimeAsc(int userID);
}
