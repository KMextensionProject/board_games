package sk.mkrajcovic.bgs.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BoardGameSearchCriteria {

	private String title;
	private Integer minPlayers;
	private Integer maxPlayers;
	private Integer playTime;
//	private String author;

}
