package genlib.plugins;

import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.individuals.Individual;

import java.util.HashMap;

/**
 * Class that represents plugin for fitness functions. It implements Plugin interface,
 * so it must implement methods on how to add plugin and where is the storage
 * for the plugins.
 * 
 * @author Lukas Surin
 *
 */
public abstract class FitnessPlugin implements
		Plugin<Class<? extends FitnessFunction<? extends Individual>>> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final HashMap<String, Class<? extends FitnessFunction<? extends Individual>>> getStorage() {
		return PluginManager.fitFuncs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void addPlugin(String key,
			Class<? extends FitnessFunction<? extends Individual>> pluginClass) {
		PluginManager.fitFuncs.put(key, pluginClass);
	}

	/**
	 * Method that is called from plugin manager which initializes the fitness
	 * functions. It is done by calling addPlugin method.
	 */
	public abstract void initFitnesses();
}
