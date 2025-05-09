package sk.mkrajcovic.bgs;

import java.util.List;

import org.springframework.http.HttpStatus;

/**
 * Exception representing the client error.<br>
 * Intended for use as validation errors or any error tied to user input.
 */
public class ClientException extends GlobalException {

	private static final long serialVersionUID = -2229150692866750400L;

	public ClientException(HttpStatus httpStatus, String code) {
		super(httpStatus, code, null, null);
	}

	public ClientException(HttpStatus httpStatus, String code, Object... args) {
		super(httpStatus, code, null, null, args);
	}

	public ClientException(HttpStatus httpStatus, String code, Throwable cause) {
		super(httpStatus, code, cause, null);
	}

	public ClientException(HttpStatus httpStatus, String code, List<String> hints) {
		super(httpStatus, code, null, hints);
	}

	public ClientException(HttpStatus httpStatus, String code, Throwable cause, Object... args) {
		super(httpStatus, code, cause, null, args);
	}

	public ClientException(HttpStatus httpStatus, String code, Throwable cause, List<String> hints, Object... args) {
		super(httpStatus, code, cause, hints, args);
	}
}
