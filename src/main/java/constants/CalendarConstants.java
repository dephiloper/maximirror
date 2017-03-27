package constants;

public class CalendarConstants {
    public static final String APPLICATION_NAME = "assistantmirror";
    public static final Integer CALENDAR_RESULT_COUNT = 10;

    /** Directory to store user credentials for this application. */
    public static final java.io.File DATA_STORE_DIR =
            new java.io.File(System.getProperty("user.home"), ".credentials/assistantmirror");

}
