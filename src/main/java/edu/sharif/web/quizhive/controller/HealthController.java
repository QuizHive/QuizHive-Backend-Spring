package edu.sharif.web.quizhive.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Health", description = "Health check endpoint for monitoring service availability")
public class HealthController {
	@Operation(summary = "Health Check", description = "Returns OK if the service is running.")
	@ApiResponse(responseCode = "200", description = "Service is running")
	@ApiResponse(responseCode = "500", description = "Service is down")
	@GetMapping("/health")
	public ResponseEntity<String> healthCheck() {
		return ResponseEntity.ok("OK");
	}
}
