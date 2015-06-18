package genlib.exceptions;

public class TypeParameterException extends RuntimeException {

	/** for serialization */
	private static final long serialVersionUID = 8888658537284604010L;

	public TypeParameterException() {
		super();
	}
	
	public TypeParameterException(String s) {
		super(s);
	}
}
