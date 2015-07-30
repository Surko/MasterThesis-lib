package genlib.plugins;

import genlib.classifier.Classifier;

import java.util.HashMap;

/**
 * Class that represents plugin for classifiers. It implements Plugin interface,
 * so it must implement methods on how to add plugin and where is the storage
 * for the plugins.
 * 
 * @author Lukas Surin
 *
 */
public abstract class ClassifierPlugin implements
		Plugin<Class<? extends Classifier>> {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final HashMap<String, Class<? extends Classifier>> getStorage() {
		return PluginManager.classifiers;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void addPlugin(String key,
			Class<? extends Classifier> pluginClass) {
		PluginManager.classifiers.put(key, pluginClass);
	}

	/**
	 * Method that is called from plugin manager which initializes the
	 * classifiers. It is done by calling addPlugin method.
	 */
	public abstract void initClassifiers();
}
