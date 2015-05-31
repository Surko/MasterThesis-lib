package genlib.exceptions.format;

public class PopulationTypeStringFormatException extends RuntimeException {			

	/** for serialization */
	private static final long serialVersionUID = -8982871962048008651L;

	public PopulationTypeStringFormatException() {
		super();
	}
	
	public PopulationTypeStringFormatException(String s) {
		super(s);
	}
}
