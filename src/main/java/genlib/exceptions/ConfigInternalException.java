package genlib.exceptions;

public class ConfigInternalException extends RuntimeException {
	/** for serialization */
	private static final long serialVersionUID = -8100181687645161126L;

	public ConfigInternalException() {
		super();
	}
	
	public ConfigInternalException(String s) {
		super(s);
	}
}
