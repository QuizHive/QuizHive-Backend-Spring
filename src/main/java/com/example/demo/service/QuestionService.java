package com.example.demo.service;

import com.example.demo.dto.CreateCategoryDTO;
import com.example.demo.dto.CreateQuestionDTO;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Category;
import com.example.demo.model.Difficulty;
import com.example.demo.model.Question;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;

    /**
     * Retrieve all categories.
     * @return List of categories.
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Create a new category.
     * @param dto Data Transfer Object containing category details.
     * @return Created Category.
     */
    public Category createCategory(CreateCategoryDTO dto) {
        categoryRepository.findByCategoryName(dto.getCategoryName()).ifPresent(c -> {
            throw new ConflictException("Category \"" + dto.getCategoryName() + "\" already exists.");
        });

        Category category = Category.builder()
                .categoryName(dto.getCategoryName())
                .description(dto.getDescription())
                .build();

        return categoryRepository.save(category);
    }

    /**
     * Delete a category by ID and cascade delete related questions.
     * @param categoryId ID of the category to delete.
     */
    public void deleteCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found."));

        // Cascade delete questions related to this category
        List<Question> questions = questionRepository.findByCategoryId(categoryId);
        questionRepository.deleteAll(questions);

        categoryRepository.delete(category);
    }

    /**
     * Create a new question under a category.
     * @param dto Data Transfer Object containing question details.
     * @return Created Question.
     */
    public Question createQuestion(CreateQuestionDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found."));

        Question question = Question.builder()
                .title(dto.getTitle())
                .text(dto.getText())
                .options(dto.getOptions())
                .correct(dto.getCorrect())
                .category(category)
                .difficulty(dto.getDifficulty())
                .solves(0)
                .createdAt(new Date())
                .build();

        return questionRepository.save(question);
    }

    /**
     * Retrieve all questions with optional filters.
     * @param categoryId Optional category ID filter.
     * @param difficulty Optional difficulty level filter.
     * @param limit Optional limit on number of questions.
     * @return List of questions.
     */
    public List<Question> getQuestions(String categoryId, Difficulty difficulty, Integer limit) {
        List<Question> questions;

        if (categoryId != null && difficulty != null) {
            questions = questionRepository.findByCategoryIdAndDifficulty(categoryId, difficulty);
        } else if (categoryId != null) {
            questions = questionRepository.findByCategoryId(categoryId);
        } else if (difficulty != null) {
            questions = questionRepository.findByDifficulty(difficulty);
        } else {
            questions = questionRepository.findAll();
        }

        if (limit != null && limit > 0 && limit < questions.size()) {
            questions = questions.subList(0, limit);
        }

        return questions;
    }

    /**
     * Retrieve a question by its ID.
     * @param questionId ID of the question.
     * @return Found Question.
     */
    public Question getQuestionById(String questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Question not found."));
    }

    /**
     * Delete a question by its ID.
     * @param questionId ID of the question to delete.
     */
    public void deleteQuestion(String questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Question not found."));
        questionRepository.delete(question);
    }
}