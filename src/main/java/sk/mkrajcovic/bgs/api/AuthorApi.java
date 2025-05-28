package sk.mkrajcovic.bgs.api;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
	name = "Author",
	description = "Endpoints for retrieving author-related data."
)
public interface AuthorApi {

	@Operation(
		summary = "Retrieve a list of authors based on search criteria", 
		description = """
			Returns a list of author names matching the optional `name` query parameter.<br>
			The search is case-insensitive, accent-insensitive, and supports partial matches."""
	)
	public List<String> listAuthors(String name);

}
