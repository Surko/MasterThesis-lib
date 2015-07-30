package genlib.exceptions;

/**
 * Runtime exception that should be thrown if component is not compatible with
 * weka
 * 
 * @author Lukas Surin
 *
 */
public class CompatibleWekaException extends RuntimeException {
	/** for serialization */
	private static final long serialVersionUID = 6237573950718067793L;

	/**
	 * Default constructor
	 */
	public CompatibleWekaException() {
		super();
	}

	/**
	 * Constructor with additional message
	 * 
	 * @param s
	 *            message
	 */
	public CompatibleWekaException(String s) {
		super(s);
	}
}
