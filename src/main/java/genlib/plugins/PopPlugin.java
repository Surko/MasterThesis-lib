package genlib.plugins;

public interface PopPlugin {
	public String getName();
	public void initPopulators();
	public boolean hasError();
}
