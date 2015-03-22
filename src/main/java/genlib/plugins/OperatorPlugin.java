package genlib.plugins;

public interface OperatorPlugin {
	public String getName();
	public void initOperators();
	public boolean hasError();
}

