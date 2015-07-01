package genlib.configurations;

import genlib.locales.PermMessages;
import genlib.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * 
 * 
 * 
 * @author Lukas Surin
 */
public class Config implements Serializable {
	/** for serialization */
	private static final long serialVersionUID = -3103934146617400619L;
	/** Logger for Config file which configurations falls under root logger */
	private static final Logger LOG = Logger.getLogger(Config.class.getName());

	/** Key in property file with loggable switch */
	public static final String DEBUG = "debug";
	/**
	 * Key in property file with saved locale
	 */
	public static final String LOCALE = "locale";
	/**
	 * Key in property file with saved file localization
	 */
	public static final String FILE_LOCALISE = "file-localization";
	/**
	 * Key in property file with saved mutation operators
	 */
	public static final String MUT_OPERATORS = "mut-operators";
	/**
	 * Key in property file with saved crossover operators
	 */
	public static final String XOVER_OPERATORS = "xover-operators";
	/**
	 * Key in property file with saved fit functions
	 */
	public static final String FIT_FUNCTIONS = "fit-functions";
	/**
	 * Key in property file with saved fit comparator
	 */
	public static final String FIT_COMPARATOR = "fit-comparator";
	/**
	 * Key in property file with saved selectors
	 */
	public static final String SELECTORS = "selectors";
	/**
	 * Key in property file with saved environment selectors
	 */
	public static final String ENV_SELECTORS = "env-selectors";
	/**
	 * Key in property file with saved elitism rate
	 */
	public static final String ELITISM = "elitism";
	/**
	 * Key in property file with saved population size
	 */
	public static final String POP_SIZE = "pop-size";
	/**
	 * Key in property file with saved population initializator
	 */
	public static final String POP_INIT = "pop-init";
	/**
	 * Key in property file with saved individual generator
	 */
	public static final String IND_GEN = "ind-generator";
	/**
	 * Key in property file with saved fitness threads
	 */
	public static final String FIT_THREADS = "fit-threads";
	/**
	 * Key in property file with saved individual generator threads
	 */
	public static final String GEN_THREADS = "gen-threads";
	/**
	 * Key in property file with saved operator threads
	 */
	public static final String OPER_THREADS = "op-threads";
	/**
	 * Key in property file with saved number of generations
	 */
	public static final String NUM_OF_GEN = "number-of-generations";
	/** Key in property file with population type to be used */
	public static final String POP_TYPE = "population-type";
	/**
	 * Key in property file with saved seed
	 */
	public static final String SEED = "seed";
	/** Key in property file with data splitting */
	public static final String DATA = "data";
	/** Key in property file with data splitting */
	public static final String CLASSIFY = "classify";

	/**
	 * Singleton instance of this Config to guarantee uniqueness of config.
	 */
	private static final Config instance = new Config().init();
	/**
	 * Boolean value with the state of this Config. </br> True - already
	 * configured, all the properties are loaded and ready to use. </br> False -
	 * must configure/load properties from file.
	 */
	public static boolean configured = false;

	// Private transient fields, because we do not want them to be serialized
	/**
	 * Path to config.properties file.
	 */
	private String sFile;
	/**
	 * Properties field with loaded configurations
	 */
	private Properties prop;

	/**
	 * Factory method of this class which returns unique instance of Config
	 * 
	 * @return Config instance
	 */
	public static Config getInstance() {
		return instance;
	}

	/**
	 * Read resolve method which is called in time of deserialization. Without
	 * this method, the other Config instance will be created and uniqueness is
	 * broken.
	 * 
	 * @return Object/Config instance that is deserialized
	 */
	private Object readResolve() {
		return instance;
	}

	private Config() {
	}

	/**
	 * Method that resets all the saved configs/properties and sets default
	 * values.
	 */
	public void reset() {
		prop.clear();
		setAbsentProperties();
	}

	/**
	 * Method configures this Config instance only iff it's not already
	 * configured. It takes path to config.properties from PathManager and tries
	 * to load properties into Properties instance. After this set the switch to
	 * true to signal that now it is configured.
	 */
	public Config init() {
		if (configured)
			return this;
		sFile = PathManager.getInstance().getRootPath() + File.separator
				+ "config.properties";
		loadProperties();
		configured = true;
		Utils.DEBUG = Boolean.valueOf(prop.getProperty(DEBUG));
		return this;
	}

	private void loadProperties() {
		prop = new Properties();
		try {
			InputStream istream = new FileInputStream(sFile);
			// Nacitanie noveho property objektu z ktoreho bude aplikacia citat.
			prop.load(istream);
			istream.close();
			LOG.info(PermMessages._cfg_loaded);
		} catch (FileNotFoundException fnfe) {
			LOG.info(PermMessages._cfg_file_miss);
		} catch (IOException | SecurityException ex) {
			LOG.severe(String.format(PermMessages._cfg_err_load, ex.toString()));
		}

		setAbsentProperties();
		LOG.info(PermMessages._cfg_set);

	}

