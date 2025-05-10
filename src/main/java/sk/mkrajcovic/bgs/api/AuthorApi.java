package sk.mkrajcovic.bgs.api;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import sk.mkrajcovic.bgs.repository.AuthorRepository.AuthorSearchProjection;

@Tag(name = "Author")
public interface AuthorApi {
	
	public List<AuthorSearchProjection> listAuthors(String name);

}
