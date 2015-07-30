package genlib.exceptions;

/**
 * Runtime exception that should be thrown if there is some internal error with
 * config initialization.
 * 
 * @author Lukas Surin
 *
 */
public class ConfigInternalException extends RuntimeException {
	/** for serialization */
	private static final long serialVersionUID = -8100181687645161126L;

	/**
	 * Default constructor
	 */
	public ConfigInternalException() {
		super();
	}

	/**
	 * Constructor with additional message
	 * 
	 * @param s
	 *            message
	 */
	public ConfigInternalException(String s) {
		super(s);
	}
}
