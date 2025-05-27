package sk.mkrajcovic.bgs.web;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuditPrincipalProvider implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// there is no authentication in scheduled tasks/non-http context
		return Optional.ofNullable(auth != null ? auth.getName() : "System");
	}
}
