package sk.mkrajcovic.bgs.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk.mkrajcovic.bgs.entity.Author;

@Setter
@Getter
@NoArgsConstructor
public class AuthorDtoOut {

	private String name;

	public AuthorDtoOut(Author author) {
		this.name = author.getName();
	}
}
