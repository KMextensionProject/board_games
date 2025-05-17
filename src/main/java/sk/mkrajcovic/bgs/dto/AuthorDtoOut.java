package sk.mkrajcovic.bgs.dto;

import java.util.Collection;
import java.util.List;

import lombok.Getter;
import sk.mkrajcovic.bgs.entity.Author;

@Getter
public class AuthorDtoOut {

	private Long id;
	private String name;

	public AuthorDtoOut(Author author) {
		this.id = author.getId();
		this.name = author.getName();
	}

	public static List<AuthorDtoOut> mapFromCollection(Collection<Author> authors) {
		return authors.stream()
			.map(AuthorDtoOut::new)
			.toList();
	}
}
