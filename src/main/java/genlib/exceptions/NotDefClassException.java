package genlib.exceptions;

/**
 * Runtime exception that should be thrown if the class is not defined yet.
 * 
 * @author Lukas Surin
 *
 */
public class NotDefClassException extends RuntimeException {

	/** for serialization */
	private static final long serialVersionUID = 4032100986190714109L;

	/**
	 * Default constructor
	 */
	public NotDefClassException() {
		super();
	}

	/**
	 * Constructor with additional message
	 * 
	 * @param s
	 *            message
	 */
	public NotDefClassException(String s) {
		super(s);
	}
}
