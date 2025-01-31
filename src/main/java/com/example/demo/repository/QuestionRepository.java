package com.example.demo.repository;

import com.example.demo.model.Difficulty;
import com.example.demo.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {
    List<Question> findByCategoryId(String categoryId);
    List<Question> findByDifficulty(Difficulty difficulty);
    List<Question> findByCategoryIdAndDifficulty(String categoryId, Difficulty difficulty);
}