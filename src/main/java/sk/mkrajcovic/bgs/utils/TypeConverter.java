package sk.mkrajcovic.bgs.utils;

import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import sk.mkrajcovic.bgs.InfrastructureException;
/**
 * Class provides static methods for converting(or unboxing) desired type from
 * Object. All classes shoud at first try to use this conversions for better
 * type compatibility.
 *
 * All methods accepts Object which is wanted to be converted. Of object is null
 * then null value is returned. If object cannot be convervetd to desired type,
 * IllegalArgumentException is thrown.
 */
public class TypeConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(TypeConverter.class);
	private static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	// Format of dates saved as strings in DB
	public static final String DATABASE_STRING_DATE_FORMAT = "dd.MM.yyyy";
	// This is the format returned by TO_CHAR(ap.t_sys_from)
	public static final String DATABASE_STRING_DATE_FORMAT_WITH_TIME = "dd.MM.yy HH:mm:ss,SSS";

	private static final String TRUE = "true";
	private static final String FALSE = "false";

	public static final String STRING_TRUE = "Y";
	public static final String STRING_FALSE = "N";

	private static final Pattern TIMEZONE_PATTERN = Pattern.compile("(Z|[+-][01]\\d((:)?[0-5]\\d)?)$");
	private static final Pattern FULL_TZ_PATTERN = Pattern.compile("^[+-][01]\\d:[0-5]\\d$");
	private static final Pattern FULL_TZ_WITHOUT_COLON_PATTERN = Pattern.compile("^[+-][01]\\d[0-5]\\d$");
	private static final Pattern PARCIAL_TZ_PATTERN = Pattern.compile("^[+-][01]\\d$");

	private static final String CONVERT_TYPE_MSG = "Cannot convert type %s to %s.";

	public TypeConverter() {
		super();
	}

	public String toString(Object obj) {
		if (obj == null || obj instanceof String) {
			return (String) obj;
		}

		// because if big number is considered as string .toString returns it in
		// exponencial format
		if (obj instanceof Double doubleValue) {
			return BigDecimal.valueOf(doubleValue).toPlainString();
		}
		if (obj instanceof BigDecimal bigDecimal) {
			return bigDecimal.toPlainString();
		}
		if (obj instanceof LocalDateTime localDateTime) {
			return LOCAL_DATE_TIME_FORMATTER.format(localDateTime);
		} else if (obj instanceof LocalDate localDate) {
			return LOCAL_DATE_FORMATTER.format(localDate);
		} else if (obj instanceof Clob clob) {
			try {
				return clob.getSubString(1, (int) clob.length()).trim();
			} catch (SQLException e) {
				throw new IllegalArgumentException("Cannot convert object (Clob) to String!", e);
			}
		} else {
			LOGGER.debug("Converting {} to String via toString() method.", obj.getClass());
			return obj.toString().trim();
		}
	}

	public BigDecimal toBigDecimal(Object obj) {
		if (obj == null || obj instanceof BigDecimal) {
			return (BigDecimal) obj;
		}

		BigDecimal retVal = null;
		if (obj instanceof Boolean bool) {

			return new BigDecimal(Boolean.TRUE.equals(bool) ? 1 : 0);
		} else if (obj instanceof Long longValue) {

			return new BigDecimal(longValue);
		} else if (obj instanceof Integer integerValue) {

			return new BigDecimal(integerValue);
		}

		try {
			String decInString = obj.toString();
			if (isBooleanString(decInString)) {
				if (Boolean.parseBoolean(decInString)) {
					return new BigDecimal(1);
				}
				return new BigDecimal(0);
			}

			LOGGER.debug("Converting {} to BigDecimal via toString() method.", obj.getClass());
			retVal = new BigDecimal(decInString);

		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(CONVERT_TYPE_MSG.formatted(obj, "BigDecimal"), e);
		}

		return retVal;
	}

	public Double toDouble(Object obj) {

		if (obj == null || obj instanceof Double) {
			return (Double) obj;
		}

		if (obj instanceof Number number) {
			return Double.valueOf(number.doubleValue());
		} else {
			LOGGER.debug("Converting {} to Double via toString() method.", obj.getClass());
			String doubleInString = obj.toString();
			Double retVal = null;

			try {
				retVal = Double.valueOf(doubleInString);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Cannot convert object to Double!", e);
			}

			return retVal;
		}
	}

	public Long toLong(Object obj) {
		if (obj == null || obj instanceof Long) {
			return (Long) obj;
		}

		if (obj instanceof Integer integerValue) {

			return integerValue.longValue();
		} else if (obj instanceof Boolean bool) {

			return Long.valueOf(Boolean.TRUE.equals(bool) ? 1L : 0L);
		} else if (obj instanceof BigDecimal bigDecimal) {

			return bigDecimal.longValueExact(); // throws ArithmeticException

		} else {

			String intInString = obj.toString();
			if (isBooleanString(intInString)) {
				if (Boolean.parseBoolean(intInString)) {
					return Long.valueOf(1L);
				}
				return Long.valueOf(0L);
			}
			LOGGER.debug("Converting {} to Long via toString() method.", obj.getClass());
			Long retVal = null;

			try {
				BigDecimal bigDec = new BigDecimal(intInString);
				retVal = bigDec.longValueExact();// throws ArithmeticException
			} catch (NumberFormatException | ArithmeticException e) {
				throw new IllegalArgumentException("Cannot convert object to Long!", e);
			}

			return retVal;
		}
	}

	public Integer toInteger(Object obj) {
		if (obj == null || obj instanceof Integer) {
			return (Integer) obj;
		}

		if (obj instanceof Boolean bool) {

			return Integer.valueOf(Boolean.TRUE.equals(bool) ? 1 : 0);
		} else if (obj instanceof BigDecimal bigDecimal) {

			return bigDecimal.intValueExact();// throws ArithmeticException
		}

		String intInString = obj.toString();
		if (isBooleanString(intInString)) {
			if (Boolean.parseBoolean(intInString)) {
				return Integer.valueOf(1);
			}
			return Integer.valueOf(0);
		}
		LOGGER.debug("Converting {} to Integer via toString() method.", obj.getClass());
		Integer retVal = null;

		try {
			BigDecimal bigDec = new BigDecimal(intInString);
			retVal = bigDec.intValueExact();// throws ArithmeticException
		} catch (NumberFormatException | ArithmeticException e) {
			throw new IllegalArgumentException("Cannot convert object to Integer!", e);
		}

		return retVal;
	}

	public boolean isValidDate(Object obj) {
		if (obj instanceof Long) {
			return true;
		}
		return obj instanceof String string && dateTZ.matcher(string).matches()
				|| date.matcher((String) obj).matches()
				|| longPattern.matcher((String) obj).matches();
	}

	public String calendarToStringWithoutTime(LocalDateTime datetime) {
		if (datetime == null) {
			return null;
		}
		return LOCAL_DATE_FORMATTER.format(datetime);
	}

	public boolean isValidDateTime(Object obj) {
		if (obj instanceof Long) {
			return true;
		}
		return obj instanceof String string && dateTimeTZ.matcher(string).matches() 
				|| dateTime.matcher((String) obj).matches();
	}

	public boolean isValidTime(Object obj) {
		return obj instanceof String string && time.matcher(string).matches();
	}

	public boolean isValidNumber(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj instanceof Number) {
			return true;
		} else if (obj instanceof String intInString) {
			try {
				new BigDecimal(intInString);
				return true;
			} catch (NumberFormatException | ArithmeticException e) { // NOSONAR These Exceptions are used to determine if input object is a number
				return false;
			}
		} else {
			throw new IllegalArgumentException("Argument " + obj + " can not be validated as number.");
		}
	}

	public static boolean containsDate(String str) {
		return dateTZ.matcher(str).matches()
			|| date.matcher(str).matches()
			|| dateTime.matcher(str).matches()
			|| dateTimeTZ.matcher(str).matches();
	}

	private static String dateRegex = "(\\d{4})-(\\d{2})-(\\d{2})";
	private static Pattern date = Pattern.compile(dateRegex);
	private static Pattern dateTime = Pattern.compile(dateRegex + "T(\\d{2}):(\\d{2}):(\\d{2})(\\.\\d{1,3})?");
	private static Pattern dateTZ = Pattern.compile(dateRegex + "(Z|([\\+-]\\d{2}(?::?\\d{2})?))");
	private static Pattern dateTimeTZ = Pattern.compile(dateRegex + "T(\\d{2}):(\\d{2}):(\\d{2})(\\.\\d{1,3})?(Z|([\\+-]\\d{2}(?::?\\d{2})?))");
	private static Pattern time = Pattern.compile("\\d{2}:\\d{2}(?::\\d{2}(?:\\.\\d{0,9})?)?");
	private static Pattern longPattern = Pattern.compile("\\d+");

	public Clob toClob(Object obj) {
		if (obj == null) {
			return null;
		}

		if (obj instanceof Clob clob) {
			return clob;
		}

		throw new IllegalArgumentException("Cannot convert object to Clob!");
	}

	public Reader toReader(Object obj) {
		if (obj == null) {
			return null;
		}

		if (obj instanceof Clob clob) {
			try {
				return clob.getCharacterStream();
			} catch (SQLException e) {
				throw new IllegalArgumentException("Cannot convert object to Clob!", e);
			}
		}

		throw new IllegalArgumentException("Cannot convert object to Reader!");
	}

	public Blob toBlob(Object obj) {
		if (obj == null) {
			return null;
		}

		if (obj instanceof Blob blob) {
			return blob;
		}

		throw new IllegalArgumentException("Cannot convert object to Blob!");
	}

	/**
	 * Converts the decimal mark from the string number from dot to comma.<br>
	 *
	 * @param number
	 * @return
	 */
	public String convertDecimalMark(String number) {
		return number.replace(".", ",");
	}

	/**
	 * Type - string boolean representation: {@link #STRING_TRUE} =
	 * {@value #STRING_TRUE}, {@link TypeConverter#STRING_FALSE} =
	 * {@value #STRING_FALSE}
	 *
	 * @return if internally {@link #toBoolean} returns <code>true</code> then
	 *         {@link #STRING_TRUE} = {@value #STRING_TRUE} else
	 *         {@link TypeConverter#STRING_FALSE} = {@value #STRING_FALSE}
	 */
	public String toStringBoolean(Object value) {
		boolean convValue = toBoolean(value);
		return convValue ? STRING_TRUE : STRING_FALSE;
	}

	/**
	 * Type - Conversion method String - return Boolean.valueOf(value) Long|Integer
	 * - return value.int|long Value() == 1
	 *
	 * @return
	 */
	public boolean toBoolean(Object value) {
		Assert.notNull(value, "boolean value must not be null here!");
		if (value instanceof Boolean bool) {
			return bool.booleanValue();
		} else if (value instanceof String string) {
			return Boolean.valueOf(string);
		} else if (value instanceof Number number) {
			long data = number.longValue(); // NOSONAR this conversion may truncate original value but boolean operates
														// within range 0 and 1
			// allow only 0 and 1 values all other values are not booleans!
			if (data == 0L) {
				return false;
			} else if (data == 1L) {
				return true;
			}
		}
		throw new IllegalArgumentException(CONVERT_TYPE_MSG.formatted(value.getClass(), "Boolean"));
	}

	private boolean isBooleanString(String value) {
		return TRUE.equalsIgnoreCase(value) || FALSE.equalsIgnoreCase(value);
	}

	public LocalDate toLocalDate(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof LocalDate localDate) {
			return localDate;
		}
		if (obj instanceof String string) {
			return LocalDate.parse(string);
		}
		throw new IllegalArgumentException(CONVERT_TYPE_MSG.formatted(obj.getClass(), "LocalDate"));
	}

	public LocalDateTime toLocalDateTime(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof LocalDateTime localDateTime) {
			return localDateTime;
		}
		if (obj instanceof String string) {
			if (hasTimeZone(string)) {
				return fixedJ8DateTimeParser(string);
			}
			return LocalDateTime.parse(string);
		}
		throw new IllegalArgumentException(CONVERT_TYPE_MSG.formatted(obj.getClass(), "LocalDateTime"));
	}

	private LocalDateTime fixedJ8DateTimeParser(String dateTimeWithTimeZone) {
		if (dateTimeWithTimeZone.toUpperCase().endsWith("Z")) {
			return OffsetDateTime.parse(dateTimeWithTimeZone).atZoneSameInstant(ZoneId.of("GMT")).toLocalDateTime();
		}
		String isoDateTime = convertToISOString(dateTimeWithTimeZone);
		return OffsetDateTime.parse(isoDateTime).atZoneSameInstant(ZoneId.of("GMT")).toLocalDateTime();
	}

	/**
	 * JAVA 8 {@link java.time.OffsetDateTime#parse(CharSequence)} cannot parse 2020-01-01T00:00:00+02, without minutes in timezone
	 * Fixed in JAVA 11
	 * @param dateTimeWithTimeZone
	 * @return
	 */
	private String convertToISOString(String dateTimeWithTimeZone) {
		int tIndex = dateTimeWithTimeZone.indexOf('T');
		String datePart = dateTimeWithTimeZone.substring(0, tIndex);
		String timePart = dateTimeWithTimeZone.substring(tIndex + 1);
		int index = Math.max(timePart.indexOf('+'), timePart.indexOf('-'));
		String timeWithoutTZ = timePart.substring(0, index);
		String isoTz = getISOTz(timePart.substring(index));
		return datePart + 'T' + timeWithoutTZ + isoTz;
	}

	private String getISOTz(String tz) {
		if ("Z".equalsIgnoreCase(tz)) {
			return tz;
		}
		if (FULL_TZ_PATTERN.matcher(tz).matches()) {
			return tz;
		}
		if (PARCIAL_TZ_PATTERN.matcher(tz).matches()) {
			return tz + ":00";
		}
		if (FULL_TZ_WITHOUT_COLON_PATTERN.matcher(tz).matches()) {
			return tz.substring(0, 3) + ":" + tz.substring(3);
		}
		throw new InfrastructureException("invalidTimeZone");
	}

	private boolean hasTimeZone(String obj) {
		if (obj.indexOf('T') < 0) {
			return false;
		}
		return TIMEZONE_PATTERN.matcher(obj.substring(obj.indexOf('T'))).find();
	}

	public Object toObject(Object value, int sqlType) {
		switch (sqlType) {
		case Types.DATE:
			return toLocalDate(value);
		case Types.TIMESTAMP, Types.TIMESTAMP_WITH_TIMEZONE:
			return toLocalDateTime(value);
		default:
			return value;
		}
	}

	public LocalDate toLocalDate(String date, DateTimeFormatter formatter) {
		if (date == null) {
			return null;
		}
		return LocalDate.parse(date, formatter);
	}

	public LocalDateTime zonedBeginOfDay(LocalDate localDate) {
		if (localDate == null) {
			return null;
		}
		ZonedDateTime start = localDate.atStartOfDay(ZoneId.systemDefault());
		start = start.withZoneSameInstant(ZoneId.of("GMT"));
		return start.toLocalDateTime();
	}

	public LocalDate toZonedDate(LocalDateTime dateTime) {
		if (dateTime == null) {
			return null;
		}
		ZonedDateTime zoned = ZonedDateTime.of(dateTime, ZoneId.of("GMT"));
		zoned = zoned.withZoneSameInstant(ZoneId.systemDefault());
		return zoned.toLocalDate();
	}
	/**
	 * @deprecated use only for excel, because generator for excel can't handle LocalDate
	 * @param dateTime
	 * @return
	 */
	@Deprecated(forRemoval = false)
	public Date toZonedDateForExcel(LocalDateTime dateTime) { // NOSONAR used only for Excel value, prevent to use it in services
		if (dateTime == null) {
			return null;
		}
		ZonedDateTime zoned = ZonedDateTime.of(dateTime, ZoneId.of("GMT"));
		zoned = zoned.withZoneSameInstant(ZoneId.systemDefault());
		return Date.from(zoned.toInstant());
	}

	public Long getTime(Object param) {
		if (param instanceof LocalDate localDate) {
			return Timestamp.valueOf(localDate.atTime(0, 0)).getTime();
		} else if (param instanceof LocalDateTime localDateTime) {
			return Timestamp.valueOf(localDateTime).getTime();
		} else if (param instanceof java.sql.Date sqlDate) {
			return sqlDate.getTime();
		} else if (param instanceof Timestamp timestamp) {
			return timestamp.getTime();
		}
		return null;
	}
}
