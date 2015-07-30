package genlib.plugins;

import genlib.evolution.individuals.Individual;
import genlib.generators.Generator;

import java.util.HashMap;

/**
 * Class that represents plugin for individual generators. It implements Plugin interface,
 * so it must implement methods on how to add plugin and where is the storage
 * for the plugins.
 * 
 * @author Lukas Surin
 *
 */
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
