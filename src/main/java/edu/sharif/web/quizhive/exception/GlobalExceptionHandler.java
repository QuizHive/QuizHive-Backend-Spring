package edu.sharif.web.quizhive.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Map<String, String>> handleNotFoundException(NotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(Map.of("error", ex.getMessage()));
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<Map<String, String>> handleConflictException(ConflictException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(Map.of("error", ex.getMessage()));
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Map<String, String>> handleBadRequestException(BadRequestException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(Map.of("error", ex.getMessage()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(Map.of("error", ex.getMessage()));
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<Map<String, String>> handleUnauthorizedException(UnauthorizedException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("error", ex.getMessage()));
	}

	@ExceptionHandler(HttpMessageConversionException.class)
	public ResponseEntity<Map<String, String>> handleHttpMessageConversionException(HttpMessageConversionException ex) {
		logger.error("JSON conversion error", ex);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(Map.of("error", "Invalid JSON format"));
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Map<String, String>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
		logger.error("HTTP method not supported", ex);
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
				.body(Map.of("error", "HTTP method not supported"));
	}

	@ExceptionHandler(SecurityException.class)
	public ResponseEntity<Map<String, String>> handleSecurityException(SecurityException ex) {
		logger.error("Security exception", ex);
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(Map.of("error", "Access denied"));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
		logger.error("Access denied", ex);
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(Map.of("error", "Access denied"));
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<Map<String, String>> handleNoResourceFoundException(NoResourceFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(Map.of("error", ex.getMessage()));
	}

	// Handle generic exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
		logger.error("An unexpected error occurred", ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(Map.of("error", "An unexpected error occurred"));
	}

	private String getStackTraceAsString(Exception ex) {
		StringBuilder stackTrace = new StringBuilder();
		for (StackTraceElement element : ex.getStackTrace()) {
			stackTrace.append(element.toString()).append("\n");
		}
		return stackTrace.toString();
	}
}
