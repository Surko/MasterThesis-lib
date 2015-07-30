package genlib.exceptions;

/**
 * Runtime exception that should be thrown if there is some empty configuration
 * parameter.
 * 
 * @author Lukas Surin
 *
 */
public class EmptyConfigParamException extends RuntimeException {

	/** for serialization */
	private static final long serialVersionUID = -8942797875626211580L;

	/**
	 * Default constructor
	 */
	public EmptyConfigParamException() {
		super();
	}

	/**
	 * Constructor with additional message
	 * 
	 * @param s
	 *            message
	 */
	public EmptyConfigParamException(String s) {
		super(s);
	}
}
