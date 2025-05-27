package sk.mkrajcovic.bgs.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sk.mkrajcovic.bgs.api.AuthorApi;
import sk.mkrajcovic.bgs.service.AuthorService;

@RestController
class AuthorController implements AuthorApi {

	private final AuthorService authorService;

	AuthorController(final AuthorService service) {
		this.authorService = service;
	}

	@GetMapping(path = "/author/", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> listAuthors(@RequestParam(required = false) String name) {
		return authorService.searchAuthors(name);
	}

}
