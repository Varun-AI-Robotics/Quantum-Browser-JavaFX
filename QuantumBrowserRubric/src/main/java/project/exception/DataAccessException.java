package project.exception;

/**
 * DataAccessException is a custom unchecked exception used to wrap low-level
 * SQLExceptions and translate them into domain-specific runtime failures.
 *
 * <p>This class demonstrates:
 * <ul>
 *   <li>User-defined exception</li>
 *   <li>Proper exception wrapping</li>
 *   <li>Separation between JDBC errors and UI/Servlet layers</li>
 * </ul>
 */
public class DataAccessException extends RuntimeException {

    /**
     * Creates a new DataAccessException with a message and underlying cause.
     *
     * @param message descriptive context about the failure
     * @param cause the original exception (usually SQLException)
     */
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
