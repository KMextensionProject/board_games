package sk.mkrajcovic.bgs.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import sk.mkrajcovic.bgs.dto.BoardGameDtoCreate;
import sk.mkrajcovic.bgs.dto.BoardGameDtoFindOut;
import sk.mkrajcovic.bgs.dto.BoardGameDtoOut;
import sk.mkrajcovic.bgs.dto.BoardGameDtoUpdate;
import sk.mkrajcovic.bgs.dto.BoardGameSearchCriteria;

@Tag(
	name = "Board Game",
	description = """
		Core functionality endpoints for maintaining board game data within the system.<br>
        Access to certain operations is restricted based on user roles."""
)
public interface BoardGameApi {

	@Operation(
		security = @SecurityRequirement(name = "bearerAuth"),
		summary = "Create a new board game",
		description = "Only authenticated users in either `BGS_TESTER` or `BGS_ADMIN` can create new board games.",
		requestBody = @RequestBody(
			required = true,
			content = @Content(
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				schema = @Schema(implementation = BoardGameDtoCreate.class),
				examples = @ExampleObject(value = """
					{
						"title": "Bandido",
						"description": "Zahraj kartu na zablokovanie všetkých únikov. Ak je bandido zablokovaný, tak ste spoločne vyhrali.",
						"tutorialUrl": "https://www.youtube.com/watch?v=ymkGBJdfclc",
						"minPlayers": 1,
						"maxPlayers": 4,
						"estimatedPlayTime": 15,
						"ageRange": {
							"minAge": 6,
							"maxAge": 99
						},
						"authors": [
							"Martin Nedergaard Andersen"
						],
						"isCooperative": true,
						"canPlayOnlyOnce": false,
						"isExtension": false
					}
				""")
			)
		)
	)
	public ResponseEntity<Void> createBoardGame(
		@Valid BoardGameDtoCreate createDto,
		BindingResult bindingResult
	);

	@Operation(summary = "Retrieve detailed information about board game by its ID")
	public BoardGameDtoOut getBoardGame(@NotNull @Positive Long id);

	@Operation(
		security = @SecurityRequirement(name = "bearerAuth"),
		summary = "Update an existing board game",
		description = """	
			Updates the details of an existing board game.<br>
			Only authenticated users in the `BGS_TESTER` or `BGS_ADMIN` role can perform this operation.
			<p>
			Based on the role, the following applies:
			<ul>
			  <li>Testers can only modify board games they personally created</li>
			  <li>Admin can modify any existing board game even those created by testers</li>
			</ul>"""
	)
	public BoardGameDtoOut updateBoardGame(
		@NotNull @Positive Long id,
		@Valid BoardGameDtoUpdate updateDto,
		BindingResult bindingResult
	);

	@Operation(
		summary = "Retrieve a list of board games based on search criteria"
	)
	public List<BoardGameDtoFindOut> listBoardGames(BoardGameSearchCriteria searchCriteria);

	@Operation(
		security = @SecurityRequirement(name = "bearerAuth"),
		summary = "Delete a board game",
		description = """
			Only authenticated users can delete individual board games.<br>
			Users in the `BGS_TESTER` role can delete only those board games they created, not anyone else's.<br>
			Only users in the `BGS_ADMIN` role are authorized to delete any existing board game in the system."""
	)
	public void deleteBoardGame(@NotNull @Positive Long id);

	@Operation(summary = "Export list of board games into XLSX file based on search criteria")
	public void exportBoardGamesToXlsx(
		BoardGameSearchCriteria searchCriteria,
		@Parameter(hidden = true) HttpServletResponse response
	);
}
