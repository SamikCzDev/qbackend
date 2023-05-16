package me.michalkunc.qbackend.mysql;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Integer> {

    Question findById(int id);

    List<Question> findAllByLevel(int level);
}
