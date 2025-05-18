package sk.mkrajcovic.bgs.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BoardGameSearchCriteria {

	private String title;
	private Integer estimatedPlayTime;
	private Integer minPlayers;
	private Integer maxPlayers;
	private Integer minAge;
	private Integer maxAge;
	private Boolean isCooperative;
	private Boolean canPlayOnlyOnce;
	private Boolean isExtension;
	private String author;
}
