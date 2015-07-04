package genlib.plugins;

import genlib.evolution.individuals.Individual;
import genlib.generators.Generator;

import java.util.HashMap;

public abstract class GenPlugin implements
		Plugin<Class<? extends Generator<? extends Individual>>> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final HashMap<String, Class<? extends Generator<? extends Individual>>> getStorage() {
		return PluginManager.gens;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void addPlugin(String key,
			Class<? extends Generator<? extends Individual>> pluginClass) {
		PluginManager.gens.put(key, pluginClass);
	}

	/**
	 * Method that is called from plugin manager which initializes the
	 * generators. It is done by calling addPlugin method.
	 */
	public abstract void initGenerators();
}
