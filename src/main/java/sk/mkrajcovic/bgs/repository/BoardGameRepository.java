package sk.mkrajcovic.bgs.repository;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sk.mkrajcovic.bgs.dto.BoardGameSearchCriteria;
import sk.mkrajcovic.bgs.entity.BoardGame;
import sk.mkrajcovic.bgs.repository.AuthorRepository.AuthorSearchProjection;

@Repository
public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {

	final String BOARD_GAME_SEARCH_QUERY = """
		SELECT DISTINCT bg
		FROM BoardGame bg
		LEFT JOIN FETCH bg.authors a
		WHERE (:#{#filter.title} IS NULL OR bg.titleNormalized LIKE %:#{#filter.title}%)
		AND (:#{#filter.estimatedPlayTime} IS NULL OR bg.estimatedPlayTime = :#{#filter.estimatedPlayTime})
		AND (:#{#filter.minPlayers} IS NULL OR bg.minPlayers = :#{#filter.minPlayers})
		AND (:#{#filter.maxPlayers} IS NULL OR bg.maxPlayers = :#{#filter.maxPlayers})
		AND (:#{#filter.minAge} IS NULL OR bg.ageRange.minAge = :#{#filter.minAge})
		AND (:#{#filter.maxAge} IS NULL OR bg.ageRange.maxAge = :#{#filter.maxAge})
		AND (:#{#filter.isCooperative} IS NULL OR bg.isCooperative = :#{#filter.isCooperative})
		AND (:#{#filter.isExtension} IS NULL OR bg.isExtension = :#{#filter.isExtension})
		AND (:#{#filter.canPlayOnlyOnce} IS NULL OR bg.canPlayOnlyOnce = :#{#filter.canPlayOnlyOnce})
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
		AgeRangeProjection getAgeRange();
		/*
		 * Since create/update operations rely only on author names, it would be ideal
		 * to return a List<String> of names here, excluding IDs. However, this isn't
		 * directly achievable with interface-based projections. Consider introducing a
		 * separate DTO.
		 */
		List<AuthorSearchProjection> getAuthors();
		Boolean getIsCooperative();
		Boolean getCanPlayOnlyOnce();
		Boolean getIsExtension();

		@JsonIgnore
		String getTutorialUrl();
	}

	interface AgeRangeProjection {
		Integer getMinAge();
		Integer getMaxAge();
	}
}
