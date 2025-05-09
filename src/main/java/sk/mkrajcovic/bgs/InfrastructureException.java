package sk.mkrajcovic.bgs;

import java.util.List;

import org.springframework.http.HttpStatus;

/**
 * This class represents a general application error saying that something
 * unexpected happened, and it should be used to wrap all checked
 * exceptions.<br>
 * By default the HTTP status for all exceptions of this type is set to
 * {@link HttpStatus#INTERNAL_SERVER_ERROR}
 */
public class InfrastructureException extends GlobalException {

	private static final long serialVersionUID = 1L;

	public InfrastructureException(String code) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, code, null, null);
	}

	public InfrastructureException(String code, Object... args) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, code, null, null, args);
	}

	public InfrastructureException(HttpStatus httpStatus, String code, Throwable cause) {
		super(httpStatus, code, cause, null);
	}

	public InfrastructureException(String code, Throwable cause) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, code, cause, null);
	}

	public InfrastructureException(String code, Throwable cause, Object... args) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, code, cause, null, args);
	}

	public InfrastructureException(String code, Throwable cause, List<String> hints, Object... args) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, code, cause, hints, args);
	}

}
