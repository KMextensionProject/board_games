package sk.mkrajcovic.bgs.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sk.mkrajcovic.bgs.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

	public Set<Author> findByNameIn(Collection<String> names);

	@Query("""
		SELECT a.name
		FROM Author a
		WHERE (:name IS NULL OR a.nameNormalized LIKE %:name%)
		ORDER BY a.name ASC""")
	public List<String> findAllAuthorsByName(String name);

}
