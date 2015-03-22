package genlib.exceptions.format;

public class FitCompStringFormatException extends RuntimeException {		
	/** for serialization */
	private static final long serialVersionUID = -1069239507565982309L;

	public FitCompStringFormatException() {
		super();
	}
	
	public FitCompStringFormatException(String s) {
		super(s);
	}
}
