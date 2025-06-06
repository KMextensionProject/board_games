package sk.mkrajcovic.bgs.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Custom serializer for serializing {@link LocalDateTime} objects to JSON.<br>
 * This serializer converts {@link LocalDateTime} objects to a string in ISO
 * date-time format with a 'Z' suffix to indicate UTC time.
 */
class DateTimeSerializer extends StdSerializer<LocalDateTime> {

	private static final long serialVersionUID = -2738812860784215324L;

	/**
	 * Serializes a {@link LocalDateTime} object to JSON.
	 *
	 * @param value       the {@link LocalDateTime} value to serialize
	 * @param gen         the {@link JsonGenerator} used to write JSON content
	 * @param serializers the {@link SerializerProvider} that can be used to get
	 *                    serializers for serializing objects value contains, if any
	 * @throws IOException if an I/O error occurs
	 */
	public DateTimeSerializer() {
		super(LocalDateTime.class);
	}

	@Override
	public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		if (value == null) {
			gen.writeNull();
			return;
		}
		ZonedDateTime zonedDateTime = value.atZone(ZoneOffset.UTC);
		gen.writeString(DateTimeFormatter.ISO_DATE_TIME.format(zonedDateTime));
	}
}
