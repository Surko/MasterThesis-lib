package genlib.plugins;

import genlib.annotations.FitnessAnnot;
import genlib.annotations.GenAnnot;
import genlib.annotations.PopInitAnnot;
import genlib.classifier.gens.PopGenerator;
import genlib.classifier.popinit.PopulationInitializator;
import genlib.configurations.PathManager;
import genlib.evolution.fitness.FitnessFunction;
import genlib.locales.PermMessages;
import genlib.utils.Utils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginManager {
	/** logger for this class */
	private static final Logger LOG = Logger.getLogger(PluginManager.class.getName());
	private static final String pluginClassTag = "PLUGINCLASS";
	
	/**
	 * Static method which is used to initialize all kinds of plugins in this application. </br>
	 * Every plugin initialization is done by its specific method (population initialization plugins 
	 * via method initPopInitPlugins).
	 * This method is static because we don't want to allow all kind of plugins to be callable as we could
	 * do with object approach and extending this manager. </br>
	 * Plugin initialization is done from reconfig method in GenLib class because of the 
	 * precedence of this class (it's usually loaded as first). 
	 */
	public static void initPlugins() {	
		initPopInitPlugins();
		initGeneratorPlugins();
		initFitnessPlugins();
	}
	
	private static void initPopInitPlugins() {
		LOG.log(Level.INFO, String.format(PermMessages._s_popinit,PopulationInitializator.class.getName()));
		int plugLoaded = 0;
		int classLoaded = 0;
						
		Package p = Package.getPackage("genlib.classifier.popinit");		
		if (p.getAnnotations() == null) {
			return;
		}
		
		for (Annotation rawAnot : p.getAnnotations()) {			
			PopInitAnnot popInitAnot = (PopInitAnnot)rawAnot;
			
			if (popInitAnot.toInjectClasses().length == popInitAnot.toInjectNames().length) {
				try {
					Field f = popInitAnot.toInjectClass().getField(popInitAnot.toInjectField());					
					// it's static map, so null object as parameter is adequate				
					HashMap<String,Class<? extends PopulationInitializator<?>>> h = (HashMap<String, Class<? extends PopulationInitializator<?>>>) f.get(null);
					for (int i = 0; i < popInitAnot.toInjectClasses().length; i++) {
						String key = popInitAnot.toInjectNames()[i];
						if (h.containsKey(key)) {
							LOG.log(Level.WARNING, String.format(PermMessages._class_duplkey,
									key,
									h.get(key).getName(),
									popInitAnot.toInjectClasses()[i].getName()));
							continue;
						}
						h.put(popInitAnot.toInjectNames()[i], popInitAnot.toInjectClasses()[i]);
						classLoaded++;
						LOG.log(Level.INFO,String.format(PermMessages._popclass_loaded,
								popInitAnot.toInjectClasses()[i].getName(),
								popInitAnot.toInjectNames()[i]));
					}									
				} catch (Exception e) {
					LOG.log(Level.SEVERE, e.toString());
				}
			}
			
		}

		// loading jar plugins inside plugin directory
		PathManager pm = PathManager.getInstance();		
		File popInitPluginPath = new File(pm.getPluginPath(), "pop");
		
		if (popInitPluginPath.exists() && popInitPluginPath.isDirectory()) {			
				
			for (File plugFile : popInitPluginPath.listFiles(Utils.jarFilter)) {
				try {
					URLClassLoader authorizedLoader = URLClassLoader.newInstance(new URL[] { plugFile.toURI().toURL() });
		            URL url = authorizedLoader.findResource("META-INF/MANIFEST.MF"); 
		            Manifest mf = new Manifest(url.openStream());
		            Attributes mfAttributes = mf.getMainAttributes();
		            PopPlugin plugin = (PopPlugin) authorizedLoader.loadClass(
	                        mfAttributes.getValue(pluginClassTag)).newInstance();
		            plugin.initPopulators();
					plugLoaded++;
				} catch (ClassCastException cce) {
					LOG.log(Level.WARNING, String.format(PermMessages._plug_type_err,plugFile.getName()));
				} catch (Exception e) {
					LOG.log(Level.SEVERE, e.toString());
				}
			}
		}
		
		String sPopInit = "population initializator";
		LOG.log(Level.INFO, String.format(PermMessages._c_class_loaded, sPopInit, classLoaded));
		LOG.log(Level.INFO, String.format(PermMessages._c_plug_loaded, sPopInit, plugLoaded));
		
	}
	
	private static void initFitnessPlugins() {
		LOG.log(Level.INFO, String.format(PermMessages._s_fitinit,FitnessFunction.class.getName()));
		int plugLoaded = 0;
		int classLoaded = 0;
						
		Package p = Package.getPackage("genlib.evolution.fitness");		
		if (p.getAnnotations() == null) {
			return;
		}
		
		for (Annotation rawAnot : p.getAnnotations()) {			
			FitnessAnnot fitnessAnot = (FitnessAnnot)rawAnot;
			
			if (fitnessAnot.toInjectClasses().length == fitnessAnot.toInjectNames().length) {
				try {
					Field f = fitnessAnot.toInjectClass().getField(fitnessAnot.toInjectField());					
					// it's static map, so null object as parameter is adequate				
					HashMap<String,Class<? extends FitnessFunction<?>>> h = (HashMap<String, Class<? extends FitnessFunction<?>>>) f.get(null);
					for (int i = 0; i < fitnessAnot.toInjectClasses().length; i++) {
						String key = fitnessAnot.toInjectNames()[i];
						if (h.containsKey(key)) {
							LOG.log(Level.WARNING, String.format(PermMessages._class_duplkey,
									key,
									h.get(key).getName(),
									fitnessAnot.toInjectClasses()[i].getName()));
							continue;
						}
						h.put(fitnessAnot.toInjectNames()[i], fitnessAnot.toInjectClasses()[i]);
						classLoaded++;
						LOG.log(Level.INFO,String.format(PermMessages._fitclass_loaded,
								fitnessAnot.toInjectClasses()[i].getName(),
								fitnessAnot.toInjectNames()[i]));
					}									
				} catch (Exception e) {
					LOG.log(Level.SEVERE, e.toString());
				}
			}
			
		}

		// loading jar plugins inside plugin directory
		PathManager pm = PathManager.getInstance();		
		File fitnessPluginPath = new File(pm.getPluginPath(), "fit");
		
		if (fitnessPluginPath.exists() && fitnessPluginPath.isDirectory()) {			
				
			for (File plugFile : fitnessPluginPath.listFiles(Utils.jarFilter)) {
				try {
					URLClassLoader authorizedLoader = URLClassLoader.newInstance(new URL[] { plugFile.toURI().toURL() });
		            URL url = authorizedLoader.findResource("META-INF/MANIFEST.MF"); 
		            Manifest mf = new Manifest(url.openStream());
		            Attributes mfAttributes = mf.getMainAttributes();
		            FitnessPlugin plugin = (FitnessPlugin) authorizedLoader.loadClass(
	                        mfAttributes.getValue(pluginClassTag)).newInstance();
		            plugin.initFitnesses();
					plugLoaded++;
				} catch (ClassCastException cce) {
					LOG.log(Level.WARNING, String.format(PermMessages._plug_type_err,plugFile.getName()));
				} catch (Exception e) {
					LOG.log(Level.SEVERE, e.toString());
				}
			}
		}
		
		String sPopInit = "fitness function";
		LOG.log(Level.INFO, String.format(PermMessages._c_class_loaded, sPopInit, classLoaded));
		LOG.log(Level.INFO, String.format(PermMessages._c_plug_loaded, sPopInit, plugLoaded));
		
	}
	
	private static void initGeneratorPlugins() {
		LOG.log(Level.INFO, String.format(PermMessages._s_geninit,PopGenerator.class.getName()));
		int plugLoaded = 0;
		int classLoaded = 0;
						
		Package p = Package.getPackage("genlib.classifier.gens");		
		if (p.getAnnotations() == null) {
			return;
		}
		
		for (Annotation rawAnot : p.getAnnotations()) {			
			GenAnnot genAnot = (GenAnnot)rawAnot;
			
			if (genAnot.toInjectClasses().length == genAnot.toInjectNames().length) {
				try {
					Field f = genAnot.toInjectClass().getField(genAnot.toInjectField());					
					// it's static map, so null object as parameter is adequate				
					HashMap<String,Class<? extends PopGenerator>> h = (HashMap<String, Class<? extends PopGenerator>>) f.get(null);
					for (int i = 0; i < genAnot.toInjectClasses().length; i++) {
						String key = genAnot.toInjectNames()[i];
						if (h.containsKey(key)) {
							LOG.log(Level.WARNING, String.format(PermMessages._class_duplkey,
									key,
									h.get(key).getName(),
									genAnot.toInjectClasses()[i].getName()));
							continue;
						}
						h.put(genAnot.toInjectNames()[i], genAnot.toInjectClasses()[i]);
						classLoaded++;
						LOG.log(Level.INFO,String.format(PermMessages._genclass_loaded,
								genAnot.toInjectClasses()[i].getName(),
								genAnot.toInjectNames()[i]));
					}									
				} catch (Exception e) {
					LOG.log(Level.SEVERE, e.toString());
				}
			}
			
		}

		// loading jar plugins inside plugin directory
		PathManager pm = PathManager.getInstance();		
		File genPluginPath = new File(pm.getPluginPath(), "gen");
		
		if (genPluginPath.exists() && genPluginPath.isDirectory()) {			
				
			for (File plugFile : genPluginPath.listFiles(Utils.jarFilter)) {
				try {
					URLClassLoader authorizedLoader = URLClassLoader.newInstance(new URL[] { plugFile.toURI().toURL() });
		            URL url = authorizedLoader.findResource("META-INF/MANIFEST.MF"); 
		            Manifest mf = new Manifest(url.openStream());
		            Attributes mfAttributes = mf.getMainAttributes();
		            GenPlugin plugin = (GenPlugin) authorizedLoader.loadClass(
	                        mfAttributes.getValue(pluginClassTag)).newInstance();
		            plugin.initGenerators();
					plugLoaded++;
				} catch (ClassCastException cce) {
					LOG.log(Level.WARNING, String.format(PermMessages._plug_type_err,plugFile.getName()));
				} catch (Exception e) {
					LOG.log(Level.SEVERE, e.toString());
				}
			}
		}
		
		String sPopInit = "individual generator";
		LOG.log(Level.INFO, String.format(PermMessages._c_class_loaded, sPopInit, classLoaded));
		LOG.log(Level.INFO, String.format(PermMessages._c_plug_loaded, sPopInit, plugLoaded));		
	}
	
}
