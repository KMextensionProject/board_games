package sk.mkrajcovic.bgs.utils;

public class BooleanUtils extends org.apache.commons.lang3.BooleanUtils {

	public static final String ANO = "áno";
	public static final String NIE = "nie";

	private BooleanUtils() {
		throw new UnsupportedOperationException("This class was not designed to be instantiated");
	}

	public static String toStringAnoNie(final Boolean bool) {
		return toString(bool, ANO, NIE, null);
	}

	public static String toStringAnoNie(final Boolean bool, final String nullString) {
		return toString(bool, ANO, NIE, nullString);
	}
}
