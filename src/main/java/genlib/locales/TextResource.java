package genlib.locales;

import genlib.configurations.Config;
import genlib.configurations.PathManager;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that contains resource bundle with loaded static texts for specific
 * locale. To return string, call {@link #getString(String)}.
 * 
 * @author Lukas Surin
 *
 */
public class TextResource {
	private static final Logger LOG = Logger.getLogger(TextResource.class
			.getName());

	/** resource bundle with static texts */
	private static ResourceBundle rb;
	/** message returned when mapping not found */
	public static final String no_value = "#NOT_DEFINED# \n";

	/**
	 * Method gets the value mapped to specific key.
	 * 
	 * @param key
	 *            for which we want value
	 * @return static text for key
	 */
	public static String getString(String key) {
		if (rb == null)
			reinit();

		if (rb.containsKey(key)) {
			return rb.getString(key);
		} else {
			LOG.warning(String.format(PermMessages._loc_noval, key));
			return no_value;
		}
	}

	/**
	 * Reinitializes the TextResource with possible different locale.
	 * 
	 * @return true iff reinitialization takes place
	 */
	public static boolean reinit() {
		if (rb != null
				&& Config.getInstance().getLocale().equals(rb.getLocale()))
			return false;

		if (Config.getInstance().isFileLocalized()) {
			try {
				ClassLoader cl = URLClassLoader
						.newInstance(new URL[] { PathManager.getInstance()
								.getLocalePath().toURI().toURL() });
				rb = ResourceBundle.getBundle("lang", Config.getInstance()
						.getLocale(), cl);
			} catch (Exception e) {
				LOG.severe(e.toString());
			}

		} else {
			rb = ResourceBundle.getBundle("genlib.locales.lang", Config
					.getInstance().getLocale());
		}

		LOG.log(Level.INFO, String.format(PermMessages._loc_changed, Config
				.getInstance().getLocale()));
		return true;
	}

	/**
	 * Clear of all the text saved inside object ResourceBundle. Next reinit
	 * will recognize all the changes in a localization files and returns true.
	 */
	public static void clear() {
		rb = null;
	}

	/**
	 * Method enumerate keys of resource bundle
	 * 
	 * @return enumeration of keys
	 */
	public static Enumeration<String> getKeys() {
		if (rb == null) {
			reinit();
		}

		return rb.getKeys();
	}
}
