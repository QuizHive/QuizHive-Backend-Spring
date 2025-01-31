package edu.sharif.web.quizhive.service;

import edu.sharif.web.quizhive.dto.resultdto.SubmitDTO;
import edu.sharif.web.quizhive.exception.NotFoundException;
import edu.sharif.web.quizhive.model.Question;
import edu.sharif.web.quizhive.model.Submit;
import edu.sharif.web.quizhive.model.User;
import edu.sharif.web.quizhive.repository.QuestionRepository;
import edu.sharif.web.quizhive.repository.SubmitRepository;
import edu.sharif.web.quizhive.repository.UserRepository;
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

	public SubmitDTO submitAnswer(User user, String questionId, int choice) {
		Question question = questionRepository.findById(questionId)
				.orElseThrow(() -> new NotFoundException("Question not found."));

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

		return SubmitDTO.builder()
				.id(submit.getId())
				.questionId(questionId)
				.choice(choice)
				.isCorrect(isCorrect)
				.gainedScore(gainedScore)
				.timestamp((submit.getTimestamp().getTime()))
				.build();
	}

	public List<SubmitDTO> getSubmissions(String userId, String questionId) {
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

		return submissions.stream()
				.map(s -> SubmitDTO.builder()
						.id(s.getId())
						.questionId(s.getQuestion().getId())
						.choice(s.getChoice())
						.isCorrect(s.isCorrect())
						.gainedScore(s.getGainedScore())
						.timestamp((s.getTimestamp().getTime()))
						.build())
				.toList();
	}
}