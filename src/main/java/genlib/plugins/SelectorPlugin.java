package genlib.plugins;

import genlib.evolution.selectors.Selector;

import java.util.HashMap;

public abstract class SelectorPlugin implements
		Plugin<Class<? extends Selector>> {

	public enum SelectorEnum {
		ENVSELECTORS, SELECTORS
	}

	/** selector enum with which we distinct what kind of selector we load */
	public SelectorEnum selEnum;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<String, Class<? extends Selector>> getStorage() {
		if (selEnum == null) {
			throw new NullPointerException();
		}

		switch (selEnum) {
		case ENVSELECTORS:
			return PluginManager.envSelectors;
		case SELECTORS:
			return PluginManager.selectors;
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPlugin(String key, Class<? extends Selector> pluginClass) {
		if (selEnum == null) {
			throw new NullPointerException();
		}

		switch (selEnum) {
		case ENVSELECTORS:
			PluginManager.envSelectors.put(key, pluginClass);
		case SELECTORS:
			PluginManager.selectors.put(key, pluginClass);
		}		

	}

	/**
	 * Method that is called from plugin manager which initializes the
	 * selectors. It is done by calling addPlugin method.
	 */
	public abstract void initSelectors();

}
