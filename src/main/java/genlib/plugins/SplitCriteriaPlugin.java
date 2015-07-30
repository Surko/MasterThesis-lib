package genlib.plugins;

import genlib.splitfunctions.SplitCriteria;

import java.util.HashMap;

/**
 * Class that represents plugin for split criterias. It implements Plugin
 * interface, so it must implement methods on how to add plugin and where is the
 * storage for the plugins.
 * 
 * @author Lukas Surin
 *
 */
public abstract class SplitCriteriaPlugin implements
		Plugin<SplitCriteria<?, ?>> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<String, SplitCriteria<?, ?>> getStorage() {
		return PluginManager.splitCriterias;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPlugin(String key, SplitCriteria<?, ?> pluginClass) {
		PluginManager.splitCriterias.put(key, pluginClass);
	}

	/**
	 * Method that is called from plugin manager which initializes the split
	 * criterias. It is done by calling addPlugin method.
	 */
	public abstract void initCriterias();

}
