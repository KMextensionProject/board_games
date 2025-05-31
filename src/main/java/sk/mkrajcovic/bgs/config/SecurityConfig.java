package sk.mkrajcovic.bgs.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfig {

	private static final Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, ServerProperties serverProperties) throws Exception {
        // state in access token only
        http.sessionManagement(sessionConfigurer -> sessionConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(c -> c.disable()); // safe because there is no session
        http.headers(headersConfigurer -> headersConfigurer.frameOptions(FrameOptionsConfig::sameOrigin));

        http.oauth2ResourceServer(cfg -> cfg.jwt(Customizer.withDefaults()));

        http.exceptionHandling(exh -> exh.authenticationEntryPoint((request, response, authException) -> {
            response.addHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer realm=\"confidential\"");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            LOG.error("Error {}", authException);
        }));

        // If SSL enabled, disable http (https only)
        if (serverProperties.getSsl() != null && serverProperties.getSsl().isEnabled()) {
            http.requiresChannel(ch -> ch.anyRequest().requiresSecure());
        }

        http.authorizeHttpRequests(authorise ->
            authorise
                // not secured
                .requestMatchers(
					AntPathRequestMatcher.antMatcher("/home**"),
					AntPathRequestMatcher.antMatcher("/detail**"),
					AntPathRequestMatcher.antMatcher("/index.html"),
					AntPathRequestMatcher.antMatcher("/detail.html"),
					AntPathRequestMatcher.antMatcher("/ping/**"),
					AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/author/**"),
					AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/boardGame/**"),

					// Open-API documentation
					AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
					AntPathRequestMatcher.antMatcher("/api-docs/**"),
					AntPathRequestMatcher.antMatcher("/openapi/**")
				)
                .permitAll()

				 // backoffice
				.anyRequest()
                    .authenticated());

        return http.build();
	}

	/**
	 * Configures a {@link JwtAuthenticationConverter} to extract roles from the JWT
	 * and convert them into Spring Security {@link GrantedAuthority} instances.
	 * This converter processes the "group" claim as roles, maps each role to a
	 * corresponding {@link GrantedAuthority}, and sets the principal claim name as
	 * "upn" (User Principal Name).
	 *
	 * @return a configured {@link JwtAuthenticationConverter} for JWT
	 *         authentication in Spring Security
	 */
	@Bean
	JwtAuthenticationConverter jwtAuthenticationConverter() {
		Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthConverter = jwt -> {
			return extractRolesFromJwt(jwt)
				.stream()
				.map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role))
				.toList();
		};

		var jwtAuthConverter = new JwtAuthenticationConverter();
		jwtAuthConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthConverter);
		jwtAuthConverter.setPrincipalClaimName("name");

		return jwtAuthConverter;
	}

	/**
	 * Extracts the "roles" claim from the given JWT. If the "roles" claim is
	 * absent, an empty collection is returned.
	 */
	@SuppressWarnings("unchecked")
	private Collection<String> extractRolesFromJwt(Jwt jwt) {
		List<String> roles = new ArrayList<>();
		Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
		if (resourceAccess != null) {
			Map<String, Object> bgsRoles = (Map<String, Object>) resourceAccess.get("bgs-rest-api");
			if (bgsRoles != null) {
				List<String> bgsRoleList = (List<String>) bgsRoles.get("roles");
				if (bgsRoleList != null) {
					roles.addAll(bgsRoleList);
				}
			}
		}
		if (roles.isEmpty()) {
			LOG.info("No authorization roles found in JWT");
		}
		return roles;
	}

	/**
	 * Disabling the Spring's "ROLE_" prefix from GrantedAuthories mappers.<br>
	 * Allows to skip the manual prefix handling when we want to use
	 * <code>@PreAuthorize's "hasAnyRole()"</code> or JSR-250
	 * <code>@RolesAllowed</code> with custom authorities/roles.
	 */
	@Bean
	GrantedAuthorityDefaults grantedAuthorityDefaults() {
		return new GrantedAuthorityDefaults("");
	}
}
