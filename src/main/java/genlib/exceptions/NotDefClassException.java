package genlib.exceptions;

public class NotDefClassException extends RuntimeException {

	/** for serialization */
	private static final long serialVersionUID = 4032100986190714109L;

	public NotDefClassException() {
		super();
	}
	
	public NotDefClassException(String s) {
		super(s);
	}
}
