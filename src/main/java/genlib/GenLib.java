package genlib;

import genlib.classifier.Classifier;
import genlib.configurations.Config;
import genlib.configurations.PathManager;
import genlib.locales.PermMessages;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;
import genlib.plugins.PluginManager;
import genlib.structures.Data;

import java.io.File;
import java.util.Arrays;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * If we use GenLib as a standalone application then this class with main static
 * method is used. It typically parses parameters handed to the main method.
 * Class defines root logger and configures it inside static block that is
 * called after first access to GenLib class. This class has even a
 * reconfiguration method {@link #reconfig()} that can be globally called from
 * other places in this project. Mentioned static block calls reconfig method
 * because GenLib is typically one of the first class that is accessed from
 * project (because of main method).
 * 
 * @author Lukas Surin
 *
 */
public class GenLib {

	/** logger for this class */
	public static final Logger LOG = Logger.getLogger(GenLib.class.getPackage()
			.getName());

	/**
	 * Static block of commands that is ran after first static/non-static access
	 * to this class. There we do initialization of default logger unless we
	 * have already defined one with parameter java.util.logging.config.file.
	 * Logger is set to print to the console and to the file GenTree.log.
	 * Console takes only message with Level higher or equal to Level.SEVERE.
	 * GenTree.log recognize all the messages.
	 */
	static {
		/*
		 * When app runs with property -Djava.util.logging.config.file then we
		 * skip all of if-condition and set the logger configuration from the
		 * file. Otherwise we set the logger to default config.
		 */
		if (System.getProperty("java.util.logging.config.file") == null) {
			try {
				ConsoleHandler ch = new ConsoleHandler();
				// it's habit to not print to the console every msg only those
				// that are important (Level.SEVERE)
				ch.setLevel(Level.SEVERE);
				ch.setFormatter(new SimpleFormatter());
				LOG.addHandler(ch);
				// Log file located in root directory
				FileHandler fh = new FileHandler(
						new File(PathManager.getInstance().getLogPath(),
								"GenLib.log").getAbsolutePath(), false);
				fh.setLevel(Level.ALL);
				fh.setFormatter(new SimpleFormatter());
				LOG.addHandler(fh);
				LOG.setUseParentHandlers(false);
			} catch (Exception e) {
				LOG.log(Level.SEVERE, e.toString());
			}
		}

		// Reconfig serves as a first initialization because it's inside static
		// block
		reconfig();
	}

	/**
	 * Reconfiguration of basic config class with all the information in it.
	 * Unexpected behavior could lead to erased or changed configurations. This
	 * method serves as a reconfiguration of this potentially lost configs and
	 * it generates new ones. If it's already configured then we will skip it.
	 * This method is called by a variety of other classes and method that wants
	 * to utilize localizations, additional logging, etc... Usually it's called
	 * only once but this behavior can be altered by changing Config.configured
	 * to false (weka end is using this altered behavior)
	 * 
	 * @return true - reconfiguration initialized, false - already configured
	 */
	public static boolean reconfig() {
		// Essential configuration
		if (Config.configured)
			return false;
		Config c = Config.getInstance();
		if (!Config.configured) {
			c.init();
		}
		c.saveProperties();
		// reinit of TextResource if there was change in locales
		TextResource.reinit();
		PluginManager.initPlugins();
		return true;
	}

	/**
	 * Main method of this application which parses arguments and run adequate
	 * functions.
	 * 
	 * @param args
	 *            Arguments that application is run with
	 */
	public static void main(String[] args) {
		LOG.info(PermMessages._arg_pars);
		// COMMAND LINE
		int _counter = 0;
		String trainString = "";
		String testString = "";

		try {
			while (_counter < args.length) {
				switch (args[_counter]) {
				// version argument
				case "-version":
					System.out.printf(TextResource.getString("_arg_version"),
							new Object[] { "GenTree", "1.0" });
					_counter = args.length;
					break;
				case "-?":
				case "-h":
					printHelp();
					_counter = args.length;
					break;
				case "-no-logging":
					for (Handler h : LOG.getHandlers()) {
						h.close();
						LOG.removeHandler(h);
					}
					File logFile = new File(PathManager.getInstance()
							.getRootPath(), "GenTree.log");
					logFile.deleteOnExit();
					_counter++;
					break;	
				case "-tr":
					_counter++;
					trainString = args[_counter];
					if (trainString == null || trainString.isEmpty()) {
						printHelp();
						exit();
					}
					_counter++;
					break;
				case "-ts":
					_counter++;
					testString = args[_counter];
					if (testString == null || testString.isEmpty()) {
						printHelp();
						exit();
					}
					_counter++;
					break;	
				case "-c":
					_counter++;
					String className = args[_counter];
					Class<? extends Classifier> classifierClass = PluginManager.classifiers
							.get(className);

					if (classifierClass == null) {
						LOG.log(Level.SEVERE, String.format(
								TextResource.getString(TextKeys.eNotDefClass),
								className));
						break;
					}					
						
					// parameters are not mandatory and can be blank,
					// default config is from config file
					String[] restOfParams = Arrays.copyOfRange(args, _counter,
							args.length);
					Classifier classifier = classifierClass.newInstance();
					classifier.setOptions(restOfParams);
					// loading train dataset
					Data trainData = classifier.makeDataFromFile(trainString);
					// create classifier from dataset
					classifier.buildClassifier(trainData);
					// print builded classifier
					System.out.println(classifier.toString());
					// print train data classification
					System.out.println(classifier.classifyData(trainData));
					if (!testString.isEmpty()) {
						// loading test dataset
						Data testData = classifier.makeDataFromFile(testString);
						// print test data classification
						System.out.println(classifier.classifyData(testData));
					}
					
					_counter++;
					break;
				default:
					System.out.printf(TextResource.getString("_arg_ndparam"),
							new Object[] { args[_counter] });
					LOG.warning(String.format(PermMessages._arg_ndef,
							new Object[] { args[_counter] }));
					_counter = args.length;
					exit();
				}

			}
		} catch (Exception e) {
			LOG.severe(e.toString());
		}
		exit();
	}

	/**
	 * Exit of the application with the writing into log. It should be noted
	 * that methods calling this exit() method have to clean after themselves
	 * because of system exiting .
	 */
	public static void exit() {
		LOG.info(PermMessages._app_ending);
		// things that should be done before exiting (saving, closing, ...)
		Config.getInstance().saveProperties();
		LOG.info(PermMessages._app_ended);
		System.exit(0);
	}

	/**
	 * Printing help about application into command line. It contains info about
	 * options it can be run with.
	 */
	private static void printHelp() {
		System.out.println("Usage: GenLib [-options]");
		System.out.println("where options include :");
		System.out.println("	-h, -?			print this help message");
		System.out.println("	-version		prints application version");
		System.out.println("	-no-logging		logging is disabled for this run");
		System.out.println("	-tr				file with train data to be classified");
		System.out.println("	-ts				file with test data to be classified");
		System.out.println("	-c	class-name params	classify with classificator of name class-name and params");
	}

	/**
	 * Current version of this application
	 * 
	 * @return String value of version
	 */
	public static String getApplicationVersion() {
		return "GenLib version 1.0";
	}

}
