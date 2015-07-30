package genlib.exceptions;

/**
 * Runtime exception that should be thrown if the field is not initialized yet.
 * 
 * @author Lukas Surin
 *
 */
public class NotInitializedFieldException extends RuntimeException {

	/** for serialization */
	private static final long serialVersionUID = 1424135750153774083L;

	/**
	 * Default constructor
	 */
	public NotInitializedFieldException() {
		super();
	}
	
	/**
	 * Constructor with additional message
	 * 
	 * @param s
	 *            message
	 */
	public NotInitializedFieldException(String s) {
		super(s);
	}
	
}
