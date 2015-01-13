package genlib;

import genlib.classifier.EAUniTreeClassifier;
import genlib.configurations.Config;
import genlib.configurations.PathManager;
import genlib.locales.PermMessages;
import genlib.locales.TextResource;

import java.io.File;
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
 * @author kirrie
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
						new File(PathManager.getInstance().getRootPath(),
								"GenLib.log").getAbsolutePath(), false);
				fh.setLevel(Level.ALL);
				fh.setFormatter(new SimpleFormatter());
				LOG.addHandler(fh);
				LOG.setUseParentHandlers(false);
			} catch (Exception e) {
				LOG.log(Level.SEVERE, e.toString());
			}
		}

		// Reconfig serves as a first initialization because it's in static
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
		if (Config.configured)
			return false;
		// Essential configuration
		Config c = Config.getInstance();
		c.init();
		c.saveProperties();
		// reinit of TextResource if there was change in locales
		TextResource.reinit();
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
				case "-dummy":
					new EAUniTreeClassifier().buildClassifier(null);
					// default argument for not specific behavior
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
		System.out.println("	-h, -?		print this help message");
		System.out.println("	-version	prints application version");
		System.out.println("	-no-logging	logging is disabled for this run");
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
