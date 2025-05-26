package sk.mkrajcovic.bgs.controllers;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus.Series;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import sk.mkrajcovic.bgs.ClientException;
import sk.mkrajcovic.bgs.GlobalException;
import sk.mkrajcovic.bgs.InfrastructureException;
import sk.mkrajcovic.bgs.config.BgsMessageSource;
import sk.mkrajcovic.bgs.dto.ExceptionDtoOut;
import sk.mkrajcovic.bgs.web.MessageCodeConstants;

/**
 * Every exception thrown from a Controller and layers beneath will be processed
 * and serialized by this class.
 * <p>
 * For exceptions of type {@link ClientException} we return HTTP 4xx response
 * code, and for all other exceptions we return HTTP 500.
 * <p>
 * If one of {@link GlobalException} is encountered during serialization, the
 * {@link NaskMessageSource} is used to translate its code into a user friendly
 * error message.
 * <p>
 *
 * @author mkrajcovicux
 */
@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerController.class);

	@Value("${bgs.exception.display-stack-trace:false}")
	private boolean displayStackTrace;

	private final BgsMessageSource messageSource;

	public ExceptionHandlerController(BgsMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * Handles the {@link AccessDeniedException} raised from the method level
	 * security due to unauthorized access.
	 *
	 * @return serialized exception with HTTP 403
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ExceptionDtoOut> handleAccessDeniedException(AccessDeniedException ex) {
		LOG.error(MessageCodeConstants.ERROR, ex);
		var exceptionDto = new ExceptionDtoOut();
		exceptionDto.setMessage("Access denied");
		exceptionDto.setType(resolveErrorType(Series.CLIENT_ERROR));
		exceptionDto.setStackTrace(displayStackTrace ? readStackTrace(ex) : null);
		return new ResponseEntity<>(exceptionDto, HttpStatus.FORBIDDEN);
	}

	/**
	 * Catches and serializes Exception objects the following way:
	 * <p>
	 * In case of the {@link GlobalException}, HTTP response inherits its
	 * {@link GlobalException#getHttpStatus()} and if present, the
	 * {@link GlobalException#getCode()} is translated to user friendly message via
	 * {@link NaskMessageSource}.
	 * <p>
	 * In case of every other exception, HTTP response status code is set to 500 and
	 * message gets translated from {@link MessageCodeConstants#ERROR} code.<br>
	 * Note: this is also default behavior for {@link InfrastructureException}
	 *
	 * @return serialized exception
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionDtoOut> handleException(Exception ex) {
		LOG.error(MessageCodeConstants.ERROR, ex);
		return createResponseEntity(ex)
			.body(serializeException(ex));
	}

	private BodyBuilder createResponseEntity(Exception exception) {
		return switch (exception) {
			case GlobalException apiEx -> ResponseEntity.status(apiEx.getHttpStatus().value());
			default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
		};
	}

	private ExceptionDtoOut serializeException(Throwable exception) {
		var exceptionDto = new ExceptionDtoOut();
		exceptionDto.setStackTrace(displayStackTrace ? readStackTrace(exception) : null);

		if (exception instanceof GlobalException global) {
			exceptionDto.setMessage(messageSource.getMessage(global.getCode(), global.getArgs()));
			exceptionDto.setType(resolveErrorType(global.getHttpStatus().series()));
			if (Objects.equals(exceptionDto.getMessage(), global.getCode())) {
				exceptionDto.setCode("custom message");
			} else {
				exceptionDto.setCode(global.getCode());
			}
			List<String> hints = global.getHints();
			if (!hints.isEmpty()) { 
				exceptionDto.setHints(hints);
			}
		} else {
			exceptionDto.setMessage(messageSource.getMessage(MessageCodeConstants.ERROR));
			exceptionDto.setType(resolveErrorType(Series.SERVER_ERROR));
		}
		return exceptionDto;
	}

	private String resolveErrorType(HttpStatus.Series series) {
		return series.name().replace("_", " ").toLowerCase();
	}

	private String readStackTrace(Throwable throwable) {
		StringBuilder result = new StringBuilder();
		StackTraceElement[] stackTrace = throwable.getStackTrace();

		result.append(this).append("\n");
		for (int x = 0; x < stackTrace.length; x++) {
			result.append("\tat ").append(stackTrace[x].toString());
			if (x != stackTrace.length - 1) {
				result.append("\n");
			}
		}
		return result.toString();
	}
}
