package genlib.exceptions.format;

public class SelectorStringFormatException extends RuntimeException {		
	/** for serialization */
	private static final long serialVersionUID = 7174233067055025669L;

	public SelectorStringFormatException() {
		super();
	}
	
	public SelectorStringFormatException(String s) {
		super(s);
	}
}
