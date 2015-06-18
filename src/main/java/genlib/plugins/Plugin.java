package genlib.plugins;

import java.util.HashMap;

/**
 * Plugin interface which is implemented by all kinds of plugins (Operators,
 * CLassifier,...).
 * 
 * @author Lukas Surin
 *
 * @param <T>
 *            describes the behavior of plugin
 */
public interface Plugin<T> {
	/**
	 * Method returns the hashmap with loaded plugins. </p> Hashmap is inside
	 * PluginManager class.
	 * 
	 * @return hashmap with loaded plugins
	 */
	public HashMap<String, T> getStorage();

	/**
	 * Method adds the plugin specified in pluginClass param into hashmap with
	 * param key. </p> Hashmap is inside PluginManager class.
	 * 
	 * @param key
	 *            to hashmap with added plugin
	 * @param pluginClass
	 *            of type class to save into hashmap
	 */
	public void addPlugin(String key, T pluginClass);

	/**
	 * Name of the plugin.
	 * 
	 * @return name of plugin
	 */
	public String getName();

	/**
	 * Tests if the plugin has any error
	 * 
	 * @return true if there is any error.
	 */
	public boolean hasError();
}
