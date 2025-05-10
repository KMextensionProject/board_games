package sk.mkrajcovic.bgs.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import sk.mkrajcovic.bgs.dto.BoardGameDtoCreate;
import sk.mkrajcovic.bgs.dto.BoardGameDtoOut;
import sk.mkrajcovic.bgs.dto.BoardGameSearchCriteria;
import sk.mkrajcovic.bgs.repository.BoardGameRepository.BoardGameSearchProjection;
import sk.mkrajcovic.bgs.service.BoardGameService;
import sk.mkrajcovic.bgs.utils.ValidationUtils;
import sk.mkrajcovic.bgs.web.filter.CreatedResponseEntity;

@RestController
public class BoardGameController {

	private final BoardGameService service;

	BoardGameController(final BoardGameService service) {
		this.service = service;
	}

	@PostMapping(path = "/boardGame", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createBoardGame(@RequestBody @Valid BoardGameDtoCreate createDto, BindingResult validationResult) {
		ValidationUtils.processFieldBindingErrors(validationResult.getFieldErrors());
		Long id = service.createBoardGame(createDto);
		return CreatedResponseEntity.create("/boardGame/{id}", id);
	}

	@GetMapping(path = "/boardGame/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public BoardGameDtoOut getBoardGame(@PathVariable Long id) {
		return new BoardGameDtoOut(service.getBoardGame(id));
	}

	@GetMapping(path = "/boardGame/", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<BoardGameSearchProjection> listBoardGames(@ModelAttribute BoardGameSearchCriteria searchCriteria, Pageable pageable) {
		return service.searchBoardGames(searchCriteria, pageable);
	}

	@DeleteMapping("/boardGame/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteBoardGame(@PathVariable Long id) {
		service.deleteBoardGame(id);
	}
}
