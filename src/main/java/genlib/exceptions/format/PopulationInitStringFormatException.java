package genlib.exceptions.format;

/**
 * Runtime exception that should be thrown if population initializer string
 * (config) is incorrect.
 * 
 * @author Lukas Surin
 *
 */
public class PopulationInitStringFormatException extends RuntimeException {

	/** for serialization */
	private static final long serialVersionUID = -3300937232283119051L;

	/**
	 * Default constructor
	 */
	public PopulationInitStringFormatException() {
		super();
	}

	/**
	 * Constructor with additional message
	 * 
	 * @param s
	 *            message
	 */
	public PopulationInitStringFormatException(String s) {
		super(s);
	}
}
