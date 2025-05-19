package sk.mkrajcovic.bgs.web.validation;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for the {@link YouTubeUrl} annotation.
 * <p>
 * This class checks if a given URL matches the format of a valid YouTube URL.
 * It uses a regular expression to match YouTube domains like "youtube.com" and
 * "youtu.be".
 */
public class YouTubeUrlValidator implements ConstraintValidator<YouTubeUrl, String> {

	private static final Pattern YOUTUBE_URL_PATTERN = Pattern.compile(
		"^(https?://)?(www\\.)?(youtube|youtu|youtube-nocookie)\\.(com|be)/.*$");

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		return YOUTUBE_URL_PATTERN.matcher(value).matches();
	}
}
