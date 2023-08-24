package com.jsp.studentmanagementsystem.exception;

public class StudentEmailsNotFoundByGradeException extends RuntimeException {
	private String message;

	public StudentEmailsNotFoundByGradeException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}


}
