package genlib.plugins;

public interface SplitCriteriaPlugin {
	public String getName();
	public void initCriterias();
	public boolean hasError();
}
