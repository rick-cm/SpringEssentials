 package com.example.demo.awesome.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.example.demo.awesome.error.ErrorDetails;
import com.example.demo.awesome.error.ResourceNotFoundDetails;
import com.example.demo.awesome.error.ResourceNotFoundException;
import com.example.demo.awesome.error.ValidationErrorDetails;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rfnException){
		ResourceNotFoundDetails rnfDetails = ResourceNotFoundDetails.Builder
			.newBuilder()
			.timestamp(new Date().getTime())
			.status(HttpStatus.NOT_FOUND.value())
			.title("Resource Not Found")
			.detail(rfnException.getMessage())
			.developerMessage(rfnException.getClass().getName())
			.build();
		return new ResponseEntity<>(rnfDetails, HttpStatus.NOT_FOUND);
	}
	
	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException manvException, 
																							HttpHeaders headers, 
																							HttpStatus status, 
																							WebRequest request) {
		List<FieldError> fieldErrors = manvException.getBindingResult().getFieldErrors();
		String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(","));
		String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
		ValidationErrorDetails rnfDetails = ValidationErrorDetails.Builder
			.newBuilder()
			.timestamp(new Date().getTime())
			.status(HttpStatus.BAD_REQUEST.value())
			.title("Field Validation Error")
			.detail("Validation failed for arguments")
			.developerMessage(manvException.getClass().getName())
			.field(fields)
			.fieldMessage(fieldsMessage)
			.build();
		return new ResponseEntity<>(rnfDetails, HttpStatus.BAD_REQUEST);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
			Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

		ErrorDetails rnfDetails = ErrorDetails.Builder
				.newBuilder()
				.timestamp(new Date().getTime())
				.status(status.value())
				.title("Internal exception") 
				.detail(ex.getMessage())
				.developerMessage(ex.getClass().getName())
				.build();
		return new ResponseEntity<>(rnfDetails, headers, status);
	}
	
	@ExceptionHandler(PropertyReferenceException.class)
	public ResponseEntity<?> handlePropertyReferenceException(PropertyReferenceException prException){
		ResourceNotFoundDetails prDetails = ResourceNotFoundDetails.Builder
			.newBuilder()
			.timestamp(new Date().getTime())
			.status(HttpStatus.NOT_FOUND.value())
			.title("Property not found")
			.detail(prException.getMessage())
			.developerMessage(prException.getClass().getName())
			.build();
		return new ResponseEntity<>(prDetails, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException adException){
		ErrorDetails adDetails = ErrorDetails.Builder
				.newBuilder()
				.timestamp(new Date().getTime())
				.status(HttpStatus.NOT_FOUND.value())
				.title("Access Denied")
				.detail(adException.getMessage())
				.developerMessage(adException.getClass().getName())
				.build();
			return new ResponseEntity<>(adDetails, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException unfException){
		ErrorDetails adDetails = ErrorDetails.Builder
				.newBuilder()
				.timestamp(new Date().getTime())
				.status(HttpStatus.NOT_FOUND.value())
				.title("User not found")
				.detail(unfException.getMessage())
				.developerMessage(unfException.getClass().getName())
				.build();
			return new ResponseEntity<>(adDetails, HttpStatus.NOT_FOUND);
	}
}