	public void saveProperties() {
		try {
			OutputStream ostream = new FileOutputStream(sFile);
			// Saving properties into output stream with current time
			// (timestamp)
			prop.store(ostream, getCurrentTime());
			ostream.close();
			LOG.info(PermMessages._cfg_saved);
		} catch (Exception e) {
			LOG.severe(String.format(PermMessages._cfg_err_save, e.toString()));
		}
	}

	public void setAbsentProperties() {
		putIfAbsent(CLASSIFY, "1");
		putIfAbsent(DEBUG, "false");
		putIfAbsent(FIT_THREADS, "1");
		putIfAbsent(GEN_THREADS, "1");
		putIfAbsent(OPER_THREADS, "1");
		putIfAbsent(FIT_COMPARATOR, "SINGLE 0");
		putIfAbsent(FIT_FUNCTIONS, "tAcc x");
		putIfAbsent(MUT_OPERATORS, "dtM PROB,0.04");
		putIfAbsent(XOVER_OPERATORS, "dtX PROB,0.8");
		putIfAbsent(ELITISM, "0.15");
		putIfAbsent(NUM_OF_GEN, "1");
		putIfAbsent(SELECTORS, "Tmt x");
		putIfAbsent(ENV_SELECTORS, "Tmt x");
		putIfAbsent(POP_INIT, "wCompTree MAXHEIGHT,2");
		putIfAbsent(IND_GEN, "wJ48Gen -C,0.25,-M,2");
		putIfAbsent(POP_SIZE, "100");
		putIfAbsent(SEED, "28041991");
		putIfAbsent(POP_TYPE, "typical x");
		putIfAbsent(FILE_LOCALISE, "false");
		putIfAbsent(DATA, "x");
		putIfAbsent(LOCALE, "en");
	}

	public boolean changeProperty(String key, String value) {
		if (prop.containsKey(key)) {
			prop.setProperty(key, value);
			return true;
		}
		return false;
	}

	private void putIfAbsent(String key, String value) {
		if (!prop.containsKey(key)) {
			prop.put(key, value);
		}
	}

	//
	/*
	 * GETTERS
	 */
	//

	/**
	 * Method returns actual current time (not from config file). It's used
	 * mainly from inside this class to save timestamp of lastly edited config
	 * file.
	 * 
	 * @return actual current time.
	 */
	public String getCurrentTime() {
		return DateFormat.getInstance()
				.format(Calendar.getInstance().getTime());
	}

	/**
	 * Getter returns Locale of texts (from config file) used inside app.
	 * 
	 * @return Locale
	 */
	public Locale getLocale() {
		return new Locale(prop.getProperty(LOCALE));
	}

	/**
	 * Getter returns true iff debug mode should be activated.
	 * 
	 * @return true iff active debugging
	 */
	public boolean isDebug() {
		return Boolean.valueOf(prop.getProperty(DEBUG));
	}

