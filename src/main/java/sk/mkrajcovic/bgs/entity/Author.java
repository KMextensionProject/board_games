package sk.mkrajcovic.bgs.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import sk.mkrajcovic.bgs.utils.StringNormalizer;

@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = false)
public class Author extends BaseEntity {

	@EqualsAndHashCode.Include
	@Column(nullable = false, length = 100, unique = true)
	private String name;

	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	@Column(nullable = false, length = 100)
	private String nameNormalized;

	@ManyToMany(mappedBy = "authors")
	private Set<BoardGame> boardGames;

	@PrePersist
	protected void runPrePersistOperations() {
		nameNormalized = StringNormalizer.normalize(name);
	}
}
