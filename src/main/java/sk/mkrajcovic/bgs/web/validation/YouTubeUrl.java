package sk.mkrajcovic.bgs.web.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Custom annotation for validating YouTube URLs.
 * <p>
 * This annotation ensures that the URL provided points to a valid YouTube video
 * or channel. It uses a regular expression to match valid YouTube URLs,
 * including both `youtube.com` and `youtu.be` domains.
 * <p>
 * It can be applied to a String field to validate that the URL is from a
 * trusted domain (YouTube).
 *
 * @see YouTubeUrlValidator
 */
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = YouTubeUrlValidator.class)
public @interface YouTubeUrl {

	String message() default "Invalid YouTube URL";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
