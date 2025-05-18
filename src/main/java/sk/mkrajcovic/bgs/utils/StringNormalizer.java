package sk.mkrajcovic.bgs.utils;

import org.apache.commons.lang3.StringUtils;

public class StringNormalizer {

	private StringNormalizer() {
		throw new IllegalStateException("This class was not designed to be instantiated");
	}

    /**
     * Normalizes the given string by removing accents and diacritical marks,
     * trimming leading and trailing whitespace, and converting to lowercase.
     *
     * @param string the search query parameter to be normalized
     * @return the normalized string
     */
    public static String normalize(String string) {
        if (string == null) {
            return null;
        }
        return StringUtils.stripAccents(string.trim().toLowerCase());
    }
}
