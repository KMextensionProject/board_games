package sk.mkrajcovic.bgs.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import sk.mkrajcovic.bgs.entity.BoardGame;
import sk.mkrajcovic.bgs.entity.BoardGame.AgeRange;

@Getter
public class BoardGameDtoOut {

	private Long id;
	private long version;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	private String title;
	private String description;
	private Integer minPlayers;
	private Integer maxPlayers;
	private Integer estimatedPlayTime;
	private AgeRangeDtoOut ageRange;
	private List<AuthorDtoOut> authors;
	
	private Boolean isCooperative;
	private Boolean canPlayOnlyOnce;

	public BoardGameDtoOut(BoardGame boardGame) {
		id = boardGame.getId();
		version = boardGame.getVersion();
		createdAt = boardGame.getCreatedAt();
		modifiedAt = boardGame.getModifiedAt();
		title = boardGame.getTitle();
		description = boardGame.getDescription();
		minPlayers = boardGame.getMinPlayers();
		maxPlayers = boardGame.getMaxPlayers();
		estimatedPlayTime = boardGame.getEstimatedPlayTime();
		isCooperative = boardGame.getIsCooperative();
		canPlayOnlyOnce = boardGame.getCanPlayOnlyOnce();
		ageRange = new AgeRangeDtoOut(boardGame.getAgeRange());
		authors = AuthorDtoOut.mapFromCollection(boardGame.getAuthors());
	}

	@Getter
	public static class AgeRangeDtoOut {
		private Integer minAge;
		private Integer maxAge;

		public AgeRangeDtoOut(AgeRange entity) {
			if (entity == null) {
				return;
			}
			this.minAge = entity.getMinAge();
			this.maxAge = entity.getMaxAge();
		}
	}
}
