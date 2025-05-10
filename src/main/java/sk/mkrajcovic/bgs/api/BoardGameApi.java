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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import sk.mkrajcovic.bgs.dto.BoardGameDtoCreate;
import sk.mkrajcovic.bgs.dto.BoardGameDtoOut;
import sk.mkrajcovic.bgs.dto.BoardGameSearchCriteria;
import sk.mkrajcovic.bgs.repository.BoardGameRepository.BoardGameSearchProjection;

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
						"title": "My test board game",
						"minPlayers": 1,
						"maxPlayers": 7,
						"estimatedPlayTime": 30,
						"authors": [
							"Frank Nowitsky",
							"Susan Lombard",
							"Daenish Portorea"
						]
					}
				""")
			)
		)
	)
	public ResponseEntity<?> createBoardGame(
		@Valid BoardGameDtoCreate createDto, // try validated on controller, if the override is possible there
		@Parameter(hidden = true) BindingResult validationResult);

	@Operation(summary = "Retrieve detailed information about board game by its ID")
	public BoardGameDtoOut getBoardGame(@NotNull @Positive Long id);

	@Operation(
		summary = "Retrieve a full list of board games based on search criteria"
	)
	public List<BoardGameSearchProjection> listBoardGames(BoardGameSearchCriteria searchCriteria);

	@Operation(summary = "Delete a board game")
	public void deleteBoardGame(@NotNull @Positive Long id);
}
