package genlib.exceptions;

public class EmptyConfigParamException extends RuntimeException {

	/** for serialization */
	private static final long serialVersionUID = -8942797875626211580L;

	public EmptyConfigParamException() {
		super();
	}
	
	public EmptyConfigParamException(String s) {
		super(s);
	}
}
