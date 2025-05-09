package sk.mkrajcovic.bgs.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import sk.mkrajcovic.bgs.dto.BoardGameDtoCreate;
import sk.mkrajcovic.bgs.dto.BoardGameSearchCriteria;
import sk.mkrajcovic.bgs.entity.Author;
import sk.mkrajcovic.bgs.entity.BoardGame;
import sk.mkrajcovic.bgs.repository.AuthorRepository;
import sk.mkrajcovic.bgs.repository.BoardGameRepository;
import sk.mkrajcovic.bgs.repository.BoardGameRepository.BoardGameSearchProjection;
import sk.mkrajcovic.bgs.utils.EntityUtils;

@Service
public class BoardGameService {

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
		boardGame.setTitle(createDto.getTitle());
		boardGame.setMinPlayers(createDto.getMinPlayers());
		boardGame.setMaxPlayers(createDto.getMaxPlayers());
		boardGame.setEstimatedPlayTime(createDto.getEstimatedPlayTime());
		boardGame.setAuthors(findOrCreateAuthors(createDto.getAuthors()));

		return boardGameRepository.save(boardGame).getId();
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

	public Page<BoardGameSearchProjection> searchBoardGames(BoardGameSearchCriteria searchCriteria, Pageable pageable) {
		return boardGameRepository.pageAllByParams(searchCriteria, pageable);
	}

	public void deleteBoardGame(Long id) {
		var boardGame = EntityUtils.getExistingEntityById(boardGameRepository, id);
		boardGameRepository.delete(boardGame);
	}

}
