package genlib.exceptions;

public class CompatibleWekaException extends RuntimeException {
	/** for serialization */
	private static final long serialVersionUID = 6237573950718067793L;

	public CompatibleWekaException() {
		super();
	}
	
	public CompatibleWekaException(String s) {
		super(s);
	}
}
