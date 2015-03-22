package genlib.exceptions.format;

public class OperatorStringFormatException extends RuntimeException {		
	/** for serialization */
	private static final long serialVersionUID = 6750954672559057848L;

	public OperatorStringFormatException() {
		super();
	}
	
	public OperatorStringFormatException(String s) {
		super(s);
	}
}
