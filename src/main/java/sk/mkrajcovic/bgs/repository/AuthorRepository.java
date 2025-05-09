package sk.mkrajcovic.bgs.repository;

import java.util.Collection;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sk.mkrajcovic.bgs.entity.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

	public Set<Author> findByNameIn(Collection<String> names);

}
