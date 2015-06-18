package genlib.plugins;

import genlib.classifier.splitting.SplitCriteria;

import java.util.HashMap;

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
	 * Method that is called from plugin manager which initializes the
	 * split criterias. It is done by calling addPlugin method.
	 */
	public abstract void initCriterias();

}
