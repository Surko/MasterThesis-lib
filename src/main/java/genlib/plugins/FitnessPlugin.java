package genlib.plugins;

public interface FitnessPlugin {
	public String getName();
	public void initFitnesses();
	public boolean hasError();
}
