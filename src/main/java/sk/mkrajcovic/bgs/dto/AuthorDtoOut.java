package sk.mkrajcovic.bgs.dto;

import lombok.Getter;
import lombok.Setter;
import sk.mkrajcovic.bgs.entity.Author;

@Getter
@Setter
public class AuthorDtoOut {

	private Long id;
	private String name;

	public AuthorDtoOut(Author author) {
		this.id = author.getId();
		this.name = author.getName();
	}

}
