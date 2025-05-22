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

@Tag(name = "Board Game")
public interface BoardGameApi {

	@Operation(
		summary = "Create a new board game",
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
	public ResponseEntity<?> createBoardGame(
		@Valid BoardGameDtoCreate createDto,
		BindingResult bindingResult
	);

	@Operation(summary = "Retrieve detailed information about board game by its ID")
	public BoardGameDtoOut getBoardGame(@NotNull @Positive Long id);

	@Operation(summary = "Update an existing board game")
	public BoardGameDtoOut updateBoardGame(
		@NotNull @Positive Long id,
		@Valid BoardGameDtoUpdate updateDto,
		BindingResult bindingResult
	);

	@Operation(
		summary = "Retrieve a full list of board games based on search criteria"
	)
	public List<BoardGameDtoFindOut> listBoardGames(BoardGameSearchCriteria searchCriteria);

	@Operation(summary = "Delete a board game")
	public void deleteBoardGame(@NotNull @Positive Long id);

	@Operation(summary = "Export list of board games into XLSX file based on search criteria")
	public void exportBoardGamesToXlsx(
		BoardGameSearchCriteria searchCriteria,
		@Parameter(hidden = true) HttpServletResponse response
	);
}
