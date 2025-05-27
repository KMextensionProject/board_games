package sk.mkrajcovic.bgs.web;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CallContext {

	private static final Logger LOG = LoggerFactory.getLogger(CallContext.class);

	public String getUserName() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			return authentication.getName();
		}
		LOG.warn("Principal name not present in authentication object");
		return "Anonymous";
	}

	public boolean isUserInRole(String role) {
		if (StringUtils.isBlank(role)) {
			return false;
		}
		for (SimpleGrantedAuthority simpleGrantedAuthority : getUserAuthorities()) {
			if (simpleGrantedAuthority.getAuthority().equals(role)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private Collection<SimpleGrantedAuthority> getUserAuthorities() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null
			? (Collection<SimpleGrantedAuthority>) authentication.getAuthorities()
			: Collections.emptyList();
	}
	
}
