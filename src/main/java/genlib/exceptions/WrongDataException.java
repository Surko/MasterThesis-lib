package genlib.exceptions;

/**
 * Unchecked exception that should be thrown in the case of not consistent types
 * that is further used in object. </p> <b>For Example :</b> Data instance can
 * work only with Instances of GenLibInstance. Other types of object are
 * prohibited so the exception must be thrown. </p>
 * 
 * @author Lukas Surin
 * @see Data
 */
public class WrongDataException extends RuntimeException {

	/** for serialization */
	private static final long serialVersionUID = -8284526649594695521L;

	/**
	 * Default constructor
	 */
	public WrongDataException() {
		super();
	}

	/**
	 * Constructor with additional message
	 * 
	 * @param s
	 *            message
	 */
	public WrongDataException(String message) {
		super(message);
	}

}
