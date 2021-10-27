package com.seculayer.mrms.request;

public class RequestException extends Exception {

	private static final long serialVersionUID = 1L;

	public RequestException() {
		super();
	}

	public RequestException(String errorCode) {
		super(errorCode);
	}

	public RequestException(String errorCode, Exception e) {
		super(errorCode, e);
	}

	public RequestException(String errorCode, String additionalExceptionMessage) {
		super(errorCode + " " + additionalExceptionMessage);
	}

	public RequestException(String s, Throwable t) {
		super(s, t);
	}

	public RequestException(Throwable t) {
		super(t);
	}
}
