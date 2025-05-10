package sk.mkrajcovic.bgs.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import sk.mkrajcovic.bgs.entity.BoardGame;

@Setter
@Getter
public class BoardGameDtoOut {

	private Long id;
	private long version;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	private String title;
	private Integer minPlayers;
	private Integer maxPlayers;
	private Integer estimatedPlayTime;
	private List<AuthorDtoOut> authors;

	public BoardGameDtoOut(BoardGame boardGame) {
		id = boardGame.getId();
		version = boardGame.getVersion();
		createdAt = boardGame.getCreatedAt();
		modifiedAt = boardGame.getModifiedAt();
		title = boardGame.getTitle();
		minPlayers = boardGame.getMinPlayers();
		maxPlayers = boardGame.getMaxPlayers();
		estimatedPlayTime = boardGame.getEstimatedPlayTime();
		authors = boardGame.getAuthors().stream()
			.map(AuthorDtoOut::new)
			.toList();
	}
}
