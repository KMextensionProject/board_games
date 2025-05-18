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
	private String tutorialUrl;
	private Integer minPlayers;
	private Integer maxPlayers;
	private Integer estimatedPlayTime;
	private AgeRangeDtoOut ageRange;
	private List<AuthorDtoOut> authors;
	
	private Boolean isCooperative;
	private Boolean canPlayOnlyOnce;
	private Boolean isExtension;

	/*
	 * 0x2^7, 1x2^6, 1x2^5, 0x2^4, 0x2^3, 1x2^2, 2x2^1, 0x2^0 
	 * 01100110
	 */	

	public BoardGameDtoOut(BoardGame boardGame) {
		if (boardGame == null) {
			return;
		}
		id = boardGame.getId();
		version = boardGame.getVersion();
		createdAt = boardGame.getCreatedAt();
		modifiedAt = boardGame.getModifiedAt();
		title = boardGame.getTitle();
		description = boardGame.getDescription();
		tutorialUrl = boardGame.getTutorialUrl();
		minPlayers = boardGame.getMinPlayers();
		maxPlayers = boardGame.getMaxPlayers();
		estimatedPlayTime = boardGame.getEstimatedPlayTime();
		isCooperative = boardGame.getIsCooperative();
		canPlayOnlyOnce = boardGame.getCanPlayOnlyOnce();
		isExtension = boardGame.getIsExtension();
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
