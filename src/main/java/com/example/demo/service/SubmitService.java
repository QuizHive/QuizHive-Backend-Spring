package com.example.demo.service;

import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Question;
import com.example.demo.model.Submit;
import com.example.demo.model.User;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.SubmitRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmitService {

    private final SubmitRepository submitRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    /**
     * Submit an answer to a question.
     * @param userId ID of the user.
     * @param questionId ID of the question.
     * @param choice User's choice index.
     * @return Created Submit record.
     */
    public Submit submitAnswer(String userId, String questionId, int choice) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Question not found."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));

        boolean isCorrect = (question.getCorrect() == choice);
        int gainedScore = isCorrect ? question.getDifficulty().getLevel() : 0;

        Submit submit = Submit.builder()
                .question(question)
                .user(user)
                .choice(choice)
                .isCorrect(isCorrect)
                .gainedScore(gainedScore)
                .timestamp(new Date())
                .build();

        submitRepository.save(submit);

        // Update User's score
        user.setScore(user.getScore() + gainedScore);
        userRepository.save(user);

        // Update Question's solve count
        if (isCorrect) {
            question.setSolves(question.getSolves() + 1);
            questionRepository.save(question);
        }

        return submit;
    }

    /**
     * Retrieve all submissions based on filters.
     * @param userId Optional user ID filter.
     * @param questionId Optional question ID filter.
     * @param isCorrect Optional correctness filter.
     * @param limit Optional limit on number of submissions.
     * @param after Optional start date filter.
     * @param before Optional end date filter.
     * @return List of submissions.
     */
    public List<Submit> getSubmissions(String userId, String questionId, Boolean isCorrect, Integer limit, Date after, Date before) {
        List<Submit> submissions;

        if (userId != null && questionId != null) {
            submissions = submitRepository.findByUserIdAndQuestionId(userId, questionId);
        } else if (userId != null) {
            submissions = submitRepository.findByUserId(userId);
        } else if (questionId != null) {
            submissions = submitRepository.findByQuestionId(questionId);
        } else {
            submissions = submitRepository.findAll();
        }

        if (isCorrect != null) {
            submissions = submissions.stream()
                    .filter(s -> s.isCorrect() == isCorrect)
                    .toList();
        }

        if (after != null) {
            submissions = submissions.stream()
                    .filter(s -> s.getTimestamp().after(after))
                    .toList();
        }

        if (before != null) {
            submissions = submissions.stream()
                    .filter(s -> s.getTimestamp().before(before))
                    .toList();
        }

        if (limit != null && limit > 0 && limit < submissions.size()) {
            submissions = submissions.subList(0, limit);
        }

        return submissions;
    }
}