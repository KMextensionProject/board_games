package sk.mkrajcovic.bgs.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sk.mkrajcovic.bgs.dto.BoardGameSearchCriteria;
import sk.mkrajcovic.bgs.entity.BoardGame;

@Repository
public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {

	// -- TODO add authors as concatenated list of their names
	final String BOARD_GAME_SEARCH_QUERY = """
		SELECT bg.id AS id,
		       bg.title AS title,
		       bg.minPlayers AS minPlayers,
		       bg.maxPlayers AS maxPlayers,
		       bg.estimatedPlayTime AS playTime
		FROM BoardGame bg
		WHERE (:#{#filter.title} IS NULL OR bg.title = :#{#filter.title})
		AND (:#{#filter.minPlayers} IS NULL OR bg.minPlayers = :#{#filter.minPlayers})
		AND (:#{#filter.maxPlayers} IS NULL OR bg.maxPlayers = :#{#filter.maxPlayers})
		AND (:#{#filter.playTime} IS NULL OR bg.estimatedPlayTime = :#{#filter.playTime})""";

	@Query(BOARD_GAME_SEARCH_QUERY)
	public Page<BoardGameSearchProjection> pageAllByParams(
		@Param("filter") BoardGameSearchCriteria filter,
		Pageable pageRequest
	);

	interface BoardGameSearchProjection {
		Long getId();
		String getTitle();
		Integer getMinPlayers();
		Integer getMaxPlayers();
		Integer getPlayTime();
	}
}
