package sk.mkrajcovic.bgs.service;

import static sk.mkrajcovic.bgs.utils.FileNameGenerator.generateFileNameWithTimestamp;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;
import sk.mkrajcovic.bgs.InfrastructureException;
import sk.mkrajcovic.bgs.dto.BoardGameDtoCreate;
import sk.mkrajcovic.bgs.dto.BoardGameDtoIn;
import sk.mkrajcovic.bgs.dto.BoardGameDtoUpdate;
import sk.mkrajcovic.bgs.dto.BoardGameSearchCriteria;
import sk.mkrajcovic.bgs.entity.Author;
import sk.mkrajcovic.bgs.entity.BoardGame;
import sk.mkrajcovic.bgs.repository.AuthorRepository;
import sk.mkrajcovic.bgs.repository.AuthorRepository.AuthorSearchProjection;
import sk.mkrajcovic.bgs.repository.BoardGameRepository;
import sk.mkrajcovic.bgs.repository.BoardGameRepository.BoardGameSearchProjection;
import sk.mkrajcovic.bgs.utils.EntityUtils;
import sk.mkrajcovic.bgs.utils.StringNormalizer;
import sk.mkrajcovic.bgs.utils.TypeMap;
import sk.mkrajcovic.bgs.utils.XlsxUtils;
import sk.mkrajcovic.bgs.web.MessageCodeConstants;

@Service
public class BoardGameService {

	private static final String BOARD_GAME_XLSX_TEMPLATE = "templates/exports/xlsx/BoardGamesTemplate.xlsx";

	private final BoardGameRepository boardGameRepository;
	private final AuthorRepository authorRepository;

	public BoardGameService(
			final BoardGameRepository boardGameRepository,
			final AuthorRepository authorRepository) {

		this.boardGameRepository = boardGameRepository;
		this.authorRepository = authorRepository;
	}

	@Transactional
	public Long createBoardGame(BoardGameDtoCreate createDto) {
		var boardGame = new BoardGame();
		setBoardGameFields(boardGame, createDto);
		return boardGameRepository.save(boardGame).getId();
	}

	@Transactional
	public BoardGame updateBoardGame(Long id, BoardGameDtoUpdate updateDto) {
		var boardGame = EntityUtils.getExistingEntityById(boardGameRepository, id);
		EntityUtils.checkStaleUpdate(updateDto.getVersion(), boardGame.getVersion());
		setBoardGameFields(boardGame, updateDto);
		return boardGameRepository.save(boardGame);
	}

	private void setBoardGameFields(BoardGame boardGame, BoardGameDtoIn dtoIn) {
		boardGame.setTitle(dtoIn.getTitle());
		boardGame.setDescription(dtoIn.getDescription());
		boardGame.setMinPlayers(dtoIn.getMinPlayers());
		boardGame.setMaxPlayers(dtoIn.getMaxPlayers());
		boardGame.setEstimatedPlayTime(dtoIn.getEstimatedPlayTime());
		boardGame.setAgeRange(dtoIn.getAgeRange().asEntity());
		boardGame.setAuthors(findOrCreateAuthors(dtoIn.getAuthors()));
		boardGame.setCanPlayOnlyOnce(dtoIn.getCanPlayOnlyOnce());
		boardGame.setIsCooperative(dtoIn.getIsCooperative());
		boardGame.setIsExtension(dtoIn.getIsExtension());
	}

	private Set<Author> findOrCreateAuthors(Set<String> inputAuthors) {
		// normalize author names from input
		Set<String> authorNames = new HashSet<>(10);
		inputAuthors.forEach(authorName -> authorNames.add(authorName.trim()));

		Set<Author> authorsFromDb = new HashSet<>(authorRepository.findByNameIn(authorNames));

		// identify new authors to be created
		Set<Author> authorsToBeCreated = new HashSet<>();
		for (String authorName : authorNames) {

			boolean saveAuthor = authorsFromDb.stream()
				.noneMatch(author -> author.getName().equals(authorName));

			if (saveAuthor) {
				Author newAuthor = new Author();
				newAuthor.setName(authorName);
				authorsToBeCreated.add(newAuthor);
			}
		}

		if (!authorsToBeCreated.isEmpty()) {
			authorRepository.saveAll(authorsToBeCreated);
			authorsFromDb.addAll(authorsToBeCreated);
		}

		return authorsFromDb;
	}

	public BoardGame getBoardGame(Long id) {
		return EntityUtils.getExistingEntityById(boardGameRepository, id);
	}

	public void deleteBoardGame(Long id) {
		var boardGame = EntityUtils.getExistingEntityById(boardGameRepository, id);
		boardGameRepository.delete(boardGame);
	}

	public List<BoardGameSearchProjection> searchBoardGames(BoardGameSearchCriteria searchCriteria) {
		normalizeSearchCriteria(searchCriteria);
		return boardGameRepository.getAllByParams(searchCriteria);
	}

	private void normalizeSearchCriteria(BoardGameSearchCriteria searchCriteria) {
		searchCriteria.setTitle(StringNormalizer.normalize(searchCriteria.getTitle()));
		searchCriteria.setAuthor(StringNormalizer.normalize(searchCriteria.getAuthor()));
	}

	@Transactional(readOnly = true)
	public void exportBoardGamesToXlsx(BoardGameSearchCriteria searchCriteria, HttpServletResponse response) {
		normalizeSearchCriteria(searchCriteria);
		try (Stream<TypeMap> boardGameStream = boardGameRepository.streamAllByParams(searchCriteria)
				.map(this::convertToTypeMap)) {

			XlsxUtils.generateXlsx(
				BOARD_GAME_XLSX_TEMPLATE,
				boardGameStream,
				generateFileNameWithTimestamp("BoarGames", ".xlsx"),
				response
			);
		} catch (IOException ioex) {
			throw new InfrastructureException(MessageCodeConstants.ERROR, ioex);
		}
	}

	private TypeMap convertToTypeMap(BoardGameSearchProjection boardGame) {
		return new TypeMap(
			"id", boardGame.getId(),
			"title", boardGame.getTitle(),
			"minPlayers", boardGame.getMinPlayers(),
			"maxPlayers", boardGame.getMaxPlayers(),
			"playTime", boardGame.getEstimatedPlayTime(),
			"authors", boardGame.getAuthors()
				.stream()
				.map(AuthorSearchProjection::getName)
				.collect(Collectors.joining(", "))
		);
	}
}
