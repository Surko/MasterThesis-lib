package genlib.configurations;

import genlib.GenLib;
import genlib.locales.PermMessages;

import java.io.File;
import java.net.URL;

public class PathManager {

	/** instance of this PathManager */
	private static PathManager instance;

	/** Default root directory */
	private File rootPath;
	/** Path to local files */
	private File localePath;
	/** Path to plugins */
	private File pluginPath;
	/** Path to logs */
	private File logPath;

	/**
	 * Method which will return instance of PathManager. Uniqueness of this
	 * instance can be done via singleton design pattern.
	 * 
	 * @return singleton instance of PathManager
	 */
	public static PathManager getInstance() {
		if (instance == null) {
			instance = new PathManager();
			// Instance of this PathManager is universal for this run.
			instance.init();
		}
		return instance;
	}

	/**
	 * Initialization of PathManager with default paths. </br> Root directory is
	 * a place where the application is located. </br> Plugin directory is
	 * inside root directory under the name plugins. </br> User directory can be
	 * changed throughout the run of application but the default value is the
	 * same as root directory. This directory contains future saved files,
	 * databases.
	 */
	public void init() {
		/*
		 * Ziskame lokaciu triedy. Pri spustani v IDE vrati zlozku. Pri spustani
		 * z jar zase cestu k jaru.
		 */

		URL url = GenLib.class.getProtectionDomain().getCodeSource()
				.getLocation();
		//
		File _pRoot = new File(url.getFile());
		if (_pRoot.isDirectory()) {
			rootPath = _pRoot;
		} else {
			rootPath = new File(_pRoot.getParent());
		}

		// Ostatne cesty nastavime priamo z root
		try {
			localePath = new File(rootPath, "locales");
			localePath.mkdir();
			pluginPath = new File(rootPath, "plugins");
			pluginPath.mkdir();
			logPath = new File(rootPath, "_logs");
			logPath.mkdir();
		} catch (SecurityException se) {
			System.err.println(PermMessages._security_exc);
		}
	}

	/**
	 * 
	 * Getter which will get you root path. Root directory is starter location
	 * or in other words place where the application resides. Other directories
	 * are branching from here.
	 * 
	 * @see #getPluginPath()
	 * @return root directory as a file
	 */
	public File getRootPath() {
		return rootPath;
	}

	/**
	 * Getter which return path with locales.
	 * 
	 * Locales directory exists in rootDirectory under dir of name locales.
	 * 
	 * @return locales path
	 */
	public File getLocalePath() {
		return localePath;
	}

	/**
	 * Getter which return log path.
	 * 
	 * Log directory exists in rootDirectory under dir of name _log.
	 * 
	 * @return log path
	 */
	public File getLogPath() {
		return logPath;
	}

	/**
	 * Gets the value of the property pluginPath.
	 * 
	 * @return plugin directory as a file
	 */
	public File getPluginPath() {
		return pluginPath;
	}

}
