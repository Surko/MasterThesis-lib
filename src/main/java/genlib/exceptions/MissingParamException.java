package genlib.exceptions;

/**
 * Runtime exception that should be thrown if there is some missing parameter.
 * 
 * @author Lukas Surin
 *
 */
public class MissingParamException extends RuntimeException {
	/** for serialization */
	private static final long serialVersionUID = -5628122029141402034L;

	/**
	 * Default constructor
	 */
	public MissingParamException() {
		super();
	}

	/**
	 * Constructor with additional message
	 * 
	 * @param s
	 *            message
	 */
	public MissingParamException(String s) {
		super(s);
	}
}
