package genlib.plugins;

public interface SelectorPlugin {
	public String getName();
	public void initSelectors();
	public boolean hasError();
}
