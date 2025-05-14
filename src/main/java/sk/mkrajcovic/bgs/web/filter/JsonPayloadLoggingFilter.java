package sk.mkrajcovic.bgs.web.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class JsonPayloadLoggingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // wrap the request to cache the input stream
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

        // continue with the filter chain so that the body gets cached
        filterChain.doFilter(wrappedRequest, response);

        // read the cached body after the request has been processed
        String body = new String(wrappedRequest.getContentAsByteArray(), wrappedRequest.getCharacterEncoding());

        // log the payload if it is JSON
        if (isJsonContent(request.getContentType())) {
            String compact = compactJsonSafely(body);
        	logger.debug("Request payload: "+request.getMethod()+""+request.getRequestURI()+", payload="+ compact);
        }
    }

    // check if the request's content type is JSON
    private boolean isJsonContent(String contentType) {
        return contentType != null && contentType.contains("application/json");
    }

    // safely compact the JSON payload to remove unnecessary whitespaces
    private String compactJsonSafely(String input) {
        if (input == null || input.isBlank()) return "<no payload>";

        try {
            Object json = objectMapper.readValue(input, Object.class);
            return objectMapper.writeValueAsString(json);  // Compact JSON
        } catch (Exception e) {
            return "<non-json payload>";
        }
    }
}

//@Component
//@Order(1) // Ensure it's early in the chain
//public class JsonPayloadLoggingFilter extends OncePerRequestFilter {
//
//	private final ObjectMapper objectMapper = new ObjectMapper();
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//
//		// Only wrap if not already wrapped
//		HttpServletRequest wrappedRequest = request instanceof ContentCachingRequestWrapper ? request
//				: new ContentCachingRequestWrapper(request);
//
//		// Read the input stream to force content caching
//		String body = readBody((ContentCachingRequestWrapper) wrappedRequest);
//
//		// Log if JSON
//		if (isJsonContent(request.getContentType())) {
//			String compact = compactJsonSafely(body);
//			logger.debug("Incoming request: "+request.getMethod()+""+request.getRequestURI()+", payload="+ compact);
//		}
//
//		// Continue the chain
//		filterChain.doFilter(wrappedRequest, response);
//	}
//
//	private boolean isJsonContent(String contentType) {
//		return contentType != null && contentType.contains("application/json");
//	}
//
//	private String readBody(ContentCachingRequestWrapper request) {
//		try {
//			ServletInputStream inputStream = request.getInputStream();
//			byte[] buffer = StreamUtils.copyToByteArray(inputStream);
//			return new String(buffer, request.getCharacterEncoding());
//		} catch (IOException e) {
//			logger.warn("Could not read request body", e);
//			return null;
//		}
//	}
//
//	private String compactJsonSafely(String input) {
//		if (input == null || input.isBlank())
//			return "<no payload>";
//
//		try {
//			Object json = objectMapper.readValue(input, Object.class);
//			return objectMapper.writeValueAsString(json); // Compact JSON
//		} catch (Exception e) {
//			return "<non-json payload>";
//		}
//	}
//}

//
//import java.io.IOException;
//import java.util.Objects;
//
//import org.springframework.web.filter.CommonsRequestLoggingFilter;
//import org.springframework.web.util.ContentCachingRequestWrapper;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//public class JsonPayloadLoggingFilter extends CommonsRequestLoggingFilter {
//
//	@Override
//	protected boolean shouldLog(HttpServletRequest request) {
//		String uri = request.getRequestURI();
//		String contentType = request.getContentType();
//
//		// use pattern rather
//		return uri.startsWith("/bgs/svc/") 
//			&& Objects.equals(contentType, "application/json"); 
//	}
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//	        throws ServletException, IOException {
//
//	    HttpServletRequest wrappedRequest = new ContentCachingRequestWrapper(request);
//
//	    beforeRequest(wrappedRequest, getBeforeMessage(wrappedRequest));
//
//	    try {
//	        filterChain.doFilter(wrappedRequest, response);
//	    } finally {
//	        // No afterRequest(), since you're doing logging only in beforeRequest
//	    }
//	}
//	
//	@Override
//	protected void beforeRequest(HttpServletRequest request, String message) {
//		 String rawPayload = getMessagePayload(request);
//		    String compactPayload = compactJsonSafely(rawPayload);
//		    logger.debug("Incoming request: " + request.getMethod() + " " + request.getRequestURI() +
//		                 ", payload=" + compactPayload);
//	}
//	
//	private String compactJsonSafely(String input) {
//	    try {
//	        ObjectMapper mapper = new ObjectMapper();
//	        Object json = mapper.readValue(input, Object.class);
//	        return mapper.writeValueAsString(json); // Minified JSON
//	    } catch (Exception e) {
//	        // Fallback if input is not JSON: replace multiple whitespace with single space
//	        return input.replaceAll("\\s+", " ");
//	    }
//	}
//
//	@Override
//	protected void afterRequest(HttpServletRequest request, String message) {
//		String prefix = "payload=";
//		int idx = message.indexOf(prefix);
//		if (idx != -1) {
//			String payload = message.substring(idx + prefix.length());
//			logger.debug(payload.replaceAll("\\s+", ""));
//		}
//	}
//}
