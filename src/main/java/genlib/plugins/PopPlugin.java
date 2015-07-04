package genlib.plugins;

import genlib.evolution.individuals.Individual;
import genlib.initializators.PopulationInitializator;

import java.util.HashMap;

public abstract class PopPlugin implements
		Plugin<Class<? extends PopulationInitializator<? extends Individual>>> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<String, Class<? extends PopulationInitializator<? extends Individual>>> getStorage() {
		return PluginManager.popInits;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPlugin(
			String key,
			Class<? extends PopulationInitializator<? extends Individual>> pluginClass) {
		PluginManager.popInits.put(key, pluginClass);

	}

	/**
	 * Method that is called from plugin manager which initializes the
	 * population types. It is done by calling addPlugin method.
	 */
	public abstract void initPopulators();
}
