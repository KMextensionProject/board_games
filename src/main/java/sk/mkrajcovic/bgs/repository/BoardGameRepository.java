package sk.mkrajcovic.bgs.repository;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sk.mkrajcovic.bgs.dto.BoardGameSearchCriteria;
import sk.mkrajcovic.bgs.entity.BoardGame;

public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {

	// N+1 problem here. I decided to leave it as is for now because using a
	// native query and explicit mappings would not offer significant benefits.

	// FIXME: maybe write this select differently?
	String BOARD_GAME_SEARCH_QUERY = """
		SELECT DISTINCT bg
		FROM BoardGame bg
		LEFT JOIN FETCH bg.authors a
		WHERE (:#{#filter.title} IS NULL OR bg.titleNormalized LIKE %:#{#filter.title}%)
		AND (:#{#filter.estimatedPlayTimeFrom} IS NULL OR bg.estimatedPlayTime >= :#{#filter.estimatedPlayTimeFrom})
		AND (:#{#filter.estimatedPlayTimeTo} IS NULL OR bg.estimatedPlayTime <= :#{#filter.estimatedPlayTimeTo})
		AND (:#{#filter.minPlayers} IS NULL OR bg.minPlayers >= :#{#filter.minPlayers})
		AND (:#{#filter.maxPlayers} IS NULL OR bg.maxPlayers <= :#{#filter.maxPlayers})
		AND (:#{#filter.minAge} IS NULL OR bg.ageRange.minAge >= :#{#filter.minAge})
		AND (:#{#filter.maxAge} IS NULL OR bg.ageRange.maxAge <= :#{#filter.maxAge})
		AND (:#{#filter.isCooperative} IS NULL OR (bg.isCooperative IS NULL OR bg.isCooperative = :#{#filter.isCooperative}))
		AND (:#{#filter.isExtension} IS NULL OR bg.isExtension = :#{#filter.isExtension})
		AND (:#{#filter.canPlayOnlyOnce} IS NULL OR bg.canPlayOnlyOnce = :#{#filter.canPlayOnlyOnce})
		AND (:#{#filter.year} IS NULL OR bg.yearPublished = :#{#filter.year})
		AND (:#{#filter.author} IS NULL OR EXISTS (
			 SELECT 1
			 FROM bg.authors a
			 WHERE a.nameNormalized LIKE %:#{#filter.author}%))
		ORDER BY bg.title ASC""";

	@Query(BOARD_GAME_SEARCH_QUERY)
	public List<BoardGameSearchProjection> getAllByParams(
		@Param("filter") BoardGameSearchCriteria filter
	);

	// if the order by were missing, we would get duplicates here
	@Query(BOARD_GAME_SEARCH_QUERY)
	public Stream<BoardGameSearchProjection> streamAllByParams(
		@Param("filter") BoardGameSearchCriteria filter
	);

	interface BoardGameSearchProjection {
		Long getId();
		String getTitle();
		Integer getEstimatedPlayTime();
		Integer getMinPlayers();
		Integer getMaxPlayers();
		Boolean getIsCooperative();
		Boolean getCanPlayOnlyOnce();
		Boolean getIsExtension();
		String getTutorialUrl();
		Integer getYearPublished();
		AgeRangeProjection getAgeRange();
		List<AuthorProjection> getAuthors();
	}

	interface AgeRangeProjection {
		Integer getMinAge();
		Integer getMaxAge();
	}

	interface AuthorProjection {
		Long getId();
		String getName();
	}
}
