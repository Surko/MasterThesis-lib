package genlib.exceptions;

/**
 * Runtime exception that should be thrown if something can't handle numeric
 * attributes.
 * 
 * @author Lukas Surin
 *
 */
public class NumericHandleException extends RuntimeException {

	/** for serialization */
	private static final long serialVersionUID = -5369702574554563551L;

	/**
	 * Default constructor
	 */
	public NumericHandleException() {
		super();
	}

	/**
	 * Constructor with additional message
	 * 
	 * @param s
	 *            message
	 */
	public NumericHandleException(String s) {
		super(s);
	}
}
