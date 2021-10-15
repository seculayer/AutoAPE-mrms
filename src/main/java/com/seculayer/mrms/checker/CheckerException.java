package com.seculayer.mrms.checker;

public class CheckerException extends Exception {

	private static final long serialVersionUID = 1L;

	public CheckerException() {
		super();
	}

	public CheckerException(String errorCode) {
		super(errorCode);
	}

	public CheckerException(String errorCode, Exception e) {
		super(errorCode, e);
	}

	public CheckerException(String errorCode, String additionalExceptionMessage) {
		super(errorCode + " " + additionalExceptionMessage);
	}

	public CheckerException(String s, Throwable t) {
		super(s, t);
	}

	public CheckerException(Throwable t) {
		super(t);
	}
}
