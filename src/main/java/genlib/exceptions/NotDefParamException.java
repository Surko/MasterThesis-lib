package genlib.exceptions;

/**
 * Runtime exception that should be thrown if there parameter is not defined.
 * 
 * @author Lukas Surin
 *
 */
public class NotDefParamException extends RuntimeException {
	/** for serialization */
	private static final long serialVersionUID = 1874081003550566846L;

	/**
	 * Default constructor
	 */
	public NotDefParamException() {
		super();
	}

	/**
	 * Constructor with additional message
	 * 
	 * @param s
	 *            message
	 */
	public NotDefParamException(String s) {
		super(s);
	}
}
