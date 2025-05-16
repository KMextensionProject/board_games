package sk.mkrajcovic.bgs.web.filter;

import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE - 100)
class RequestLoggingFilter extends OncePerRequestFilter {

	private static final List<HttpMethod> SUPPORTED_HTTP_METHODS = List.of(POST, PUT, PATCH);

	private final ObjectMapper jsonMapper;

	RequestLoggingFilter(final ObjectMapper objectMapper) {
		this.jsonMapper = objectMapper;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if (shouldLog(request)) {
			var requestWrapper = new BufferedRequestWrapper(request);
			logRequest(requestWrapper);
			request = requestWrapper;
		}
		filterChain.doFilter(request, response);
	}

	private boolean shouldLog(HttpServletRequest request) {
		return logger.isDebugEnabled()
			&& MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(request.getContentType())
			&& SUPPORTED_HTTP_METHODS.contains(HttpMethod.valueOf(request.getMethod()));
		// uri patterns
	}

	private void logRequest(BufferedRequestWrapper request) {
		StringBuilder message = new StringBuilder(request.getMethod())
			.append(": ")
			.append(request.getRequestURI());

		if (!StringUtils.isBlank(request.getQueryString())) {
			message.append('?').append(request.getQueryString());
		}

		message.append(", ").append(compactJsonSafely(request.getRequestBody()));
		logger.debug(message);
	}

	private String compactJsonSafely(String jsonPayload) {
		if (StringUtils.isBlank(jsonPayload)) {
			return "<no payload>";
		}
		try {
			Object json = jsonMapper.readValue(jsonPayload, Object.class);
			return jsonMapper.writeValueAsString(json);
		} catch (IOException ex) {
			return "<non-json payload>";
		}
	}
}
