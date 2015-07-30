package genlib.exceptions;

/**
 * Runtime exception that should be thrown if there is some problem when
 * initializing node.
 * 
 * @author Lukas Surin
 *
 */
public class NodeCreationException extends RuntimeException {

	/** for serialization */
	private static final long serialVersionUID = -4240020935279966L;

	/**
	 * Default constructor
	 */
	public NodeCreationException() {
		super();
	}

	/**
	 * Constructor with additional message
	 * 
	 * @param s
	 *            message
	 */
	public NodeCreationException(String s) {
		super(s);
	}
}
