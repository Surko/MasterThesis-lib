package genlib.plugins;

import java.util.HashMap;

import genlib.evolution.population.IPopulation;

/**
 * Class that represents plugin for population containers. It implements Plugin
 * interface, so it must implement methods on how to add plugin and where is the
 * storage for the plugins.
 * 
 * @author Lukas Surin
 *
 */
@SuppressWarnings("rawtypes")
public abstract class PopulationPlugin implements Plugin<Class<? extends IPopulation>>{
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<String, Class<? extends IPopulation>> getStorage() {
		return PluginManager.populationTypes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPlugin(String key, Class<? extends IPopulation> pluginClass) {
		PluginManager.populationTypes.put(key, pluginClass);		
	}

	/**
	 * Method that is called from plugin manager which initializes the
	 * population types. It is done by calling addPlugin method.
	 */
	public abstract void initPopulations();	
}
