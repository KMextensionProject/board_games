package sk.mkrajcovic.bgs.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class BoardGame extends BaseEntity {

	@Setter(AccessLevel.NONE)
	@Version
	private long version;

	@Column(nullable = false, length = 50)
	private String title;

	private Integer estimatedPlayTime;
	private Integer minPlayers;
	private Integer maxPlayers;

	@ManyToMany
	@JoinTable(
		name = "board_game_author",
		joinColumns = @JoinColumn(name = "board_game_id"),
		inverseJoinColumns = @JoinColumn(name = "author_id"))
	private Set<Author> authors;

}
