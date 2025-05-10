package sk.mkrajcovic.bgs.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import sk.mkrajcovic.bgs.dto.PingDtoOut;

@Tag(
	name = "System",
	description = "Endpoints for monitoring the system's health, environment information, and user-related data."
			    + "Includes services to check the application and database status, retrieve environment details "
			    + "like build version and active profiles, and access authenticated user information."
)
public interface SystemApi {

	@Operation(
		summary = "Check the status of the application",
		description = "Ping service to check whether the application is up and running."
	)
	public PingDtoOut ping();

}
