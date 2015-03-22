package genlib.exceptions;

public class NodeCreationException extends RuntimeException {
	
	/** for serialization */
	private static final long serialVersionUID = -4240020935279966L;

	public NodeCreationException() {
		super();
	}
	
	public NodeCreationException(String s) {
		super(s);
	}
}
