package genlib.exceptions.format;

/**
 * Runtime exception that should be thrown if operator string (config) is
 * incorrect.
 * 
 * @author Lukas Surin
 *
 */
public class OperatorStringFormatException extends RuntimeException {
	/** for serialization */
	private static final long serialVersionUID = 6750954672559057848L;

	/**
	 * Default constructor
	 */
	public OperatorStringFormatException() {
		super();
	}

	/**
	 * Constructor with additional message
	 * 
	 * @param s
	 *            message
	 */
	public OperatorStringFormatException(String s) {
		super(s);
	}
}
