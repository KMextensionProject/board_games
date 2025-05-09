package sk.mkrajcovic.bgs.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

@Configuration
@ComponentScan(basePackages = "sk.mkrajcovic.bgs")
public class WebMvcConfig implements WebMvcConfigurer {

	/**
	 * Configures a custom {@link ObjectMapper} for the application.
	 * <p>
	 * The entire backend operates in UTC, and this custom {@link ObjectMapper} is
	 * used to serialize {@link LocalDateTime} objects with a 'Z' suffix to indicate
	 * UTC time.
	 * </p>
	 *
	 * @return a custom-configured {@link ObjectMapper} that includes the
	 *         {@link DateTimeSerializer} for {@link LocalDateTime} objects.
	 */
	@Bean
	ObjectMapper getObjectMapper() {
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_DATE));
		javaTimeModule.addSerializer(LocalDateTime.class, new DateTimeSerializer());

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(javaTimeModule);
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		return mapper;
	}
}
