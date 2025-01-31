package edu.sharif.web.quizhive.service;

import edu.sharif.web.quizhive.dto.requestdto.CreateCategoryDTO;
import edu.sharif.web.quizhive.dto.requestdto.CreateQuestionDTO;
import edu.sharif.web.quizhive.dto.requestdto.GetQuestionsDTO;
import edu.sharif.web.quizhive.dto.resultdto.QuestionDTO;
import edu.sharif.web.quizhive.dto.resultdto.CategoryDTO;
import edu.sharif.web.quizhive.exception.BadRequestException;
import edu.sharif.web.quizhive.exception.ConflictException;
import edu.sharif.web.quizhive.exception.NotFoundException;
import edu.sharif.web.quizhive.model.Category;
import edu.sharif.web.quizhive.model.Difficulty;
import edu.sharif.web.quizhive.model.LoggedInUser;
import edu.sharif.web.quizhive.model.Question;
import edu.sharif.web.quizhive.repository.CategoryRepository;
import edu.sharif.web.quizhive.repository.QuestionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
	private final CategoryRepository categoryRepository;
	private final QuestionRepository questionRepository;

	public List<CategoryDTO> getAllCategories() {
		return categoryRepository.findAll().stream()
				.map(c -> CategoryDTO.builder()
						.id(c.getId())
						.categoryName(c.getCategoryName())
						.description(c.getDescription())
						.build())
				.toList();
	}

	public CategoryDTO createCategory(@Valid CreateCategoryDTO dto) {
		categoryRepository.findByCategoryName(dto.getCategoryName()).ifPresent(c -> {
			throw new ConflictException("Category \"" + dto.getCategoryName() + "\" already exists.");
		});

		Category category = Category.builder()
				.categoryName(dto.getCategoryName())
				.description(dto.getDescription())
				.build();

		category = categoryRepository.save(category);

		return CategoryDTO.builder()
				.id(category.getId())
				.categoryName(category.getCategoryName())
				.description(category.getDescription())
				.build();
	}

	public void deleteCategory(String categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new NotFoundException("Category not found."));
		// Cascade delete questions related to this category
		List<Question> questions = questionRepository.findByCategoryId(categoryId);
		questionRepository.deleteAll(questions);
		categoryRepository.delete(category);
	}

	public QuestionDTO createQuestion(CreateQuestionDTO dto) {
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

		question = questionRepository.save(question);

		return QuestionDTO.builder()
				.id(question.getId())
				.title(question.getTitle())
				.text(question.getText())
				.options(question.getOptions())
				.correct(question.getCorrect())
				.categoryId(question.getCategory().getId())
				.build();
	}

	public List<QuestionDTO> getQuestions(LoggedInUser user, GetQuestionsDTO dto) {
		List<Question> questions;
		String categoryId = dto.getCategoryId();
		Difficulty difficulty = dto.getDifficulty();
		boolean followedCreator = dto.isFollowedCreator();
		int limit = dto.getLimit();
		if (categoryId != null && difficulty != null) {
			questions = questionRepository.findByCategoryIdAndDifficulty(categoryId, difficulty);
		} else if (categoryId != null) {
			questions = questionRepository.findByCategoryId(categoryId);
		} else if (difficulty != null) {
			questions = questionRepository.findByDifficulty(difficulty);
		} else {
			questions = questionRepository.findAll();
		}

		if (followedCreator && user == null)
			throw new BadRequestException("User must be logged in to filter by followed creator.");

		questions = questions.stream()
				.filter(q -> !followedCreator || user.get().getFollowings().contains(q.getCreator()))
				.toList();

		if (limit > 0 && limit < questions.size()) {
			questions = questions.subList(0, limit);
		}

		return questions.stream()
				.map(q -> QuestionDTO.builder()
						.id(q.getId())
						.title(q.getTitle())
						.text(q.getText())
						.options(q.getOptions())
						.correct(q.getCorrect())
						.categoryId(q.getCategory().getId())
						.creator(q.getCreator().getNickname())
						.build())
				.toList();
	}

	public QuestionDTO getQuestionById(String questionId) {
		Question question = questionRepository.findById(questionId)
				.orElseThrow(() -> new NotFoundException("Question not found."));
		return QuestionDTO.builder()
				.id(question.getId())
				.title(question.getTitle())
				.text(question.getText())
				.options(question.getOptions())
				.correct(question.getCorrect())
				.categoryId(question.getCategory().getId())
				.creator(question.getCreator().getNickname())
				.build();
	}

	/**
	 * Delete a question by its ID.
	 *
	 * @param questionId ID of the question to delete.
	 */
	public void deleteQuestion(String questionId) {
		Question question = questionRepository.findById(questionId)
				.orElseThrow(() -> new NotFoundException("Question not found."));
		questionRepository.delete(question);
	}
}