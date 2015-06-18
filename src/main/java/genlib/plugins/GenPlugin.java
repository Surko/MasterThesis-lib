package genlib.plugins;

import genlib.classifier.gens.PopGenerator;
import genlib.evolution.individuals.Individual;

import java.util.HashMap;

public abstract class GenPlugin implements
		Plugin<Class<? extends PopGenerator<? extends Individual>>> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final HashMap<String, Class<? extends PopGenerator<? extends Individual>>> getStorage() {
		return PluginManager.gens;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void addPlugin(String key,
			Class<? extends PopGenerator<? extends Individual>> pluginClass) {
		PluginManager.gens.put(key, pluginClass);
	}

	/**
	 * Method that is called from plugin manager which initializes the
	 * generators. It is done by calling addPlugin method.
	 */
	public abstract void initGenerators();
}
