package sk.mkrajcovic.bgs.utils;

import static java.util.Objects.isNull;

import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;

import sk.mkrajcovic.bgs.ClientException;
import sk.mkrajcovic.bgs.web.MessageCodeConstants;

public class EntityUtils {

	private EntityUtils() {
		throw new UnsupportedOperationException("This class was not designed to be instantiated");
	}

	/**
	 * Checks for stale updates between two entities based on their
	 * version number.
	 * <p>
	 * Use this method as pre-check before hitting the database, as it may be
	 * beneficial by reducing useless database access and providing better user
	 * friendly message about the issue.
	 *
	 * @throws ClientException          if the versions of the entities differ
	 */
	public static void checkStaleUpdate(long e1Version, long e2Version) {
		if (e1Version != e2Version) {
			throw new ClientException(
				HttpStatus.CONFLICT,
				MessageCodeConstants.STALE_UPDATE
			);
		}
	}

	/**
	 * Retrieves an entity from the given {@link CrudRepository} if it is found,
	 * otherwise throws client error with initialized with
	 * {@link HttpStatus#NOT_FOUND} and
	 * {@link MessageCodeConstants#RESOURCE_NOT_FOUND}.
	 *
	 * @param repository from which to retrieve the entity
	 * @param id primary identifier of the entity
	 * @return a valid entity object from the given repository
	 * @throws ClientException if the entity is not found in repository
	 * @throws IllegalArgumentException if either argument is {@code null}
	 */
	public static <T> T getExistingEntityById(CrudRepository<T, Long> repository, Long id) {
		if (isNull(repository) || isNull(id)) {
			throw new IllegalArgumentException("Neither of arguments can be null to successfully run query for an entity");
		}
		return repository.findById(id)
						 .orElseThrow(() -> new ClientException(
								 HttpStatus.NOT_FOUND, 
								 MessageCodeConstants.RESOURCE_NOT_FOUND
						 ));
	}
}
