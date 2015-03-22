package genlib.exceptions;

public class EmptyEnvironmentSelectorException extends RuntimeException {
	/** for serialization */
	private static final long serialVersionUID = -1679602703159084936L;

	public EmptyEnvironmentSelectorException() {
		super();
	}
	
	public EmptyEnvironmentSelectorException(String s) {
		super(s);
	}
}
