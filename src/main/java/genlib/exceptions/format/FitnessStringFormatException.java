package genlib.exceptions.format;

public class FitnessStringFormatException extends RuntimeException {	
	/** for serialization */
	private static final long serialVersionUID = 6450615861556314504L;

	public FitnessStringFormatException() {
		super();
	}
	
	public FitnessStringFormatException(String s) {
		super(s);
	}
}
