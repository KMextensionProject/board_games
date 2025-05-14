package sk.mkrajcovic.bgs.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import sk.mkrajcovic.bgs.InfrastructureException;
import sk.mkrajcovic.bgs.web.filter.MessageCodeConstants;

/**
 * This class provides convenient methods to load resources of various types
 * from the classpath and convert them into corresponding Java objects.
 *
 * @author mkrajcovicux
 */
public class ResourceLoader {

	private ResourceLoader() {
		throw new IllegalStateException("This class was not designed to be instantiated");
	}

	/**
	 * Loads a resource from the classpath based on the specified resource name.
	 * <p>
	 * This method constructs a resource path using the provided resource name and
	 * the classpath prefix, then attempts to load the resource. If the resource
	 * does not exist on the classpath, an exception is thrown.
	 *
	 * @param resourceName the name of the resource to load. This should be the path
	 *                     relative to the classpath. For example,
	 *                     "report_templates/Pripady.xlsx" would load a resource
	 *                     located at
	 *                     "src/main/resources/report_templates/Pripady.xlsx".
	 * @return a {@link Resource} object representing the loaded resource.
	 * @throws InfrastructureException if the resource cannot be found on the
	 *                                 classpath.
	 */
	public static Resource loadResource(String resourceName) {
		var resourcePath = "classpath:" + resourceName;
		var resource = new DefaultResourceLoader().getResource(resourcePath);
		if (!resource.exists()) {
			throw new InfrastructureException("Unable to find " + resourcePath + " on classpath");
		}
		return resource;
	}

	/**
	 * Loads a JSON resource and returns a {@link JsonReader} instance for further operations.
	 *
	 * @param resourceName the name or path of the JSON resource, not {@code null}
	 * @return a JsonReader instance for reading and converting the JSON resource
	 * @throws NullPointerException if resourceName is {@code null}
	 */
	public static JsonReader loadJson(String resourceName) {
		Objects.requireNonNull(resourceName, "resourceName can not be null");
		return new JsonReader(resourceName);
	}

	/**
	 * Represents a reader for JSON resources, providing methods to convert JSON to
	 * Java types.
	 */
	public static class JsonReader {
		private ObjectMapper objectMapper;
		private Resource resource;

		private JsonReader(String resourceName) {
			objectMapper = new ObjectMapper();
			resource = new DefaultResourceLoader().getResource(resourceName);
		}

		/**
		 * Converts the JSON resource to a Java object of the specified type using
		 * Jackson's ObjectMapper.
		 *
		 * @param typeReference the TypeReference specifying the Java type to convert to
		 * @param <T>           the generic type of the Java object
		 * @return the Java object converted from JSON
		 * @throws InfrastructureException if there is an error reading or converting
		 *                                 the JSON
		 */
		public <T> T asTypeReference(TypeReference<T> typeReference) {
			try {
				return objectMapper.readValue(resource.getInputStream(), typeReference);
			} catch (IOException ioex) {
				throw new InfrastructureException(MessageCodeConstants.ERROR, ioex);
			}
		}

		/**
		 * Converts the JSON resource to a Map.
		 *
		 * @param <K> the type of keys in the Map
		 * @param <V> the type of values in the Map
		 * @return a mutable Map representation of the JSON resource
		 */
		public <K, V> Map<K, V> asMap() {
			return asTypeReference(new TypeReference<Map<K, V>>(){});
		}

		/**
		 * Converts the JSON resource to a List.
		 *
		 * @param <T> the type of elements in the List
		 * @return a mutable List representation of the JSON resource
		 */
		public <T> List<T> asList() {
			return asTypeReference(new TypeReference<List<T>>(){});
		}
	}
}
