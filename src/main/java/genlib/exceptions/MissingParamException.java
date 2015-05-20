package genlib.exceptions;

public class MissingParamException extends RuntimeException {
	/** for serialization */
	private static final long serialVersionUID = -5628122029141402034L;

	public MissingParamException() {
		super();
	}
	
	public MissingParamException(String s) {
		super(s);
	}
}
