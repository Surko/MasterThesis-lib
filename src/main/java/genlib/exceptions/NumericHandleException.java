package genlib.exceptions;

public class NumericHandleException extends RuntimeException {
	
	/** for serialization */
	private static final long serialVersionUID = -5369702574554563551L;

	public NumericHandleException() {
		super();
	}
	
	public NumericHandleException(String s) {
		super(s);
	}
}
