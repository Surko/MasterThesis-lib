package genlib.exceptions.format;

/**
 * Runtime exception that should be thrown if population container string
 * (config) is incorrect.
 * 
 * @author Lukas Surin
 *
 */
public class PopulationTypeStringFormatException extends RuntimeException {

	/** for serialization */
	private static final long serialVersionUID = -8982871962048008651L;

	/**
	 * Default constructor
	 */
	public PopulationTypeStringFormatException() {
		super();
	}

	/**
	 * Constructor with additional message
	 * 
	 * @param s
	 *            message
	 */
	public PopulationTypeStringFormatException(String s) {
		super(s);
	}
}
