package me.michalkunc.qbackend.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodAnsRepository extends JpaRepository<GoodAns,Integer> {
    GoodAns findById(int id);

    List<GoodAns> findTop3ByUserIdOrderByReturnTimeAsc(int userID);
}
