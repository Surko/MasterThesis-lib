package genlib.exceptions.format;

/**
 * Runtime exception that should be thrown if selector string (config) is
 * incorrect.
 * 
 * @author Lukas Surin
 *
 */
public class SelectorStringFormatException extends RuntimeException {
	/** for serialization */
	private static final long serialVersionUID = 7174233067055025669L;

	/**
	 * Default constructor
	 */
	public SelectorStringFormatException() {
		super();
	}

	/**
	 * Constructor with additional message
	 * 
	 * @param s
	 *            message
	 */
	public SelectorStringFormatException(String s) {
		super(s);
	}
}
