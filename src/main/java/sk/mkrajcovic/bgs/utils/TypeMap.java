package sk.mkrajcovic.bgs.utils;

import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.util.Assert;

import sk.mkrajcovic.bgs.InfrastructureException;

/**
 * The {@code TypeMap} class is a serializable map implementation that provides
 * utility methods for handling various data conversions and operations on
 * key-value pairs.<br>
 * It is commonly used in occasions where the type conversions for different
 * data formats such as {@code BigDecimal}, {@code LocalDate}, {@code Calendar},
 * and more are needed.
 *
 * <p>
 * In addition to standard {@link Map} operations, it includes methods for
 * managing {@code null} values, renaming keys, creating submaps, and recursive
 * cleanup of empty objects.<br>
 * It supports integration with a {@link TypeConverter} for complex type
 * transformations.
 *
 * This class is typically used in data processing contexts where flexible data
 * handling and transformation is required.
 */
public class TypeMap extends LinkedHashMap<String, Object> implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final List<?> TRUE_VALUES = Arrays.asList(1, 1l, BigDecimal.ONE, "Y", "YES", "y", "true", "TRUE", "T", "t", "A", "1", true);
	private static final List<?> FALSE_VALUES = Arrays.asList(0, 0l, -1l, -1, BigDecimal.ZERO, new BigDecimal(-1), "N", "NO", "n", "false", "FALSE", "F", "f", "0",
			false);

	private static final String TYPE_CONVERSION_MESSAGE_CODE = "infrastructureTypeConversionException";
	private static final TypeConverter CONVERTER = new TypeConverter();

	private static final String NOT_PRESENT_MSG = "%s not present!";
	private static final String NOT_RECOGNIZABLE_MSG = "Field \"%s\" is not recognizable as %s object! Value=%s";

	public TypeMap() {
	}

	/**
	 * Guava-style initializable constructor.
	 *
	 * @param data
	 *            must contain even number of values, which will be put into a newly created map as key-value pairs.
	 *
	 * @throws IllegalArgumentException
	 *             if the numer of input data is not even
	 * @throws IllegalArgumentException
	 *             if the type of the key-positionized data is not java.lang.String
	 */
	public TypeMap(Object... data) {
		if (data != null && data.length == 1 && data[0] instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> dataMap = (Map<String, Object>) data[0];
			Assert.notNull(dataMap, "map canot be null");
			putAll(dataMap);
			return;
		}

		this.putInternal(data);
	}

	public TypeMap(Map<String, Object> map) {
		Assert.notNull(map, "map canot be null");
		putAll(map);
	}

	public static TypeMap emptyTypeMap() {
		return new TypeMap();
	}

	public static boolean isNullOrEmpty(TypeMap map) {
		return Objects.isNull(map) || map.isEmpty();
	}

	@Override
	public String toString() {
		return toString(0);
	}

	private static final String PAD_TAB = "\t";

	public String toString(int level) {
		StringBuilder sbPad = new StringBuilder();
		for (int j = 0; j < level; j++) {
			sbPad.append(PAD_TAB);
		}

		StringBuilder sb = new StringBuilder();
		Iterator<Entry<String, Object>> iter = entrySet().iterator();
		if (!iter.hasNext()) {
			return sb.append(sbPad).append("{}").toString();
		}

		sb.append("{\n");
		for (;;) {
			Entry<String, Object> e = iter.next();
			String key = e.getKey();
			Object value = e.getValue();
			sb.append(sbPad)
				.append(PAD_TAB)
				.append(key)
				.append('=');

			String userValue;
			String type;
			if (value == null) {
				userValue = "null";
				type = "?";
			} else if (value == this) {
				userValue = "(this Map)";
				type = value.getClass().getName();
			} else if (value instanceof TypeMap typeMap) {
				userValue = typeMap.toString(level+1);
				type = value.getClass().getName();
			} else {
				userValue = CONVERTER.toString(value);
				type = value.getClass().getName();
			}

			sb.append(userValue)
				.append(PAD_TAB)
				.append('(')
				.append(type)
				.append(')');

			if (!iter.hasNext()){
				return sb.append('\n')
						.append(sbPad)
						.append("}")
						.toString();
			}
			sb.append(",\n");
		}
	}

	@SuppressWarnings("unchecked")
	public TypeMap removeMap(String key) {
		Object obj = this.remove(key);
		if (!(obj instanceof Map map)) { // NOSONAR #S3740 cannot specify type parameters here
			return emptyTypeMap();
		}
		return new TypeMap(map);
	}

	public TypeMap renameKey(String oldName, String newName) {
		if (containsKey(oldName)) {
			put(newName, remove(oldName));
		}
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + super.hashCode();
		return result;
	}

	public LocalDateTime getDateTime(String key) {
		try {
			return getAsLocalDateTime(key);
		} catch (IllegalArgumentException e) {
			throw new InfrastructureException(TYPE_CONVERSION_MESSAGE_CODE, e, null, key, get(key));
		}
	}

	private LocalDateTime getAsLocalDateTime(String key) {
		Object obj = super.get(key);
		try {
			return CONVERTER.toLocalDateTime(obj);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(NOT_RECOGNIZABLE_MSG.formatted(key, "LocalDateTime", obj), e);
		}
	}

	public LocalDate getLocalDate(String key) {
		try {
			return getAsLocalDate(key);
		} catch (IllegalArgumentException e) {
			throw new InfrastructureException(TYPE_CONVERSION_MESSAGE_CODE, e, null, key, get(key));
		}
	}

	private LocalDate getAsLocalDate(String key) {
		Object obj = super.get(key);
		try {
			return CONVERTER.toLocalDate(obj);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(NOT_RECOGNIZABLE_MSG.formatted(key, "LocalDate", obj), e);
		}
	}

	public Long getLong(String key) {
		try {
			return getAsLong(key);
		} catch (IllegalArgumentException e) {
			throw new InfrastructureException(TYPE_CONVERSION_MESSAGE_CODE, e, null, key, get(key));
		}
	}

	private Long getAsLong(String key) {
		Object obj = super.get(key);
		try {
			return CONVERTER.toLong(obj);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(NOT_RECOGNIZABLE_MSG.formatted(key, "Long", obj), e);
		}
	}

	public Double getDouble(String key) {
		try {
			return getAsDouble(key);
		} catch (IllegalArgumentException e) {
			throw new InfrastructureException(TYPE_CONVERSION_MESSAGE_CODE, e, null, key, get(key));
		}
	}

	private Double getAsDouble(String key) {
		Object obj = super.get(key);
		try {
			return CONVERTER.toDouble(obj);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(NOT_RECOGNIZABLE_MSG.formatted(key, "Double", obj), e);
		}
	}

	public String getString(String key) {
		try {
			Object obj = super.get(key);
			return CONVERTER.toString(obj);
		} catch (IllegalArgumentException e) {
			throw new InfrastructureException(TYPE_CONVERSION_MESSAGE_CODE, e, null, key, get(key));
		}
	}

	/**
	 * Convinient method for handling non-existing key in map by returning defaultValue.
	 *
	 * @param string
	 * @param defaultValue
	 * @return
	 */
	public BigDecimal getBigDecimalDefault(String propertyName, BigDecimal defaultValue) {
		BigDecimal value = getBigDecimal(propertyName);
		if (value != null) {
			return value;
		}
		return defaultValue;
	}

	public BigDecimal getBigDecimal(String key) {
		try {
			Object obj = super.get(key);
			return CONVERTER.toBigDecimal(obj);
		} catch (IllegalArgumentException e) {
			throw new InfrastructureException(TYPE_CONVERSION_MESSAGE_CODE, e, null, key, get(key));
		}
	}

	/**
	 * Convinient method for handling non-existing key in map by throwing ClientException.
	 *
	 * @param string
	 * @return
	 */
	public BigDecimal getBigDecimalSafe(String propertyName) {
		BigDecimal value = getBigDecimal(propertyName);
		if (value != null) {
			return value;
		}
		throw new InfrastructureException(NOT_PRESENT_MSG.formatted(propertyName));
	}

	public Boolean getBoolean(String key) {
		try {
			Object obj = get(key);
			if (obj == null) {
				return null; //NOSONAR Discussed, there is good reason for returning null
			}
			return CONVERTER.toBoolean(obj);
		} catch (IllegalArgumentException e) {
			throw new InfrastructureException(TYPE_CONVERSION_MESSAGE_CODE, e, null, key, get(key));
		}
	}

	public Integer getInteger(String key) {
		try {
			Object obj = get(key);
			return CONVERTER.toInteger(obj);
		} catch (IllegalArgumentException e) {
			throw new InfrastructureException(TYPE_CONVERSION_MESSAGE_CODE, e, null, key, get(key));
		}
	}

	public Clob getClob(String key) {
		try {
			Object obj = get(key);
			return CONVERTER.toClob(obj);
		} catch (IllegalArgumentException e) {
			throw new InfrastructureException(TYPE_CONVERSION_MESSAGE_CODE, e, null, key, get(key));
		}
	}

	public Reader getReader(String key) {
		try {
			Object obj = get(key);
			return CONVERTER.toReader(obj);
		} catch (IllegalArgumentException e) {
			throw new InfrastructureException(TYPE_CONVERSION_MESSAGE_CODE, e, null, key, get(key));
		}
	}

	public Blob getBlob(String key) {
		try {
			Object obj = get(key);
			return CONVERTER.toBlob(obj);
		} catch (IllegalArgumentException e) {
			throw new InfrastructureException(TYPE_CONVERSION_MESSAGE_CODE, e, null, key, get(key));
		}
	}

	/**
	 * creates sub map containing only collumns defineed by input; obiects that are contained in submap are the same as in original map the method does submap
	 * only on first level of objects
	 *
	 * @param collumns
	 * @return
	 */
	public TypeMap subMap(String... collumns) {
		TypeMap out = new TypeMap(CONVERTER);
		for (String collumn : collumns) {
			if (containsKey(collumn)) {
				out.put(collumn, get(collumn));
			}
		}
		return out;
	}

	public TypeMap cloneTypeMap() {
		return SerializationUtils.clone(this);
	}

	/**
	 * Changes mappings based on pairs given as the argument. Calling map.remap("a", "b") will remove object mapped to "a" and map it to "b". Amount of pairs as
	 * arguments to be remapped is eligible.
	 *
	 * @param mapping
	 * @return
	 */
	public TypeMap remap(String... mappings) {
		for (int i = 0; i < mappings.length; i += 2) {
			this.renameKey(mappings[i], mappings[i + 1]);
		}
		return this;
	}

	/**
	 * remove all Entrys from input map with null value if you want to remove all Entryies with null and enpty maps and lists use it with
	 * {@link #removeEmptyObjectsRecursive(TypeMap)}
	 */
	public void removeNullValues() {
		removeNullValues(this);
	}

	private void removeNullValues(TypeMap input) {
		List<String> toRemove = new ArrayList<>();
		for (Entry<String, Object> ent : input.entrySet()) {
			Object value = ent.getValue();
			if (value == null) {
				toRemove.add(ent.getKey());
			} else if (Map.class.isAssignableFrom(value.getClass())) {
				removeNullValues((TypeMap) value);
			} else if (List.class.isAssignableFrom(value.getClass())) {
				List<TypeMap> list = input.<TypeMap> getList(ent.getKey());
				for (TypeMap object : list) {
					removeNullValues(object);
				}
			}
		}
		for (String remove : toRemove) {
			input.remove(remove);
		}
	}

	/**
	 * Removes all null/empty objects using recursion.
	 */
	public void removeEmptyObjectsRecursive() {
		removeEmptyObjectsRecursive(this);
	}

	/**
	 * Removes all null/empty objects from the input map using recursion.
	 *
	 * @param input
	 */
	public void removeEmptyObjectsRecursive(TypeMap input) {
		Objects.requireNonNull(input, "input cannot be null !");

		List<String> toRemove = new ArrayList<>();
		for (Entry<String, Object> ent : input.entrySet()) {
			Object value = ent.getValue();
			if (value == null) {
				return;
			} else if (Map.class.isAssignableFrom(value.getClass())) {
				processMap(ent, toRemove);
			} else if (List.class.isAssignableFrom(value.getClass())) {
				processList(ent, toRemove);
			}
		}
		input.removeAll(toRemove);
	}

	private void processMap(Map.Entry<String, Object> entry, List<String> toRemove) {
		TypeMap map = (TypeMap) entry.getValue();
		removeEmptyObjectsRecursive(map);
		if (map.isEmpty()) {
			toRemove.add(entry.getKey());
		}
	}

	private void processList(Map.Entry<String, Object> entry, List<String> toRemove) {
		@SuppressWarnings("unchecked")
		List<TypeMap> list = (List<TypeMap>) entry.getValue();
		ListIterator<TypeMap> li = list.listIterator(list.size());
		while (li.hasPrevious()) {
			TypeMap map = li.previous();
			removeEmptyObjectsRecursive(map);
			if (map.isEmpty()) {
				li.remove();
			}
		}
		if (list.isEmpty()) {
			toRemove.add(entry.getKey());
		}
		list.removeAll(Collections.singleton(null));
	}

	private void removeAll(List<String> keys) {
		keys.forEach(this::remove);
	}

	/**
	 * Convinient method for handling non-existing key in map by returning defaultValue.
	 *
	 * @param string
	 * @param Long
	 * @return Long value of key, or default
	 */
	public Long getLongDefault(String propertyName, Long defaultvalue) {
		Long value = getLong(propertyName);
		return value != null ? value : defaultvalue;
	}

	/**
	 * Convinient method for handling non-existing key in map by throwing InfrastructureException.
	 *
	 * @param string
	 * @return
	 */
	public Long getLongSafe(String propertyName) {
		Long value = getLong(propertyName);
		if (value != null) {
			return value;
		}
		throw new InfrastructureException(NOT_PRESENT_MSG.formatted(propertyName));
	}

	/**
	 * Convinient method for handling non-existing key in map by returning defaultValue.
	 *
	 * @param string
	 * @param Double
	 * @return Double value of key, or default
	 */
	public Double getDoubleDefault(String propertyName, Double defaultvalue) {
		Double value = getDouble(propertyName);
		return value != null ? value : defaultvalue;
	}

	/**
	 *
	 * Convinient method for handling non-existing key in map by returning defaultValue.
	 *
	 * @param string
	 * @param maximumDate
	 * @return
	 */
	public LocalDateTime getDateTimeDefault(String propertyName, LocalDateTime defaultValue) {
		LocalDateTime value = getDateTime(propertyName);
		if (value != null) {
			return value;
		}
		return defaultValue;
	}

	/**
	 * Convinient method for handling non-existing key in map by throwing InfrastructureException.
	 *
	 * @param string
	 * @return
	 */
	public LocalDateTime getDateTimeSafe(String propertyName) {
		LocalDateTime value = getDateTime(propertyName);
		if (value != null) {
			return value;
		}
		throw new InfrastructureException(NOT_PRESENT_MSG.formatted(propertyName));
	}

	/**
	 * Convinient method for handling non-existing key in map by returning defaultValue.
	 *
	 * @param string
	 * @param maximumDate
	 * @return
	 */
	public Boolean getBooleanDefault(String propertyName, Boolean defaultValue) {
		Boolean value = getBoolean(propertyName);
		if (value != null) {
			return value;
		}
		return defaultValue;
	}

	/**
	 * Convinient method for handling non-existing key in map by throwing InfrastructureException.
	 *
	 * @param string
	 * @return
	 */
	public Boolean getBooleanSafe(String propertyName) {
		Boolean value = getBoolean(propertyName);
		if (value != null) {
			return value;
		}
		throw new InfrastructureException(NOT_PRESENT_MSG.formatted(propertyName));
	}

	/**
	 * Convinient method for handling non-existing key in map by returning defaultValue.
	 *
	 * @param string
	 * @param maximumDate
	 * @return
	 */
	public Integer getIntegerDefault(String propertyName, Integer defaultValue) {
		Integer value = getInteger(propertyName);
		if (value != null) {
			return value;
		}
		return defaultValue;
	}

	/**
	 * Convinient method for handling non-existing key in map by throwing InfrastructureException.
	 *
	 * @param string
	 * @return
	 */
	public Integer getIntegerSafe(String propertyName) {
		Integer value = getInteger(propertyName);
		if (value != null) {
			return value;
		}
		throw new InfrastructureException(NOT_PRESENT_MSG.formatted(propertyName));
	}

	/**
	 * Convinient method for handling non-existing key in map by returning defaultValue.
	 *
	 * @param string
	 * @param maximumDate
	 * @return
	 */
	public String getStringDefault(String propertyName, String defaultValue) {
		String value = getString(propertyName);
		if (value != null) {
			return value;
		}
		return defaultValue;
	}

	/**
	 * Convinient method for handling non-existing key in map by throwing InfrastructureException.
	 *
	 * @param string
	 * @return
	 */
	public String getStringSafe(String propertyName) {
		String value = getString(propertyName);
		if (value != null) {
			return value;
		}
		throw new InfrastructureException(NOT_PRESENT_MSG.formatted(propertyName));
	}

	/**
	 * Checks if the input map contains at least one of the specified keys.
	 *
	 * @return
	 */
	public boolean containsAnyKey(String... keys) {
		if (keys == null || keys.length == 0) {
			return false;
		}
		for (String item : keys) {
			if (containsKey(item)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsAllKeys(String... keys) {
		if (keys == null || keys.length == 0) {
			return false;
		}
		for (String item : keys) {
			if (!containsKey(item)) {
				return false;
			}
		}
		return true;
	}

	public TypeMap convertToLong(String... columnNames) {
		for (String column : columnNames) {
			Object value = get(column);
			if (column == null || value == null) {
				continue;
			}

			put(column, CONVERTER.toLong(value));
		}

		return this;
	}

	public TypeMap convertToBoolean(String... columnNames) {
		handleBooleans(columnNames);
		return this;
	}

	public TypeMap convertToBigDecimal(String... columnNames) {
		for (String column : columnNames) {
			Object value = get(column);
			if (column == null || value == null) {
				continue;
			}

			put(column, CONVERTER.toBigDecimal(value));
		}
		return this;
	}

	public TypeMap convertToString(String... columnNames) {
		for (String column : columnNames) {
			Object value = get(column);
			if (column == null || value == null) {
				continue;
			}

			put(column, CONVERTER.toString(value));
		}
		return this;
	}

	public TypeMap convertToLocalDate(String... columnNames) {
		for (String column : columnNames) {
			Object value = get(column);
			if (column == null || value == null) {
				continue;
			}

			LocalDate localDate = CONVERTER.toLocalDate(value);
			if (localDate != null) {
				put(column, localDate);
			}
		}

		return this;
	}

	public TypeMap convertToDateTime(String... columnNames) {
		for (String column : columnNames) {
			Object value = get(column);
			if (column == null || value == null) {
				continue;
			}

			put(column, CONVERTER.toLocalDateTime(value));
		}
		return this;
	}

	/**
	 * Converts boolean(with key in columnsNames) variables from String/int to Boolean variables if value is null then variable wont be converted if variable is
	 * in(1, 1l, BigDecimal.ONE, "Y", "YES", "y","true","TRUE","T","t") then true if variable in(0,0l, -1l,-1, BigDecimal.ZERO,new BigDecimal(-1), "N", "NO",
	 * "n","false","FALSE","F","f") then false
	 *
	 * @param columnNames
	 * @return
	 * @throws IllegalArgumentException
	 */
	public TypeMap handleBooleans(String... columnNames) {
		for (String column : columnNames) {
			Object value = get(column);
			if (column == null || value == null) {
				continue;
			}

			if (TRUE_VALUES.contains(value)) {
				put(column, Boolean.TRUE);
			} else if (FALSE_VALUES.contains(value)) {
				put(column, Boolean.FALSE);
			} else {
				throw new InfrastructureException(TYPE_CONVERSION_MESSAGE_CODE, column, value);
			}
		}

		return this;
	}

	/**
	 * Converts numeric(with key in columnsNames) variable from String to Long
	 *
	 * @param columnNames
	 * @return
	 * @throws IllegalArgumentException
	 */
	public TypeMap handleLongFromstring(String... columnNames) {

		for (String column : columnNames) {
			Object value = get(column);
			if (column == null || value == null) {
				continue;
			}
			try {
				put(column, Long.parseLong(value.toString()));
			} catch (NumberFormatException e) {
				throw new InfrastructureException(TYPE_CONVERSION_MESSAGE_CODE, e, null, column, value);
			}
		}
		return this;
	}

	/**
	 * Converts booleans(with key in columnsNames) variable to Long
	 *
	 * @param columnNames
	 * @return
	 * @throws IllegalArgumentException
	 */
	public TypeMap handleLongFromBooleans(String... columnNames) {
		for (String column : columnNames) {
			Object value = get(column);
			if (column == null || value == null) {
				continue;
			}

			if (TRUE_VALUES.contains(value)) {
				put(column, 1L);
			} else if (FALSE_VALUES.contains(value)) {
				put(column, 0L);
			} else {
				throw new InfrastructureException(TYPE_CONVERSION_MESSAGE_CODE, column, value);
			}
		}

		return this;
	}

	/**
	 * Guava-style put method.
	 *
	 * @param data
	 *            must contain even number of values, which will be put into the map as key-value pairs.
	 *
	 * @throws IllegalArgumentException
	 *             if the numer of input data is not even
	 * @throws IllegalArgumentException
	 *             if the type of the key-positionized data is not java.lang.String
	 */
	public void put(Object... data) {
		putInternal(data);
	}

	private void putInternal(Object... data) {
		if (data == null || data.length == 0) {
			return;
		}
		if (data.length % 2 == 1) {
			throw new IllegalArgumentException("Input data do not represent key-value pairs.");
		}
		for (int i = 0; i < data.length; i += 2) {
			Object key = data[i];
			if (key instanceof String stringKey) {
				put(stringKey, data[i + 1]);
			} else {
				throw new IllegalArgumentException("Input data keys must be of type java.lang.String.");
			}
		}
	}

	/**
	 * Returns 0 is value is null or false, este returns 1.
	 *
	 * @param key
	 * @return
	 */
	public int getBooleanToIntegerSafe(String key) {
		return Boolean.TRUE.equals(getBoolean(key)) ? 1 : 0;
	}

	@SuppressWarnings("unchecked")
	public TypeMap getTypeMap(String key) {
		Object value = get(key);

		if (value == null) {
			return null; // NOSONAR standard Map behavior
		}

		TypeMap result;
		
		if (value instanceof TypeMap typeMap) {
			result = typeMap;
		} else {
			Map<String, Object> innerMap = null;
			try {
				innerMap = (Map<String, Object>) value;
			} catch (ClassCastException e) {
				throw new InfrastructureException(TYPE_CONVERSION_MESSAGE_CODE, e, null, key, get(key));
			}
			result = new TypeMap(innerMap);
			// promoting further mutability of this instance as this is what
			// you would expect from this data structure, now this method has
			// deterministic behavior as 
			put(key, result);
		}

		return nilObjectAsNull(result);
	}

	// Fix object contains only xsi:nil
	// Old source: rel[i].nCount == 0 is equivalent to java null object
	private TypeMap nilObjectAsNull(TypeMap result) {
		if (result.keySet().size() == 1 && Boolean.TRUE.equals(result.getBoolean("@xsi:nil"))) {
			return null; // NOSONAR this is the purpose of this method
		}
		return result; 
	}

	public TypeMap getTypeMapDefault(String key) {
		TypeMap map = getTypeMap(key);
		return map == null ? new TypeMap() : map;
	}

	/**
	 * Get list from {@link TypeMap} by key. If key was not found or key value is <code>null</code> an empty {@link ArrayList} instance is returned.
	 *
	 * @param key
	 *            to lookup in {@link TypeMap}
	 * @return in case the typemap does not contain key it returns empty {@link ArrayList} instance
	 */
	public <T> List<T> getListDefault(String key) {
		List<T> result = getList(key);
		if (result == null) {
			return new ArrayList<>();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getList(String key) {
		List<T> list = null;
		try {
			list = (List<T>) get(key);
		} catch (ClassCastException e) {
			throw new InfrastructureException(TYPE_CONVERSION_MESSAGE_CODE, e, null, key, get(key));
		}

		if (list != null && !list.isEmpty() && (list.get(0) instanceof Map) && !(list.get(0) instanceof TypeMap)) {

			List<Object> arrayList = new ArrayList<>();

			for (Object object : list) {
				arrayList.add(new TypeMap((Map<String, Object>) object));
			}
			// promoting further mutability of this instance as this is what
			// you would expect from this data structure, also this way,
			// ongoing retrievals for this key will be faster
			put(key, arrayList);
			return (List<T>) arrayList;
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getListOfMap(String key) {
		try {
			return (List<Map<String, Object>>) get(key);
		} catch (ClassCastException e) {
			throw new InfrastructureException(TYPE_CONVERSION_MESSAGE_CODE, e, null, key, get(key));
		}
	}

	public TypeMap unmodifiable() {
		return new TypeMap(Collections.unmodifiableMap(this));
	}

	public void recursiveMerge(TypeMap arg0) {
		for (Entry<String, Object> entry : arg0.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if(get(key) == null) {
				put(key, value);
				continue;
			}

			if(value instanceof List) {
				List<TypeMap> list = getList(key);
				@SuppressWarnings("unchecked")
				List<TypeMap> listValue = (List<TypeMap>) value;
				Assert.isTrue(list.size() == listValue.size(), "List sizes have to be equal to merge");

				for(int i = 0; i<list.size(); i++) {
					list.get(i).recursiveMerge(listValue.get(i));
				}
			} else if(value instanceof Map) {
				getTypeMap(key).recursiveMerge((TypeMap) value);
			} else {
				put(key, value);
			}
		}
	}

	public void putIfValueNotNull(String fieldName, Object value) {
		if(value != null) {
			put(fieldName, value);
		}
	}
	
	/**
	 * Converts all key names to lowercase.
	 */
	public void convertKeysToLowerCase() {
		Set<String> keySet = new HashSet<>(this.keySet());
		for (String keyName : keySet) {
			renameKey(keyName, keyName.toLowerCase());
		}
	}
	
	public static TypeMap create(Map<String, Object> input) {
		return new TypeMap(input);
	}

	public static TypeMap createFromDb(Map<String, Object> input) {
		TypeMap res = new TypeMap();
		for (Map.Entry<String, Object> entry : input.entrySet()) {
			Object value = entry.getValue();
			res.put(entry.getKey(), convertDbValue(value));
		}
		return res;
	}

	private static Object convertDbValue(Object value) {
		if (value instanceof Date date) {
			return date.toLocalDate();
		}
		if (value instanceof Timestamp timestamp) {
			return timestamp.toLocalDateTime();
		}
		return value;
	}

	public Stream<Map.Entry<String, Object>> stream() {
		return this.entrySet().stream();
	}
}
