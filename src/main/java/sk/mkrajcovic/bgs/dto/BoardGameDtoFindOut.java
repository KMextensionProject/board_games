package sk.mkrajcovic.bgs.dto;

import java.util.List;

import lombok.Getter;
import sk.mkrajcovic.bgs.dto.BoardGameDtoOut.AgeRangeDtoOut;
import sk.mkrajcovic.bgs.repository.BoardGameRepository.AuthorProjection;
import sk.mkrajcovic.bgs.repository.BoardGameRepository.BoardGameSearchProjection;

// introduced because we needed this:
// 1) authors as List<String>, which is not possible with projections and JPQL
// 2) omit multiple properties in cleaner way than extending BoardGameDtoOut for which we would need to override getters with @JsonIgnore
@Getter
public class BoardGameDtoFindOut {

	private Long id;
	private String title;
	private Integer minPlayers;
	private Integer maxPlayers;
	private Integer estimatedPlayTime;
	private Boolean isCooperative;
	private Boolean canPlayOnlyOnce;
	private Boolean isExtension;
	private Integer year;
	private AgeRangeDtoOut ageRange;
	private List<String> authors;

	public BoardGameDtoFindOut(BoardGameSearchProjection projection) {
		id = projection.getId();
		title = projection.getTitle();
		minPlayers = projection.getMinPlayers();
		maxPlayers = projection.getMaxPlayers();
		estimatedPlayTime = projection.getEstimatedPlayTime();
		isCooperative = projection.getIsCooperative();
		canPlayOnlyOnce = projection.getCanPlayOnlyOnce();
		isExtension = projection.getIsExtension();
		year = projection.getYearPublished();

		if (projection.getAgeRange() != null) {
			ageRange = new AgeRangeDtoOut();
			ageRange.setMinAge(projection.getAgeRange().getMinAge());
			ageRange.setMaxAge(projection.getAgeRange().getMaxAge());
		}

		authors = projection.getAuthors().stream()
			.map(AuthorProjection::getName)
			.sorted()
			.toList();
	}
}
