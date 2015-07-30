package genlib.exceptions;

/**
 * Runtime exception that should be thrown if there is some problem with generic
 * types in Java (different than it should be).
 * 
 * @author Lukas Surin
 *
 */
public class TypeParameterException extends RuntimeException {

	/** for serialization */
	private static final long serialVersionUID = 8888658537284604010L;

	/**
	 * Default constructor
	 */
	public TypeParameterException() {
		super();
	}

	/**
	 * Constructor with additional message
	 * 
	 * @param s
	 *            message
	 */
	public TypeParameterException(String s) {
		super(s);
	}
}
