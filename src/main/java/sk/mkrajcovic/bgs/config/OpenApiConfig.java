package sk.mkrajcovic.bgs.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@OpenAPIDefinition(
	servers = @Server(url = "/bgs/svc/"),
	info = @Info(
		title = "Board Games API",
		version = "1.0.0",
		description = """
				A RESTful API for managing your collection of board games, including tracking which games are currently lent out.

				## Authentication

				This API uses OAuth2 with Keycloak.<br>
				To access secured endpoints, you must first obtain an access token.

				### How to obtain an access token:

				Make a `POST` request to the token endpoint with content type `application/x-www-form-urlencoded`:

				```
				POST http://{host}:8080/realms/bgs/protocol/openid-connect/token
				Content-Type: application/x-www-form-urlencoded

				grant_type=password
				&client_id=bgs-rest-api
				&username=your-username
				&password=your-password
				```

				Fill username, and password with your actual credentials.

				### Using the access token

				Include the token in the `Authorization` header of your requests:

				```
				Authorization: Bearer {access_token}
				```""",
		contact = @Contact(
			name = "Martin Krajcovic",
			email = "springapp845@gmail.com"
		)
	)
)
@Configuration
public class OpenApiConfig {
}
