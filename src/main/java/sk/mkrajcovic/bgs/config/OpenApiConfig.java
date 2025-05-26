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
	info = @Info(
		title = "Board Games API", 
		version = "1.0.0", 
		description = "A RESTful API for managing your collection of board games, including tracking which games are currently lent out.",
		contact = @Contact(
			name = "Martin Krajcovic", 
			email = ""
		)
	),
	servers = @Server(url = "/bgs/svc/")
)
@Configuration
class OpenApiConfig {

}
