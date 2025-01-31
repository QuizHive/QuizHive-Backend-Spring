package edu.sharif.web.quizhive.controller;

import edu.sharif.web.quizhive.dto.requestdto.CreateQuestionDTO;
import edu.sharif.web.quizhive.dto.resultdto.CategoryDTO;
import edu.sharif.web.quizhive.dto.requestdto.CreateCategoryDTO;
import edu.sharif.web.quizhive.dto.requestdto.GetQuestionsDTO;
import edu.sharif.web.quizhive.dto.resultdto.QuestionDTO;
import edu.sharif.web.quizhive.model.LoggedInUser;
import edu.sharif.web.quizhive.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

	private final QuestionService questionService;

	@Operation(summary = "Get all categories", responses = {
			@ApiResponse(responseCode = "200", description = "List of categories"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@GetMapping("/categories")
	public ResponseEntity<List<CategoryDTO>> getAllCategories() {
		List<CategoryDTO> categories = questionService.getAllCategories();
		return ResponseEntity.ok(categories);
	}

	@Operation(summary = "Create a new category (Admin only)", responses = {
			@ApiResponse(responseCode = "201", description = "Category created successfully"),
			@ApiResponse(responseCode = "409", description = "Category already exists"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@PostMapping("/category")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CreateCategoryDTO dto) {
		CategoryDTO category = questionService.createCategory(dto);
		return new ResponseEntity<>(category, HttpStatus.CREATED);
	}

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

	@Operation(summary = "Get all questions with optional filters", responses = {
			@ApiResponse(responseCode = "200", description = "List of questions"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@GetMapping
	public ResponseEntity<List<QuestionDTO>> getQuestions(@AuthenticationPrincipal LoggedInUser user,
	                                                      GetQuestionsDTO filters) {
		List<QuestionDTO> questions = questionService.getQuestions(user, filters);
		return ResponseEntity.ok(questions);
	}

	@Operation(summary = "Get a question by ID", responses = {
			@ApiResponse(responseCode = "200", description = "Question details"),
			@ApiResponse(responseCode = "404", description = "Question not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@GetMapping("/{id}")
	public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable String id) {
		QuestionDTO question = questionService.getQuestionById(id);
		return ResponseEntity.ok(question);
	}

	@Operation(summary = "Create a new question (Admin only)", responses = {
			@ApiResponse(responseCode = "201", description = "Question created successfully"),
			@ApiResponse(responseCode = "404", description = "Category not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<QuestionDTO> createQuestion(@Valid @RequestBody CreateQuestionDTO dto) {
		QuestionDTO question = questionService.createQuestion(dto);
		return new ResponseEntity<>(question, HttpStatus.CREATED);
	}

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
