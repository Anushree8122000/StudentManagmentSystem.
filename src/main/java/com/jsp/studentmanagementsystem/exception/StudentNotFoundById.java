package com.jsp.studentmanagementsystem.exception;

public class StudentNotFoundById extends RuntimeException {

	private String message;
	//because we are already 
	public StudentNotFoundById(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
