package sk.mkrajcovic.bgs.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import sk.mkrajcovic.bgs.entity.Author;
import sk.mkrajcovic.bgs.entity.BoardGame;

@Setter
@Getter
public class BoardGameDtoOut {

	private Long id;
	private long version;
	private String title;
	private Integer minPlayers;
	private Integer maxPlayers;
	private Integer playTime;
	private List<String> authors;

	public BoardGameDtoOut(BoardGame entity) {
		this.id = entity.getId();
		this.version = entity.getVersion();
		this.title = entity.getTitle();
		this.minPlayers = entity.getMinPlayers();
		this.maxPlayers = entity.getMaxPlayers();
		this.playTime = entity.getEstimatedPlayTime();
		this.authors = entity.getAuthors().stream()
			.map(Author::getName)
			.toList();
	}
}
