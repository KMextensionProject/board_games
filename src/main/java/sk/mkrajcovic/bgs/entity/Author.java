package sk.mkrajcovic.bgs.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Author extends BaseEntity {

	@Column(nullable = false, length = 100, unique = true)
	private String name;

	@ManyToMany(mappedBy = "authors")
	private Set<BoardGame> boardGames;

}
