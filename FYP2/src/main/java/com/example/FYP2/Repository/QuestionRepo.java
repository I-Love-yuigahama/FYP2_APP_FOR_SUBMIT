package com.example.FYP2.Repository;

import com.example.FYP2.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuestionRepo extends JpaRepository<Question, Integer> {


    Optional<Question> findById(Integer integer);

    @Query(value = "SELECT * FROM questions ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<Question> findRandomQuestions();

}
