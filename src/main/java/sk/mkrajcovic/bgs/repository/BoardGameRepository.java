package sk.mkrajcovic.bgs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sk.mkrajcovic.bgs.dto.BoardGameSearchCriteria;
import sk.mkrajcovic.bgs.entity.BoardGame;
import sk.mkrajcovic.bgs.repository.AuthorRepository.AuthorSearchProjection;

@Repository
public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {

	final String BOARD_GAME_SEARCH_QUERY = """
		SELECT DISTINCT bg
		FROM BoardGame bg
		LEFT JOIN FETCH bg.authors a
		WHERE (:#{#filter.title} IS NULL OR bg.title LIKE %:#{#filter.title}%)
		AND (:#{#filter.minPlayers} IS NULL OR bg.minPlayers = :#{#filter.minPlayers})
		AND (:#{#filter.maxPlayers} IS NULL OR bg.maxPlayers = :#{#filter.maxPlayers})
		AND (:#{#filter.playTime} IS NULL OR bg.estimatedPlayTime = :#{#filter.playTime})
		AND (:#{#filter.author} IS NULL OR EXISTS (
			 SELECT 1
			 FROM bg.authors a
			 WHERE a.name LIKE %:#{#filter.author}%))""";

	@Query(BOARD_GAME_SEARCH_QUERY)
	public List<BoardGameSearchProjection> getAllByParams(
		@Param("filter") BoardGameSearchCriteria filter);

	interface BoardGameSearchProjection {
		Long getId();
		String getTitle();
		Integer getMinPlayers();
		Integer getMaxPlayers();
		Integer getEstimatedPlayTime();
		List<AuthorSearchProjection> getAuthors();
	}
}
