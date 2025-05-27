package sk.mkrajcovic.bgs.utils;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A utility class for generating file names.
 */
public class FileNameGenerator {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

	private FileNameGenerator() {
		throw new UnsupportedOperationException("This class was not designed to be instantiated");
	}

	/**
	 * Generates a file name with a time-stamp suffix using yyyyMMdd_HHmmss
	 * pattern.<br>
	 * Example value:
	 * <p>
	 * {@code MyFile_20240814_065523.xlsx}
	 *
	 * @param baseName  the base name of the file, must not be {@code null}
	 * @param extension the file extension, including the dot (e.g., ".xlsx"), must
	 *                  start with '.'
	 * @return the generated file name with the time-stamp suffix
	 * @throws NullPointerException     if baseName or extension is {@code null}
	 * @throws IllegalArgumentException if extension does not start with '.' or is
	 *                                  just a dot
	 */
	public static String generateFileNameWithTimestamp(String baseName, String extension) {
		requireNonNull(baseName, "base file name cannot be null");
		requireNonNull(extension, "at least empty extension is expected");
		checkFileExtensionValidity(extension);

		String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
		return baseName + "_" + timestamp + extension.trim();
	}

	private static void checkFileExtensionValidity(String extension) {
		if (extension.isBlank())
			return;

		if (!extension.startsWith(".")) {
			throw new IllegalArgumentException("Extension must start with a '.'");
		} else if (extension.length() == 1) {
			throw new IllegalArgumentException("Extension must be at least one character long, excluding the dot");
		}
	}

}
