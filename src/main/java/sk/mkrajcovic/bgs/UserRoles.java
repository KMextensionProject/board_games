package sk.mkrajcovic.bgs;

public class UserRoles {

	/**
	 * Can create new board games, update board games it created and delete them.
	 * Cannot perform any mutable operation on existing records added by BGS_ADMIN.
	 */
	public static final String BGS_TESTER = "BGS_TESTER";

	/**
	 * Has full control over every existing board game.
	 */
	public static final String BGS_ADMIN = "BGS_ADMIN";

}
