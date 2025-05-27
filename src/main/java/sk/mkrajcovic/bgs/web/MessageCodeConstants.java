package sk.mkrajcovic.bgs.web;

/**
 * Centralized list of message codes.<br>
 * All of these are translated from definitions in
 * <code>messages.properties</code> by {@link NaskMessageSource}
 *
 * @author mkrajcovicux
 */
public class MessageCodeConstants {

	private MessageCodeConstants() {
		throw new IllegalStateException("MessageCodeConstants was not designed to be instantiated");
	}

	public static final String ERROR = "Error";
	public static final String STALE_UPDATE = "staleUpdate";
	public static final String RESOURCE_NOT_FOUND = "resourceNotFound";
	public static final String CANNOT_MODIFY_OTHER_USER_RECORD = "cannotModifyOtherUserRecord";

}
