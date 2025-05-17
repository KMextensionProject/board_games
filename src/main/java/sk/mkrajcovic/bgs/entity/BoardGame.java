package sk.mkrajcovic.bgs.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

	@Column(length = 500)
	private String description;

	private Integer estimatedPlayTime;
	private Integer minPlayers;
	private Integer maxPlayers;
	private Boolean isCooperative;
	private Boolean canPlayOnlyOnce;

	@Embedded
	private AgeRange ageRange;

	@ManyToMany
	@JoinTable(
		name = "board_game_author",
		joinColumns = @JoinColumn(name = "board_game_id"),
		inverseJoinColumns = @JoinColumn(name = "author_id"))
	private Set<Author> authors;

	@Getter
	@Setter
	@Embeddable
	@NoArgsConstructor // for JPA
	@AllArgsConstructor
	public static class AgeRange {
		private Integer minAge;
		private Integer maxAge;
	}
}
