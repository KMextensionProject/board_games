package sk.mkrajcovic.bgs.web.filter;

import java.io.IOException;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import sk.mkrajcovic.bgs.web.CallContext;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE - 100)
class RequestCallTimeFilter implements Filter {

	private static final Logger LOG = LoggerFactory.getLogger(RequestCallTimeFilter.class);

	private CallContext callContext;

	@Autowired
	void setCallContext(final CallContext callContext) {
		this.callContext = callContext;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		if (!(servletRequest instanceof HttpServletRequest)) {
			throw new IllegalArgumentException("servletRequest not instanceof HttpServletRequest");
		}
		long startTime = System.currentTimeMillis();

		String caller = "anonymousUser".equals(callContext.getUserName())
			? servletRequest.getRemoteHost()
			: callContext.getUserName();

		LOG.info("Request started by {}", caller);

		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		String method = httpServletRequest.getMethod();
		String uri = httpServletRequest.getRequestURI();
		String query = httpServletRequest.getQueryString();

		UriComponentsBuilder builder = UriComponentsBuilder.fromPath(uri);

		if (query != null) {
			builder.query(query);
		}

		String requestInfo = method + ":" + builder.build().toUriString();

		try {
			filterChain.doFilter(servletRequest, servletResponse);
		} finally {
			long endTime = System.currentTimeMillis();
			if (LOG.isInfoEnabled()) {
				LOG.info("Request: {} Request calltime: {}", requestInfo,
						DurationFormatUtils.formatDurationHMS(endTime - startTime));
			}
		}
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		// do nothing
	}

	@Override
	public void destroy() {
		// do nothing
	}
}
