package sk.mkrajcovic.bgs.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletResponse;
import static sk.mkrajcovic.bgs.UserRoles.*;
import sk.mkrajcovic.bgs.api.BoardGameApi;
import sk.mkrajcovic.bgs.dto.BoardGameDtoCreate;
import sk.mkrajcovic.bgs.dto.BoardGameDtoFindOut;
import sk.mkrajcovic.bgs.dto.BoardGameDtoOut;
import sk.mkrajcovic.bgs.dto.BoardGameDtoUpdate;
import sk.mkrajcovic.bgs.dto.BoardGameSearchCriteria;
import sk.mkrajcovic.bgs.service.BoardGameService;
import sk.mkrajcovic.bgs.web.filter.CreatedResponseEntity;
import sk.mkrajcovic.bgs.web.validation.ValidationUtils;

@RestController
class BoardGameController implements BoardGameApi {

	private final BoardGameService service;

	BoardGameController(final BoardGameService service) {
		this.service = service;
	}

	@RolesAllowed({BGS_TESTER, BGS_ADMIN})
	@PostMapping(path = "/boardGame", consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> createBoardGame(@RequestBody BoardGameDtoCreate createDto, BindingResult bindingResult) {
		ValidationUtils.processFieldBindingErrors(bindingResult.getFieldErrors());
		Long id = service.createBoardGame(createDto);
		return CreatedResponseEntity.create("/boardGame/{id}", id);
	}

	@RolesAllowed({BGS_TESTER, BGS_ADMIN})
	@PutMapping(path = "/boardGame/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public BoardGameDtoOut updateBoardGame(@PathVariable Long id, @RequestBody BoardGameDtoUpdate updateDto, BindingResult bindingResult) {
		ValidationUtils.processFieldBindingErrors(bindingResult.getFieldErrors());
		return new BoardGameDtoOut(service.updateBoardGame(id, updateDto));
	}

	@GetMapping(path = "/boardGame/{id}", produces = APPLICATION_JSON_VALUE)
	public BoardGameDtoOut getBoardGame(@PathVariable Long id) {
		return new BoardGameDtoOut(service.getBoardGame(id));
	}

	@GetMapping(path = "/boardGame/", produces = APPLICATION_JSON_VALUE)
	public List<BoardGameDtoFindOut> listBoardGames(@ModelAttribute BoardGameSearchCriteria searchCriteria) {
		return service.searchBoardGames(searchCriteria).stream()
			.map(BoardGameDtoFindOut::new)
			.toList();
	}

	@RolesAllowed({BGS_TESTER, BGS_ADMIN})
	@DeleteMapping("/boardGame/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteBoardGame(@PathVariable Long id) {
		service.deleteBoardGame(id);
	}

	@GetMapping(path = "/boardGame/export/")
	public void exportBoardGamesToXlsx(@ModelAttribute BoardGameSearchCriteria searchCriteria, HttpServletResponse response) {
		service.exportBoardGamesToXlsx(searchCriteria, response);
	}
}
