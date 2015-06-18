package genlib.plugins;

import genlib.annotations.EnvSelectAnnot;
import genlib.annotations.FitnessAnnot;
import genlib.annotations.GenAnnot;
import genlib.annotations.MOperatorAnnot;
import genlib.annotations.MateSelectAnnot;
import genlib.annotations.PopInitAnnot;
import genlib.annotations.XOperatorAnnot;
import genlib.classifier.Classifier;
import genlib.classifier.GenLibEvolutionTreeClassifier;
import genlib.classifier.gens.PopGenerator;
import genlib.classifier.popinit.PopulationInitializator;
import genlib.classifier.splitting.InformationGainCriteria;
import genlib.classifier.splitting.SplitCriteria;
import genlib.classifier.weka.WekaEvolutionTreeClassifier;
import genlib.configurations.PathManager;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.individuals.Individual;
import genlib.evolution.operators.Operator;
import genlib.evolution.population.IPopulation;
import genlib.evolution.population.Population;
import genlib.evolution.selectors.Selector;
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
	public static final HashMap<String, Class<? extends Classifier>> classifiers = new HashMap<>();
	/** loaded population initializators */
	public static final HashMap<String, Class<? extends PopulationInitializator<? extends Individual>>> popInits = new HashMap<>();
	/** loaded population initializators */
	public static final HashMap<String, Class<? extends PopGenerator<? extends Individual>>> gens = new HashMap<>();
	/** loaded fitness functions */
	public static final HashMap<String, Class<? extends FitnessFunction<? extends Individual>>> fitFuncs = new HashMap<>();
	/** hashmap with crossover tree operators */
	public static final HashMap<String, Class<? extends Operator<? extends Individual>>> xOper = new HashMap<>();
	/** hashmap with mutation tree operators */
	public static final HashMap<String, Class<? extends Operator<? extends Individual>>> mutOper = new HashMap<>();
	/** loaded selectors */
	public static final HashMap<String, Class<? extends Selector>> selectors = new HashMap<>();
	/** loaded environmental selectors */
	public static final HashMap<String, Class<? extends Selector>> envSelectors = new HashMap<>();
	/** loaded population types */
	@SuppressWarnings("rawtypes")
	public static final HashMap<String, Class<? extends IPopulation>> populationTypes = new HashMap<>();	
	/** loaded split criterias	 */
	public static final HashMap<String, SplitCriteria<?,?>> splitCriterias = new HashMap<>();

	/** logger for this class */
	private static final Logger LOG = Logger.getLogger(PluginManager.class
			.getName());
	private static final String pluginClassTag = "PLUGINCLASS";

	/**
	 * Static method which is used to initialize all kinds of plugins in this
	 * application. </br> Every plugin initialization is done by its specific
	 * method (population initialization plugins via method initPopInitPlugins).
	 * This method is static because we don't want to allow all kind of plugins
	 * to be callable as we could do with object approach and extending this
	 * manager. </br> Plugin initialization is done from reconfig method in
	 * GenLib class because of the precedence of this class (it's usually loaded
	 * as first).
	 */
	public static void initPlugins() {
		initClassifierPlugins();
		initPopInitPlugins();
		initGeneratorPlugins();
		initFitnessPlugins();
		initOperatorPlugins();
		initSelectorPlugins();
		initPopulationPlugins();
		initSplitCritPlugins();
	}

	private static void initClassifierPlugins() {
		LOG.log(Level.INFO,
				String.format(PermMessages._s_classifiersinit,
						Classifier.class.getName()));
		int classLoaded = 0;
		int plugLoaded = 0;

		classifiers.put(WekaEvolutionTreeClassifier.class.getName(),
				WekaEvolutionTreeClassifier.class);
		LOG.log(Level.INFO, String.format(PermMessages._classifierclass_loaded,
				WekaEvolutionTreeClassifier.class.getName(),
				WekaEvolutionTreeClassifier.class.getName()));
		classLoaded++;
		classifiers.put(GenLibEvolutionTreeClassifier.class.getName(),
				GenLibEvolutionTreeClassifier.class);
		LOG.log(Level.INFO, String.format(PermMessages._classifierclass_loaded,
				GenLibEvolutionTreeClassifier.class.getName(),
				GenLibEvolutionTreeClassifier.class.getName()));
		classLoaded++;

		// loading jar plugins inside plugin directory
		PathManager pm = PathManager.getInstance();
		File populationPluginPath = new File(pm.getPluginPath(),
				"splitcriterias");
		if (populationPluginPath.exists() && populationPluginPath.isDirectory()) {

			for (File plugFile : populationPluginPath
					.listFiles(Utils.jarFilter)) {
				try {
					URLClassLoader authorizedLoader = URLClassLoader
							.newInstance(new URL[] { plugFile.toURI().toURL() });
					URL url = authorizedLoader
							.findResource("META-INF/MANIFEST.MF");
					Manifest mf = new Manifest(url.openStream());
					Attributes mfAttributes = mf.getMainAttributes();
					ClassifierPlugin plugin = (ClassifierPlugin) authorizedLoader
							.loadClass(mfAttributes.getValue(pluginClassTag))
							.newInstance();
					plugin.initClassifiers();
					plugLoaded++;
				} catch (ClassCastException cce) {
					LOG.log(Level.WARNING,
							String.format(PermMessages._plug_type_err,
									plugFile.getName()));
				} catch (Exception e) {
					LOG.log(Level.SEVERE, e.toString());
				}
			}
		}

		String sSplitCrit = "classifiers";
		LOG.log(Level.INFO, String.format(PermMessages._c_class_loaded,
				sSplitCrit, classLoaded));
		LOG.log(Level.INFO, String.format(PermMessages._c_plug_loaded,
				sSplitCrit, plugLoaded));
	}

	private static void initSplitCritPlugins() {
		LOG.log(Level.INFO,
				String.format(PermMessages._s_splitinit,
						SplitCriteria.class.getName()));
		int classLoaded = 0;
		int plugLoaded = 0;

		splitCriterias.put(InformationGainCriteria.initName,
				InformationGainCriteria.getInstance());
		LOG.log(Level.INFO, String.format(PermMessages._splitclass_loaded,
				InformationGainCriteria.class.getName(),
				InformationGainCriteria.initName));
		classLoaded++;

		// loading jar plugins inside plugin directory
		PathManager pm = PathManager.getInstance();
		File populationPluginPath = new File(pm.getPluginPath(),
				"splitcriterias");
		if (populationPluginPath.exists() && populationPluginPath.isDirectory()) {

			for (File plugFile : populationPluginPath
					.listFiles(Utils.jarFilter)) {
				try {
					URLClassLoader authorizedLoader = URLClassLoader
							.newInstance(new URL[] { plugFile.toURI().toURL() });
					URL url = authorizedLoader
							.findResource("META-INF/MANIFEST.MF");
					Manifest mf = new Manifest(url.openStream());
					Attributes mfAttributes = mf.getMainAttributes();
					SplitCriteriaPlugin plugin = (SplitCriteriaPlugin) authorizedLoader
							.loadClass(mfAttributes.getValue(pluginClassTag))
							.newInstance();
					plugin.initCriterias();
					plugLoaded++;
				} catch (ClassCastException cce) {
					LOG.log(Level.WARNING,
							String.format(PermMessages._plug_type_err,
									plugFile.getName()));
				} catch (Exception e) {
					LOG.log(Level.SEVERE, e.toString());
				}
			}
		}

		String sSplitCrit = "splitcriterias";
		LOG.log(Level.INFO, String.format(PermMessages._c_class_loaded,
				sSplitCrit, classLoaded));
		LOG.log(Level.INFO, String.format(PermMessages._c_plug_loaded,
				sSplitCrit, plugLoaded));

	}

	private static void initPopulationPlugins() {
		LOG.log(Level.INFO,
				String.format(PermMessages._s_popcontinit,
						IPopulation.class.getName()));
		int classLoaded = 0;
		int plugLoaded = 0;

		PluginManager.populationTypes.put(Population.initName, Population.class);
		LOG.log(Level.INFO, String.format(
				PermMessages._populationcontainer_loaded,
				Population.class.getName(), Population.initName));
		classLoaded++;

		// loading jar plugins inside plugin directory
		PathManager pm = PathManager.getInstance();
		File populationPluginPath = new File(pm.getPluginPath(), "population");

		if (populationPluginPath.exists() && populationPluginPath.isDirectory()) {

			for (File plugFile : populationPluginPath
					.listFiles(Utils.jarFilter)) {
				try {
					URLClassLoader authorizedLoader = URLClassLoader
							.newInstance(new URL[] { plugFile.toURI().toURL() });
					URL url = authorizedLoader
							.findResource("META-INF/MANIFEST.MF");
					Manifest mf = new Manifest(url.openStream());
					Attributes mfAttributes = mf.getMainAttributes();
					PopulationPlugin plugin = (PopulationPlugin) authorizedLoader
							.loadClass(mfAttributes.getValue(pluginClassTag))
							.newInstance();
					plugin.initPopulations();
					plugLoaded++;
				} catch (ClassCastException cce) {
					LOG.log(Level.WARNING,
							String.format(PermMessages._plug_type_err,
									plugFile.getName()));
				} catch (Exception e) {
					LOG.log(Level.SEVERE, e.toString());
				}
			}
		}

		String sPopInit = "population containers";
		LOG.log(Level.INFO, String.format(PermMessages._c_class_loaded,
				sPopInit, classLoaded));
		LOG.log(Level.INFO, String.format(PermMessages._c_plug_loaded,
				sPopInit, plugLoaded));

	}

	private static void initPopInitPlugins() {
		LOG.log(Level.INFO, String.format(PermMessages._s_popinit,
				PopulationInitializator.class.getName()));
		int plugLoaded = 0;
		int classLoaded = 0;

		Package p = Package.getPackage("genlib.classifier.popinit");
		if (p.getAnnotations() == null) {
			return;
		}

		for (Annotation rawAnot : p.getAnnotations()) {
			PopInitAnnot popInitAnot = (PopInitAnnot) rawAnot;

			classLoaded = classLoader(popInitAnot.toInjectClasses(),
					popInitAnot.toInjectClass(), popInitAnot.toInjectNames(),
					popInitAnot.toInjectField(), PermMessages._popclass_loaded);

		}

		// loading jar plugins inside plugin directory
		PathManager pm = PathManager.getInstance();
		File popInitPluginPath = new File(pm.getPluginPath(), "pop");

		if (popInitPluginPath.exists() && popInitPluginPath.isDirectory()) {

			for (File plugFile : popInitPluginPath.listFiles(Utils.jarFilter)) {
				try {
					URLClassLoader authorizedLoader = URLClassLoader
							.newInstance(new URL[] { plugFile.toURI().toURL() });
					URL url = authorizedLoader
							.findResource("META-INF/MANIFEST.MF");
					Manifest mf = new Manifest(url.openStream());
					Attributes mfAttributes = mf.getMainAttributes();
					PopPlugin plugin = (PopPlugin) authorizedLoader.loadClass(
							mfAttributes.getValue(pluginClassTag))
							.newInstance();
					plugin.initPopulators();
					plugLoaded++;
				} catch (ClassCastException cce) {
					LOG.log(Level.WARNING,
							String.format(PermMessages._plug_type_err,
									plugFile.getName()));
				} catch (Exception e) {
					LOG.log(Level.SEVERE, e.toString());
				}
			}
		}

		String sPopInit = "population initializator";
		LOG.log(Level.INFO, String.format(PermMessages._c_class_loaded,
				sPopInit, classLoaded));
		LOG.log(Level.INFO, String.format(PermMessages._c_plug_loaded,
				sPopInit, plugLoaded));

	}

	private static void initFitnessPlugins() {
		LOG.log(Level.INFO,
				String.format(PermMessages._s_fitinit,
						FitnessFunction.class.getName()));
		int plugLoaded = 0;
		int classLoaded = 0;

		Package p = Package.getPackage("genlib.evolution.fitness");
		if (p.getAnnotations() == null) {
			return;
		}

		for (Annotation rawAnot : p.getAnnotations()) {
			FitnessAnnot fitnessAnot = (FitnessAnnot) rawAnot;

			classLoaded = classLoader(fitnessAnot.toInjectClasses(),
					fitnessAnot.toInjectClass(), fitnessAnot.toInjectNames(),
					fitnessAnot.toInjectField(), PermMessages._fitclass_loaded);

		}

		// loading jar plugins inside plugin directory
		PathManager pm = PathManager.getInstance();
		File fitnessPluginPath = new File(pm.getPluginPath(), "fit");

		if (fitnessPluginPath.exists() && fitnessPluginPath.isDirectory()) {

			for (File plugFile : fitnessPluginPath.listFiles(Utils.jarFilter)) {
				try {
					URLClassLoader authorizedLoader = URLClassLoader
							.newInstance(new URL[] { plugFile.toURI().toURL() });
					URL url = authorizedLoader
							.findResource("META-INF/MANIFEST.MF");
					Manifest mf = new Manifest(url.openStream());
					Attributes mfAttributes = mf.getMainAttributes();
					FitnessPlugin plugin = (FitnessPlugin) authorizedLoader
							.loadClass(mfAttributes.getValue(pluginClassTag))
							.newInstance();
					plugin.initFitnesses();
					plugLoaded++;
				} catch (ClassCastException cce) {
					LOG.log(Level.WARNING,
							String.format(PermMessages._plug_type_err,
									plugFile.getName()));
				} catch (Exception e) {
					LOG.log(Level.SEVERE, e.toString());
				}
			}
		}

		String sPopInit = "fitness function";
		LOG.log(Level.INFO, String.format(PermMessages._c_class_loaded,
				sPopInit, classLoaded));
		LOG.log(Level.INFO, String.format(PermMessages._c_plug_loaded,
				sPopInit, plugLoaded));

	}

	private static void initGeneratorPlugins() {
		LOG.log(Level.INFO,
				String.format(PermMessages._s_geninit,
						PopGenerator.class.getName()));
		int plugLoaded = 0;
		int classLoaded = 0;

		Package p = Package.getPackage("genlib.classifier.gens");
		if (p.getAnnotations() == null) {
			return;
		}

		for (Annotation rawAnot : p.getAnnotations()) {
			GenAnnot genAnot = (GenAnnot) rawAnot;

			classLoaded = classLoader(genAnot.toInjectClasses(),
					genAnot.toInjectClass(), genAnot.toInjectNames(),
					genAnot.toInjectField(), PermMessages._genclass_loaded);

		}

		// loading jar plugins inside plugin directory
		PathManager pm = PathManager.getInstance();
		File genPluginPath = new File(pm.getPluginPath(), "gen");

		if (genPluginPath.exists() && genPluginPath.isDirectory()) {

			for (File plugFile : genPluginPath.listFiles(Utils.jarFilter)) {
				try {
					URLClassLoader authorizedLoader = URLClassLoader
							.newInstance(new URL[] { plugFile.toURI().toURL() });
					URL url = authorizedLoader
							.findResource("META-INF/MANIFEST.MF");
					Manifest mf = new Manifest(url.openStream());
					Attributes mfAttributes = mf.getMainAttributes();
					GenPlugin plugin = (GenPlugin) authorizedLoader.loadClass(
							mfAttributes.getValue(pluginClassTag))
							.newInstance();
					plugin.initGenerators();
					plugLoaded++;
				} catch (ClassCastException cce) {
					LOG.log(Level.WARNING,
							String.format(PermMessages._plug_type_err,
									plugFile.getName()));
				} catch (Exception e) {
					LOG.log(Level.SEVERE, e.toString());
				}
			}
		}

		String sPopInit = "individual generator";
		LOG.log(Level.INFO, String.format(PermMessages._c_class_loaded,
				sPopInit, classLoaded));
		LOG.log(Level.INFO, String.format(PermMessages._c_plug_loaded,
				sPopInit, plugLoaded));
	}

	private static void initOperatorPlugins() {
		LOG.log(Level.INFO,
				String.format(PermMessages._s_operinit,
						Operator.class.getName()));
		int plugLoaded = 0;
		int classLoaded = 0;

		Package p = Package.getPackage("genlib.evolution.operators");
		if (p.getAnnotations() == null) {
			return;
		}

		for (Annotation rawAnot : p.getAnnotations()) {
			if (rawAnot instanceof MOperatorAnnot) {
				MOperatorAnnot mOperAnot = (MOperatorAnnot) rawAnot;

				classLoaded += classLoader(mOperAnot.toInjectClasses(),
						mOperAnot.toInjectClass(), mOperAnot.toInjectNames(),
						mOperAnot.toInjectField(),
						PermMessages._operclass_loaded);

			}
			if (rawAnot instanceof XOperatorAnnot) {
				XOperatorAnnot xOperAnot = (XOperatorAnnot) rawAnot;

				classLoaded += classLoader(xOperAnot.toInjectClasses(),
						xOperAnot.toInjectClass(), xOperAnot.toInjectNames(),
						xOperAnot.toInjectField(),
						PermMessages._operclass_loaded);

			}

		}

		// loading jar plugins inside plugin directory
		PathManager pm = PathManager.getInstance();
		File operPluginPath = new File(pm.getPluginPath(), "operators");

		if (operPluginPath.exists() && operPluginPath.isDirectory()) {

			for (File plugFile : operPluginPath.listFiles(Utils.jarFilter)) {
				try {
					URLClassLoader authorizedLoader = URLClassLoader
							.newInstance(new URL[] { plugFile.toURI().toURL() });
					URL url = authorizedLoader
							.findResource("META-INF/MANIFEST.MF");
					Manifest mf = new Manifest(url.openStream());
					Attributes mfAttributes = mf.getMainAttributes();
					OperatorPlugin plugin = (OperatorPlugin) authorizedLoader
							.loadClass(mfAttributes.getValue(pluginClassTag))
							.newInstance();
					plugin.initOperators();
					plugLoaded++;
				} catch (ClassCastException cce) {
					LOG.log(Level.WARNING,
							String.format(PermMessages._plug_type_err,
									plugFile.getName()));
				} catch (Exception e) {
					LOG.log(Level.SEVERE, e.toString());
				}
			}
		}

		String sPopInit = "operator";
		LOG.log(Level.INFO, String.format(PermMessages._c_class_loaded,
				sPopInit, classLoaded));
		LOG.log(Level.INFO, String.format(PermMessages._c_plug_loaded,
				sPopInit, plugLoaded));
	}

	private static void initSelectorPlugins() {
		LOG.log(Level.INFO, String.format(PermMessages._s_selinit,
				Selector.class.getName()));
		int plugLoaded = 0;
		int classLoaded = 0;

		Package p = Package.getPackage("genlib.evolution.selectors");
		if (p.getAnnotations() == null) {
			return;
		}

		for (Annotation rawAnot : p.getAnnotations()) {
			if (rawAnot instanceof EnvSelectAnnot) {
				EnvSelectAnnot selectAnot = (EnvSelectAnnot) rawAnot;

				classLoaded += classLoader(selectAnot.toInjectClasses(),
						selectAnot.toInjectClass(), selectAnot.toInjectNames(),
						selectAnot.toInjectField(),
						PermMessages._selclass_loaded);

			}
			if (rawAnot instanceof MateSelectAnnot) {
				MateSelectAnnot selectAnot = (MateSelectAnnot) rawAnot;

				classLoaded += classLoader(selectAnot.toInjectClasses(),
						selectAnot.toInjectClass(), selectAnot.toInjectNames(),
						selectAnot.toInjectField(),
						PermMessages._selclass_loaded);

			}

		}

		// loading jar plugins inside plugin directory
		PathManager pm = PathManager.getInstance();
		File selPluginPath = new File(pm.getPluginPath(), "selectors");

		if (selPluginPath.exists() && selPluginPath.isDirectory()) {

			for (File plugFile : selPluginPath.listFiles(Utils.jarFilter)) {
				try {
					URLClassLoader authorizedLoader = URLClassLoader
							.newInstance(new URL[] { plugFile.toURI().toURL() });
					URL url = authorizedLoader
							.findResource("META-INF/MANIFEST.MF");
					Manifest mf = new Manifest(url.openStream());
					Attributes mfAttributes = mf.getMainAttributes();
					SelectorPlugin plugin = (SelectorPlugin) authorizedLoader
							.loadClass(mfAttributes.getValue(pluginClassTag))
							.newInstance();
					plugin.initSelectors();
					plugLoaded++;
				} catch (ClassCastException cce) {
					LOG.log(Level.WARNING,
							String.format(PermMessages._plug_type_err,
									plugFile.getName()));
				} catch (Exception e) {
					LOG.log(Level.SEVERE, e.toString());
				}
			}
		}

		String sPopInit = "selector";
		LOG.log(Level.INFO, String.format(PermMessages._c_class_loaded,
				sPopInit, classLoaded));
		LOG.log(Level.INFO, String.format(PermMessages._c_plug_loaded,
				sPopInit, plugLoaded));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static int classLoader(Class[] toInjectClasses,
			Class toInjectClass, String[] toInjectNames, String toInjectField,
			String loadMsg) {

		if (toInjectClasses.length != toInjectNames.length) {
			return 0;
		}

		int classLoaded = 0;
		try {
			Field f = toInjectClass.getField(toInjectField);
			HashMap<String, Class> h = (HashMap<String, Class>) f.get(null);

			for (int i = 0; i < toInjectClasses.length; i++) {
				String key = toInjectNames[i];
				if (h.containsKey(key)) {
					LOG.log(Level.WARNING, String.format(
							PermMessages._class_duplkey, key, h.get(key)
									.getName(), toInjectClasses[i].getName()));
					continue;
				}
				h.put(toInjectNames[i], toInjectClasses[i]);
				classLoaded++;
				LOG.log(Level.INFO, String.format(loadMsg,
						toInjectClasses[i].getName(), toInjectNames[i]));
			}

		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.toString());
		}
		return classLoaded;
	}
}
