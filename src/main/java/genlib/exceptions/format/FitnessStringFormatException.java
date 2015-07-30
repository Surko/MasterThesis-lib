package genlib.exceptions.format;

/**
 * Runtime exception that should be thrown if fitness functions string (config) is
 * incorrect.
 * 
 * @author Lukas Surin
 *
 */
public class FitnessStringFormatException extends RuntimeException {
	/** for serialization */
	private static final long serialVersionUID = 6450615861556314504L;

	/**
	 * Default constructor
	 */
	public FitnessStringFormatException() {
		super();
	}

	/**
	 * Constructor with additional message
	 * 
	 * @param s
	 *            message
	 */
	public FitnessStringFormatException(String s) {
		super(s);
	}
}