	/**
	 * Getter which returns if locale files are out of app jar. It's pulled out
	 * from config file.
	 * 
	 * @return true/false whether locale files are out of app jar.
	 */
	public boolean isFileLocalized() {
		if (prop.containsKey(FILE_LOCALISE)
				&& Boolean.valueOf(prop.getProperty(FILE_LOCALISE))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Getter which returns MutationOperator string from config file.
	 * 
	 * @return Parameter with mutation operators
	 */
	public String getMutationOperators() {
		return prop.getProperty(MUT_OPERATORS);
	}

	/**
	 * Getter which returns CrossoverOperator string from config file
	 * 
	 * @return Parameter with crossover operators
	 */
	public String getXoverOperators() {
		return prop.getProperty(XOVER_OPERATORS);
	}

	/**
	 * Getter which returns Elitism value from config file
	 * 
	 * @return Elitism rate parameter
	 */
	public double getElitismRate() {
		return Double.parseDouble(prop.getProperty(ELITISM));
	}

	/**
	 * Getter which returns Population initializator string from config file
	 * 
	 * @return Population initializator parameter
	 */
	public String getPopulationInit() {
		return prop.getProperty(POP_INIT);
	}

	/**
	 * Getter which returns Individual generator string from config file
	 * 
	 * @return Individual generator parameter
	 */
	public String getIndGenerators() {
		return prop.getProperty(IND_GEN);
	}

	/**
	 * Getter which returns Population size value from config file
	 * 
	 * @return Population size parameter
	 */
	public int getPopulationSize() {
		return Integer.parseInt(prop.getProperty(POP_SIZE));
	}

	/**
	 * Getter which returns population value from config file. It can be even
	 * plugin name.
	 * 
	 * @return population type
	 */
	public String getPopulationType() {
		return prop.getProperty(POP_TYPE);
	}

	/**
	 * Getter which returns Number of generations value from config file
	 * 
	 * @return Number of generations parameter
	 */
	public int getNumberOfGenerations() {
		return Integer.parseInt(prop.getProperty(NUM_OF_GEN));
	}

	/**
	 * Getter which returns Number of threads (from config file) that generates
	 * initial population.
	 * 
	 * @return Generator threads parameter
	 */
	public int getGenNumOfThreads() {
		return Integer.parseInt(prop.getProperty(GEN_THREADS));
	}

	/**
	 * Getter which returns Number of threads (from config file) that computes
	 * fitness functions
	 * 
	 * @return Fitness compute threads parameter
	 */
	public int getFitNumOfThreads() {
		return Integer.parseInt(prop.getProperty(FIT_THREADS));
	}

	/**
	 * Getter which returns Number of threads (from config file) that execute
	 * operators.
	 * 
	 * @return Operator threads parameter
	 */
	public int getOperNumOfThreads() {
		return Integer.parseInt(prop.getProperty(OPER_THREADS));
	}

	/**
	 * Getter which returns Selectors string from config file
	 * 
	 * @return Selector parameter
	 */
	public String getSelectors() {
		return prop.getProperty(SELECTORS);
	}

	/**
	 * Getter which returns Environmental Selectors string from config file
	 * 
	 * @return Environmental Selector parameter
	 */
	public String getEnvSelectors() {
		return prop.getProperty(ENV_SELECTORS);
	}

	/**
	 * Getter which returns Seed value from config file
	 * 
	 * @return Seed parameter
	 */
	public int getSeed() {
		return Integer.parseInt(prop.getProperty(SEED));
	}

	/**
	 * Getter which returns Fitness comparator/evaluator string from config file
	 * 
	 * @return Fitness comparator parameter
	 */
	public String getFitnessComparator() {
		return prop.getProperty(FIT_COMPARATOR);
	}

	/**
	 * Getter which returns Fitness functions string from config file
	 * 
	 * @return Fitness functions parameter
	 */
	public String getFitnessFunctions() {
		return prop.getProperty(FIT_FUNCTIONS);
	}

	/**
	 * Getter which returns data string from config file
	 * 
	 * @return data parameter
	 */
	public String getData() {
		return prop.getProperty(DATA);
	}
	
	/**
	 * Getter which returns classify integer from config file
	 * 
	 * @return classify parameter
	 */
	public int getClassify() {
		return Integer.parseInt(prop.getProperty(CLASSIFY));
	}
	
	//
	/*
	 * SETTERS
	 */
	//

	public void setMutationOperators(String mutOperString) {
		prop.setProperty(MUT_OPERATORS, mutOperString);
	}

	public void setXoverOperators(String xoverOperString) {
		prop.setProperty(XOVER_OPERATORS, xoverOperString);
	}

	public void setElitismRate(double elitism) {
		prop.setProperty(ELITISM, String.valueOf(elitism));
	}

	public void setPopulationInit(String popInitString) {
		prop.setProperty(POP_INIT, popInitString);
	}

	public void setIndGenerators(String indGenString) {
		prop.setProperty(IND_GEN, indGenString);
	}

	public void setNumberOfGenerations(int numberOfGen) {
		prop.setProperty(NUM_OF_GEN, String.valueOf(numberOfGen));
	}

	public void setPopulationSize(int popSize) {
		prop.setProperty(POP_SIZE, String.valueOf(popSize));
	}

	public void setSeed(int seed) {
		prop.setProperty(SEED, String.valueOf(seed));
	}

	public void setFitnessComparator(String fitCompString) {
		prop.setProperty(FIT_COMPARATOR, fitCompString);
	}

	public void setFitnessFunctions(String fitFuncsString) {
		prop.setProperty(FIT_FUNCTIONS, fitFuncsString);
	}

	public void setSelectors(String selectorString) {
		prop.setProperty(SELECTORS, selectorString);
	}

	public void setEnvSelectors(String envSelectorString) {
		prop.setProperty(ENV_SELECTORS, envSelectorString);
	}

	public void setFitnessThreads(int fitThreads) {
		prop.setProperty(FIT_THREADS, String.valueOf(fitThreads));
	}

	public void setGeneratorThreads(int genThreads) {
		prop.setProperty(GEN_THREADS, String.valueOf(genThreads));
	}

	public void setOperatorThreads(int operThreads) {
		prop.setProperty(OPER_THREADS, String.valueOf(operThreads));
	}

	public void setData(String dataString) {
		prop.setProperty(DATA, dataString);
	}
	
	public void setClassify(String classify) {
		prop.setProperty(CLASSIFY, classify);
	}
	
	public void setLocale(Locale locale) {
		prop.setProperty(LOCALE, locale.toString());
	}
}
