package genlib.exceptions;

public class PopulationInitializationException extends RuntimeException {

	/** for serialization */
	private static final long serialVersionUID = 3135466006820089346L;

	public PopulationInitializationException() {
		super();
	}
	
	public PopulationInitializationException(String s) {
		super(s);
	}
}
