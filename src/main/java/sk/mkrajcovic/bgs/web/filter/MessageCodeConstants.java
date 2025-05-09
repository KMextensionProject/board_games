package sk.mkrajcovic.bgs.web.filter;

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
	public static final String NASK_ERROR = "naskError";
	public static final String STALE_UPDATE = "staleUpdate";
	public static final String RESOURCE_NOT_FOUND = "resourceNotFound";
	public static final String DATABASE_CONNECTION_FAILURE = "databaseConnectionFailure";
	public static final String UNEXPECTED_DATA_COUNT = "unexpectedDataCount";

	public static final String MISSING_CASE_TYPE = "missingCaseType";
	public static final String MISSING_SED_TYPE = "missingSedType";
	public static final String SED_XSD_NOT_FOUND_FOR_TYPE = "sedXsdNotFoundForType";
	public static final String SED_XSD_INVALID_VERSION = "sedXsdInvalidVersion";
	public static final String SED_XSD_VALIDATION = "sedXsdValidation";
	public static final String SED_XSD_VALIDATION_IM = "sedXsdValidationIM";
	public static final String BAD_HEADER_FORMAT = "badHeaderFormat";
	public static final String BAD_HEADER_PAGING_RANGE = "badPagingRange";
	public static final String CASE_TYPE_NOT_SUPPORTED = "caseTypeNotSupported";
	public static final String SED_TYPE_NOT_SUPPORTED = "sedTypNotSupported";
	public static final String NO_CASE_FOR_DOCUMENT = "noCaseForDocument";
	public static final String INVALID_INITIAL_SED_TYPE = "invalidInitialSedType";
	public static final String INCOMPLETE_DATE_RANGE_FOR_REPORT = "incompleteDateRangeReport";

	public static final String CASE_NOT_IN_MY_OFFICE = "caseNotInMyOffice";
	public static final String CASE_ALREADY_ASSIGNED = "caseAlreadyAssigned";
	public static final String INVALID_USER_FOR_CASE_ASSIGNMENT = "invalidUserForCaseAssignment";
	public static final String USER_DOES_NOT_EXIST = "userDoesNotExist";
	public static final String USER_NOT_ASSIGNED_TO_OFFICE = "userNotAssignedToOffice";
	public static final String USER_NOT_ASSIGNED_TO_ANY_OFFICE = "userNotAssignedToAnyOffice";
	public static final String REASSIGNMENT_USERS_IDENTICAL = "reassignmentUsersIdentical";

	public static final String INVALID_PIN_SEQUENCE = "invalidPinSequence";
}
