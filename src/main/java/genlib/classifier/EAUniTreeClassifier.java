package genlib.classifier;

import genlib.GenLib;
import genlib.configurations.Config;
import genlib.locales.TextResource;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import weka.classifiers.Classifier;
import weka.core.Capabilities;
import weka.core.Capabilities.Capability;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.Randomizable;
import weka.core.TechnicalInformation;
import weka.core.TechnicalInformation.Field;
import weka.core.TechnicalInformation.Type;
import weka.core.TechnicalInformationHandler;
import weka.core.Utils;

public class EAUniTreeClassifier extends Classifier implements Randomizable, OptionHandler,
TechnicalInformationHandler {

	/** for serialization */
	private static final long serialVersionUID = 5314273117546487901L;
	/** Evolution classificator not dependant on weka. */
	private EvolutionTreeClassifier e_tree_class;
	private String popInitString = "type=DECISION_STUMP;depth=2";

	/**
	 * Constructor for EvolutionClassifier which initialize EvolutionTreeClasifier class that 
	 * is used as main classificator not dependant on weka. This approach let you use the library 
	 * in weka as well as in different projects.
	 */
	public EAUniTreeClassifier() {
		GenLib.reconfig();
		this.e_tree_class = new EvolutionTreeClassifier();			
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Enumeration listOptions() {

		Vector newVector = new Vector(4);

		newVector.
		addElement(new Option("\tMutation probability.\n" + 
				"\t(default " + e_tree_class.getMutProb() + ")",
				"MP", 0, "-MP <mutation probability"));
		newVector.
		addElement(new Option("\tCrossover probability.\n" +
				"\t(default " + e_tree_class.getXoverProb() + ")",
				"XP", 1, "-XP <xover probability>"));
		newVector.
		addElement(new Option("\tSeed for random data shuffling.\n" +
				"\t(default " + e_tree_class.getSeed() + ")",
				"S", 1, "-S <seed>"));
		newVector.
		addElement(new Option("\tPopulation generator",
				"PopP", 1, "-PopP <pop_params>"));

		newVector.
		addElement(new Option("\tPopulation size",
				"IP", 1, "-IP <pop_size>"));


		newVector.
		addElement(new Option("\tForced Reconfiguration?",
				"C", 1, "-C <True|False>"));


		return newVector.elements();
	}


	/**
	 * Gets the current settings of the evolution classifier. SuppressWarnings are present because 
	 * of usage of Vector, but for the back-compatibility with Weka we are using it anyways.	
	 *    
	 * @return an array of strings suitable for passing to setOptions()
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String[] getOptions() {		
		Vector result;
		String[] options;
		int i;

		result = new Vector();

		// stands for mutation probability
		result.add("-MP");
		result.add("" + getMutProb());

		// stands for crossover probability
		result.add("-XP");
		result.add("" + getXoverProb());

		// stands for initial population size
		result.add("-IP");
		result.add("" + getPopulationSize());

		// stands for population generator parameters
		result.add("-PopP");
		result.add(getPopInit());

		// stands for seed
		result.add("-S");
		result.add("" + getSeed());	    

		// stands for configuration configured :)
		result.add("-C");
		result.add("" + getConfigured());

		options = super.getOptions();
		for (i = 0; i < options.length; i++)
			result.add(options[i]);

		return (String[]) result.toArray(new String[result.size()]);
	}

	@Override
	public void setOptions(String[] options) throws Exception {
		String tmpStr;

		tmpStr = Utils.getOption("MP", options);
		if (tmpStr.length() != 0) {
			e_tree_class.setMutProb(Double.parseDouble(tmpStr));
		}

		tmpStr = Utils.getOption("XP", options);
		if (tmpStr.length() != 0) {
			e_tree_class.setXoverProb(Double.parseDouble(tmpStr));
		}

		tmpStr = Utils.getOption("PopP",options);
		if (tmpStr.length() != 0) {
			this.popInitString = tmpStr;
			final Properties popProp = parseParameters();			
			e_tree_class.setPopInitializator(popProp,true);
		}

		tmpStr = Utils.getOption("IP", options);
		if (tmpStr.length() != 0) {
			setPopulationSize(Integer.parseInt(tmpStr));
		}

		tmpStr = Utils.getOption('S', options);
		if (tmpStr.length() != 0) {
			e_tree_class.setSeed(Integer.parseInt(tmpStr));
		}

		tmpStr = Utils.getOption('C', options);
		if (tmpStr.length() != 0) {
			setConfigured(Boolean.parseBoolean(tmpStr));
		}

		super.setOptions(options);

		Utils.checkForRemainingOptions(options);
	}

	@Override
	public void buildClassifier(Instances data) throws Exception {		
		// can classifier tree handle the data?
		getCapabilities().testWithFail(data);

		// remove instances with missing class
		data = new Instances(data);
		data.deleteWithMissingClass();		

		// parse initial population generator parameter, set and build classifier
		final Properties popProp = parseParameters();	
		e_tree_class.setPopInitializator(popProp,true);
		e_tree_class.buildClassifier(data);
		// that's all for this method
	}

	/**
	 * Method accessed only from this method that parses parameter IP which contains
	 * information about initial population generator. Different informations
	 * are stored into Properties object that is further used inside buildClassifier method.
	 * 
	 * @return Properties file with stored information about initial pop. generator
	 */
	private Properties parseParameters() {
		Properties prop = new Properties();

		String[] parameters = popInitString.split("[=;]");

		for (int i = 0; i < parameters.length; i+=2)
			prop.put(parameters[i], parameters[i+1]);

		return prop;
	}

	/**
	 * Classifies an instance.
	 *
	 * @param instance the instance to classify
	 * @return the classification for the instance
	 * @throws Exception if instance can't be classified successfully
	 */
	public double classifyInstance(Instance instance) throws Exception {		
		return 0d;
	}

	/**
	 * Getter method for the parameter IP. It contains information about
	 * initial population generator.
	 * 
	 * @return String value of parameter IP
	 */
	public String getPopInit() {
		return popInitString;
	}

	/**
	 * Setter method for the parameter IP. It contains information about 
	 * initial population generator
	 * 
	 * @param popInitString value of parameter IP
	 */
	public void setPopInit(String popInitString) {
		this.popInitString = popInitString;
	}

	/**
	 * Return the tip text for this property 
	 * @return tip text for this property suitable for
	 * displaying in the explorer/experimenter gui
	 */
	public String popInitTipText() {
		return TextResource.getString("wPopInitTipText");
	}

	/**
	 * Get the value of Seed.
	 *
	 * @return Value of Seed.
	 */
	@Override
	public int getSeed() {
		return e_tree_class.getSeed();
	}

	/**
	 * Set the value of Seed.
	 *
	 * @param seed Value to assign to Seed.
	 */
	@Override
	public void setSeed(int seed) {
		e_tree_class.setSeed(seed);	
	}

	/**
	 * Return the tip text for this property 
	 * @return tip text for this property suitable for
	 * displaying in the explorer/experimenter gui
	 */
	public String seedTipText() {
		return TextResource.getString("wSeedTipText");
	}

	public double getMutProb() {
		return e_tree_class.getMutProb();
	}

	public void setMutProb(double mutProb) {
		e_tree_class.setMutProb(mutProb);
	}
	
	/**
	 * Return the tip text for this property 
	 * @return tip text for this property suitable for
	 * displaying in the explorer/experimenter gui
	 */
	public String mutProbTipText() {
		return TextResource.getString("wMutProbTipText");
	}

	public double getXoverProb() {
		return e_tree_class.getXoverProb();
	}

	public void setXoverProb(double xoverProb) {
		e_tree_class.setXoverProb(xoverProb);
	}
	
	/**
	 * Return the tip text for this property 
	 * @return tip text for this property suitable for
	 * displaying in the explorer/experimenter gui
	 */
	public String xoverProbTipText() {
		return TextResource.getString("wXoverProbTipText");
	}

	public int getPopulationSize() {
		return e_tree_class.getPopulationSize();
	}

	public void setPopulationSize(int populationSize) {
		e_tree_class.setPopulationSize(populationSize);
	}

	/**
	 * Return the tip text for this property 
	 * @return tip text for this property suitable for
	 * displaying in the explorer/experimenter gui
	 */
	public String populationSizeTipText() {
		return TextResource.getString("wPopSizeTipText");
	}
	
	public void setConfigured(boolean configured) {
		Config.configured = configured;		
		GenLib.reconfig();
	}

	/**
	 * I would call it isConfigured but weka is sometimes strange.
	 * 
	 * @return true/false if the configuration is already configured
	 */
	public boolean getConfigured() {		
		return Config.configured;
	}
	
	/**
	 * Return the tip text for this property 
	 * @return tip text for this property suitable for
	 * displaying in the explorer/experimenter gui
	 */
	public String configuredTipText() {
		return TextResource.getString("wConfigTipText");
	}


	@Override
	public Capabilities getCapabilities() {
		Capabilities result = super.getCapabilities();
		result.disableAll();

		// attributes
		result.enable(Capability.NOMINAL_ATTRIBUTES);
		result.enable(Capability.NUMERIC_ATTRIBUTES);
		result.enable(Capability.MISSING_VALUES);

		// class
		result.enable(Capability.NOMINAL_CLASS);
		result.enable(Capability.MISSING_CLASS_VALUES);

		return result;
	}

	public String globalInfo() {
		return "Class for constructing decision tree with evolution metaheuristic.\n\n"
				+ "For more information see: \n\n"
				+ getTechnicalInformation();				
	}

	@Override
	public TechnicalInformation getTechnicalInformation() {
		TechnicalInformation result;
		result = new TechnicalInformation(Type.ARTICLE);
		result.setValue(Field.AUTHOR, "Lukas Surin");
		result.setValue(Field.YEAR, "2015");
		result.setValue(Field.TITLE, "Evolutionary Trees");		
		return result;
	}



}
