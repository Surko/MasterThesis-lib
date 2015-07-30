package genlib.exceptions.format;

/**
 * Runtime exception that should be thrown if fitness comparator string (config) is
 * incorrect.
 * 
 * @author Lukas Surin
 *
 */
public class FitCompStringFormatException extends RuntimeException {
	/** for serialization */
	private static final long serialVersionUID = -1069239507565982309L;

	/**
	 * Default constructor
	 */
	public FitCompStringFormatException() {
		super();
	}

	/**
	 * Constructor with additional message
	 * 
	 * @param s
	 *            message
	 */
	public FitCompStringFormatException(String s) {
		super(s);
	}
}
