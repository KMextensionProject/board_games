package sk.mkrajcovic.bgs.api;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Author")
public interface AuthorApi {
	
	public List<String> listAuthors(String name);

}
