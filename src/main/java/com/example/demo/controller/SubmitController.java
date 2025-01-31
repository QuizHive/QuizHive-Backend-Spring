package com.example.demo.controller;

import com.example.demo.dto.CreateSubmitDTO;
import com.example.demo.model.Submit;
import com.example.demo.service.SubmitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/questions/submit")
@RequiredArgsConstructor
public class SubmitController {

    private final SubmitService submitService;

    /**
     * POST /questions/submit
     * Submit an answer to a question.
     * Accessible by authenticated users.
     */
    @Operation(summary = "Submit an answer to a question", responses = {
            @ApiResponse(responseCode = "200", description = "Answer submitted successfully"),
            @ApiResponse(responseCode = "404", description = "Question or User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    @PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')")
    public ResponseEntity<Submit> submitAnswer(@Valid @RequestBody CreateSubmitDTO dto,
                                               Authentication authentication) {
        String userId = authentication.getName(); // Assuming user ID is set as the principal name
        Submit submit = submitService.submitAnswer(userId, dto.getQuestionId(), dto.getChoice());
        return ResponseEntity.ok(submit);
    }

    /**
     * GET /questions/submit/submissions
     * Retrieve submissions based on filters.
     * Accessible by authenticated users.
     */
    @Operation(summary = "Retrieve submissions based on filters", responses = {
            @ApiResponse(responseCode = "200", description = "List of submissions"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/submissions")
    @PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')")
    public ResponseEntity<List<Submit>> getSubmissions(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String questionId,
            @RequestParam(required = false) Boolean isCorrect,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) Date after,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) Date before,
            Authentication authentication
    ) {
        // Optionally, restrict users to view only their submissions unless ADMIN
        if (!authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) && userId != null && !authentication.getName().equals(userId)) {
            return ResponseEntity.status(403).build();
        }

        List<Submit> submissions = submitService.getSubmissions(userId, questionId, isCorrect, limit, after, before);
        return ResponseEntity.ok(submissions);
    }
}
