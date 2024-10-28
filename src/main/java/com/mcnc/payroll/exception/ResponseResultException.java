package com.mcnc.payroll.exception;

public class ResponseResultException extends RuntimeException {

	public ResponseResultException(String message) {
		super(message);
	}

	public ResponseResultException(String message, Exception e) {
		super(message, e);
	}
}
