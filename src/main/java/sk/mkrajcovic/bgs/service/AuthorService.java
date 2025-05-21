package sk.mkrajcovic.bgs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import sk.mkrajcovic.bgs.repository.AuthorRepository;
import sk.mkrajcovic.bgs.utils.StringNormalizer;

@Service
public class AuthorService {

	private final AuthorRepository authorRepository;

	public AuthorService(final AuthorRepository authorReporsitory) {
		this.authorRepository = authorReporsitory;
	}

	public List<String> searchAuthors(String name) {
		return authorRepository.findAllAuthorsByName(StringNormalizer.normalize(name));
	}
}
