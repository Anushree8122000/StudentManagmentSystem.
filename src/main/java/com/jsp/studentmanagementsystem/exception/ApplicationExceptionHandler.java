package com.jsp.studentmanagementsystem.exception;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jsp.studentmanagementsystem.util.ErrorStructure;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler{

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		// TODO Auto-generated method stub
		List<ObjectError> allErrors =ex.getAllErrors();
		Map<String, String> errors = new HashMap<>();
		for(ObjectError error :allErrors) { 
			//Downcasting
			FieldError fieldError =(FieldError) error;
			String message = fieldError.getDefaultMessage();
			String field = fieldError.getField();
			errors.put(field, message);
		}
		return new ResponseEntity<Object>(errors,HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure> studentNotFoundById(StudentNotFoundById ex){
		ErrorStructure structure = new ErrorStructure();
		structure.setStatus(HttpStatus.NOT_FOUND.value());
		structure.setMessage(ex.getMessage());
		structure.setRootCause("Student is not present with requested id !!");
		//return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
		return new ResponseEntity<ErrorStructure>(structure, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure> studentNotFoundByEmail(studentNotFoundByEmail ex){
		ErrorStructure structure = new ErrorStructure();
		structure.setStatus(HttpStatus.NOT_FOUND.value());
		structure.setMessage(ex.getMessage());
		structure.setRootCause("Student is not present with Email Id !!");
		//return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
		return new ResponseEntity<ErrorStructure>(structure, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorStructure> StudentNotFoundByPhNoException(StudentNotFoundByPhNoException ex){
		ErrorStructure structure = new ErrorStructure();
		structure.setStatus(HttpStatus.NOT_FOUND.value());
		structure.setMessage(ex.getMessage());
		structure.setRootCause("Student is not found Phone Number !!");
		//return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
		return new ResponseEntity<ErrorStructure>(structure, HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure> StudentEmailsNotFoundByGradeException(StudentEmailsNotFoundByGradeException ex){
		ErrorStructure structure = new ErrorStructure();
		structure.setStatus(HttpStatus.NOT_FOUND.value());
		structure.setMessage(ex.getMessage());
		structure.setRootCause("Student Grade Not Found with email !!");
		//return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
		return new ResponseEntity<ErrorStructure>(structure, HttpStatus.NOT_FOUND);
	}

}
