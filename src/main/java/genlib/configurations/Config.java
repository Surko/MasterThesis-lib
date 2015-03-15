package genlib.configurations;
import genlib.locales.PermMessages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Logger;

public class Config {		
	
	public static boolean configured = false;
	private static final Logger LOG = Logger.getLogger(Config.class.getName());
		
	private static Config instance;
	private String sFile;
	private Properties prop;	
		
	public static synchronized Config getInstance() {		
		if (instance == null) {
			instance = new Config();
		}
		return instance;		
	}
	
	public void init() {
		sFile = PathManager.getInstance().getRootPath() + File.separator + "config.properties";	
		loadProperties();
		configured = true;
	}
	
	public void loadProperties() {
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
			// Ulozenie properties do vystupneho streamu.
			prop.store(ostream, getCurrentTime());
			ostream.close();
			LOG.info(PermMessages._cfg_saved);
		} catch (Exception e) {
			LOG.severe(String.format(PermMessages._cfg_err_save ,e.toString()));
		}
	}
	
	public void setAbsentProperties() {
		putIfAbsent("fit-threads", "1");
		putIfAbsent("gen-threads", "1");
		putIfAbsent("oper-threads", "1");
		putIfAbsent("fit-eval", "SINGLE");
		putIfAbsent("fit-functions", "tAcc");
		putIfAbsent("mut-operators", "DEFAULT 0.04");
		putIfAbsent("xover-prob", "DEFAULT 0.8");
		putIfAbsent("elitism", "0.15");
		putIfAbsent("selectors", "Tmt 0");
		putIfAbsent("env-selectors", "");
		putIfAbsent("population-init","type DECISION_STUMP;depth 2");
		putIfAbsent("pop-size", "100");
		putIfAbsent("seed", "28041991");
		putIfAbsent("file-localization", "false");
		putIfAbsent("locale", "en");						
		//TODO
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
	
	public String getCurrentTime() {
		return DateFormat.getInstance().format(Calendar.getInstance().getTime());
	}
			
	public Locale getLocale() {
		return new Locale(prop.getProperty("locale"));
	}
	
	public boolean isFileLocalized() {
		return prop.getProperty("file-localization").equals("true");
	}
	
	public String getMutationOperators() {
		return prop.getProperty("mut-operators");
	}
	
	public String getXoverOperators() {
		return prop.getProperty("xover-operators");
	}
	
	public double getElitismRate() {
		return Double.parseDouble(prop.getProperty("elitism"));
	}
	
	public String getPopulationInit() {
		return prop.getProperty("population-init");
	}
	
	public int getPopulationSize() {
		return Integer.parseInt(prop.getProperty("pop-size"));
	}
	
	public int getGenNumOfThreads() {
		return Integer.parseInt(prop.getProperty("gen-threads"));
	}
	
	public int getFitNumOfThreads() {
		return Integer.parseInt(prop.getProperty("fit-threads"));
	}
	
	public int getOperNumOfThreads() {
		return Integer.parseInt(prop.getProperty("oper-threads"));
	}
	
	public String getSelectors() {
		return prop.getProperty("selectors");
	}
	
	public String getEnvSelectors() {
		return prop.getProperty("env-selectors");
	}
	
	public int getSeed() {
		return Integer.parseInt(prop.getProperty("seed"));
	}

	
	public String getFitnessComparator() {
		return prop.getProperty("fit-eval");
	}
	
	public String getFitnessFunctions() {
		return prop.getProperty("fit-functions");
	}
}
