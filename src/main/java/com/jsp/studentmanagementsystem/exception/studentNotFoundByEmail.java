package com.jsp.studentmanagementsystem.exception;

public class studentNotFoundByEmail extends RuntimeException {

	private String message;

	public studentNotFoundByEmail(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}



}
