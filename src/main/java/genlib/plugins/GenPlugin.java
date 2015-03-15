package genlib.plugins;

public interface GenPlugin {
	public String getName();
	public void initGenerators();
	public boolean hasError();
}
