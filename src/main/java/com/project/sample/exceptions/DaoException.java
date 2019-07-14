package com.project.sample.exceptions;

public class DaoException extends Exception {

	private static final long serialVersionUID = 3258540932401172743L;

	public DaoException(String message) {
		super(message);
	}

	public DaoException(String message, Exception e) {
		super(message, e);
	}
	
}
