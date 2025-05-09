package sk.mkrajcovic.bgs.dto;

import lombok.Getter;
import lombok.Setter;
import sk.mkrajcovic.bgs.entity.BoardGame;

@Setter
@Getter
public class BoardGameDtoOut {

	private Long id;
	private String title;
	private Integer minPlayers;
	private Integer maxPlayers;
	private long version;

	public BoardGameDtoOut(BoardGame entity) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.minPlayers = entity.getMinPlayers();
		this.maxPlayers = entity.getMaxPlayers();
		this.version = entity.getVersion();
	}
}
