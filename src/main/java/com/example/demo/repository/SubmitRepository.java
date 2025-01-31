package com.example.demo.repository;

import com.example.demo.model.Submit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmitRepository extends MongoRepository<Submit, String> {
    List<Submit> findByUserIdAndQuestionId(String userId, String questionId);
    List<Submit> findByUserId(String userId);
    List<Submit> findByQuestionId(String questionId);
}