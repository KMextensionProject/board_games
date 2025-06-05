package sk.mkrajcovic.bgs.web.validation;

import static java.util.stream.Collectors.joining;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import sk.mkrajcovic.bgs.ClientException;

public class ValidationUtils {

	private ValidationUtils() {
		throw new UnsupportedOperationException("ValidationUtils class was not designed to be instantiated");
	}

	public static void processFieldBindingErrors(List<FieldError> fieldErrors) {
		if (fieldErrors == null || fieldErrors.isEmpty()) {
			return;
		}
		throw new ClientException(HttpStatus.BAD_REQUEST, buildErrorMessage(fieldErrors));
	}

	private static String buildErrorMessage(List<FieldError> fieldErrors) {
		return fieldErrors.stream()
			.map(error -> error.getField() + ": " + error.getDefaultMessage())
			.collect(joining(", "));
	}
}
