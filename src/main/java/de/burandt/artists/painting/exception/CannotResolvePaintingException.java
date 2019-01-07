package de.burandt.artists.painting.exception;

public class CannotResolvePaintingException extends Exception {

	private static final long serialVersionUID = 1L;

	public CannotResolvePaintingException() {
		super();
	}

	public CannotResolvePaintingException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CannotResolvePaintingException(String message, Throwable cause) {
		super(message, cause);
	}

	public CannotResolvePaintingException(String message) {
		super(message);
	}

	public CannotResolvePaintingException(Throwable cause) {
		super(cause);
	}

	
}
