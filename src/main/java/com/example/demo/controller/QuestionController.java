package com.example.demo.controller;

import com.example.demo.dto.CreateCategoryDTO;
import com.example.demo.dto.CreateQuestionDTO;
import com.example.demo.model.Category;
import com.example.demo.model.Question;
import com.example.demo.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    /**
     * GET /questions/categories
     * Retrieve all categories.
     */
    @Operation(summary = "Get all categories", responses = {
            @ApiResponse(responseCode = "200", description = "List of categories"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = questionService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * POST /questions/category
     * Create a new category.
     * Accessible by ADMIN role only.
     */
    @Operation(summary = "Create a new category (Admin only)", responses = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "409", description = "Category already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/category")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CreateCategoryDTO dto) {
        Category category = questionService.createCategory(dto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    /**
     * DELETE /questions/categories/{id}
     * Delete a category by ID.
     * Accessible by ADMIN role only.
     */
    @Operation(summary = "Delete a category by ID (Admin only)", responses = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        questionService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    /**
     * GET /questions
     * Retrieve all questions with optional filters.
     */
    @Operation(summary = "Get all questions with optional filters", responses = {
            @ApiResponse(responseCode = "200", description = "List of questions"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Question>> getQuestions(
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) com.example.demo.model.Difficulty difficulty,
            @RequestParam(required = false) Integer limit
    ) {
        List<Question> questions = questionService.getQuestions(categoryId, difficulty, limit);
        return ResponseEntity.ok(questions);
    }

    /**
     * GET /questions/{id}
     * Retrieve a question by ID.
     */
    @Operation(summary = "Get a question by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Question details"),
            @ApiResponse(responseCode = "404", description = "Question not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable String id) {
        Question question = questionService.getQuestionById(id);
        return ResponseEntity.ok(question);
    }

    /**
     * POST /questions
     * Create a new question.
     * Accessible by ADMIN role only.
     */
    @Operation(summary = "Create a new question (Admin only)", responses = {
            @ApiResponse(responseCode = "201", description = "Question created successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Question> createQuestion(@Valid @RequestBody CreateQuestionDTO dto) {
        Question question = questionService.createQuestion(dto);
        return new ResponseEntity<>(question, HttpStatus.CREATED);
    }

    /**
     * DELETE /questions/{id}
     * Delete a question by ID.
     * Accessible by ADMIN role only.
     */
    @Operation(summary = "Delete a question by ID (Admin only)", responses = {
            @ApiResponse(responseCode = "200", description = "Question deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Question not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteQuestion(@PathVariable String id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok().build();
    }
}
