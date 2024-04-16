package com.abhi.questionservice.dao;

import com.abhi.questionservice.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question, Integer> {
    List<Question> findByCategory(String category);

    @Query(value = "select q.id from Question q where q.category=:category ORDER BY RAND() LIMIT :num", nativeQuery = true)
    List<Integer> findRandomQuestionsByCategory(String category, int num);
}
