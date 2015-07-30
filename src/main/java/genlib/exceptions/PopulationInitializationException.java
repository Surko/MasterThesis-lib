package genlib.exceptions;

/**
 * Runtime exception that should be thrown if there is some problem when
 * creating starting population with population initializer.
 * 
 * @author Lukas Surin
 *
 */
public class PopulationInitializationException extends RuntimeException {

	/** for serialization */
	private static final long serialVersionUID = 3135466006820089346L;

	/**
	 * Default constructor
	 */
	public PopulationInitializationException() {
		super();
	}

	/**
	 * Constructor with additional message
	 * 
	 * @param s
	 *            message
	 */
	public PopulationInitializationException(String s) {
		super(s);
	}
}
