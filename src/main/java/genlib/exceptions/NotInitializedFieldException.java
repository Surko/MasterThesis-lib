package genlib.exceptions;

public class NotInitializedFieldException extends RuntimeException {

	/** for serialization */
	private static final long serialVersionUID = 1424135750153774083L;

	public NotInitializedFieldException() {
		super();
	}
	
	public NotInitializedFieldException(String s) {
		super(s);
	}
	
}
