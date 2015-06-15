package genlib.exceptions;

public class NotDefParamException extends RuntimeException {
	/** for serialization */
	private static final long serialVersionUID = 1874081003550566846L;

	public NotDefParamException() {
		super();
	}
	
	public NotDefParamException(String s) {
		super(s);
	}
}
