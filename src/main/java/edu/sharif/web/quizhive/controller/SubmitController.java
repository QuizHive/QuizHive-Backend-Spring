package edu.sharif.web.quizhive.controller;

import edu.sharif.web.quizhive.dto.requestdto.CreateSubmitDTO;
import edu.sharif.web.quizhive.dto.resultdto.SubmitDTO;
import edu.sharif.web.quizhive.model.LoggedInUser;
import edu.sharif.web.quizhive.service.SubmitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions/submit")
@RequiredArgsConstructor
public class SubmitController {

	private final SubmitService submitService;

	@Operation(summary = "Submit an answer to a question", responses = {
			@ApiResponse(responseCode = "200", description = "Answer submitted successfully"),
			@ApiResponse(responseCode = "404", description = "Question or User not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@PostMapping
	@PreAuthorize("hasRole('PLAYER')")
	public ResponseEntity<SubmitDTO> submitAnswer(@Valid @RequestBody CreateSubmitDTO dto,
	                                              @AuthenticationPrincipal LoggedInUser user) {
		SubmitDTO submit = submitService.submitAnswer(user.get(), dto.getQuestionId(), dto.getChoice());
		return ResponseEntity.ok(submit);
	}

	@Operation(summary = "Retrieve submissions based on filters", responses = {
			@ApiResponse(responseCode = "200", description = "List of submissions"),
			@ApiResponse(responseCode = "403", description = "Forbidden"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@GetMapping("/submissions")
	@PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')")
	public ResponseEntity<List<SubmitDTO>> getSubmissions(
			@AuthenticationPrincipal LoggedInUser user,
			@RequestParam(required = false) String questionId
	) {
		String userId = user != null ? user.get().getId() : null;
		List<SubmitDTO> submissions = submitService.getSubmissions(userId, questionId);
		return ResponseEntity.ok(submissions);
	}
}
