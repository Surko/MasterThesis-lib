package genlib.classifier.weka;

import genlib.GenLib;
import genlib.classifier.classifierextensions.WekaClassifierExtension;
import genlib.classifier.common.EvolutionTreeClassifier;
import genlib.configurations.Config;
import genlib.evolution.individuals.TreeIndividual;
import genlib.exceptions.ConfigInternalException;
import genlib.exceptions.EmptyConfigParamException;
import genlib.exceptions.TypeParameterException;
import genlib.exceptions.WrongDataException;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;
import genlib.structures.Data;
import genlib.structures.data.GenLibInstance;
import genlib.structures.trees.Node;

import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.Classifier;
import weka.core.AdditionalMeasureProducer;
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

/**
 * Classifier which extends from weka class {@link Classifier}. It has usual
 * methods from there which we implement. Here in this class, we follow some
 * established principles for example implementing necessary methods </br>
 * <p>
 * {@link #buildClassifier} - to build our classifier, </br> {@link #setOptions}
 * - to set options from inside the weka, </br> {@link #listOptions} - to list
 * options from weka GUI, and others... </br> {@link #classifyInstance} - to
 * classify instance with builded classifier </br> and others... </br>
 * </p>
 * For this purpose (correct functionality of methods) it even implements from
 * some of the weka interfaces {@link Randomizable}, {@link OptionHandler},
 * {@link TechnicalInformationHandler}. </br> Name of this classifier inside the
 * weka is the same as this class name.
 * 
 * @see Classifier
 * @author Lukas Surin
 *
 */
public class WekaEvolutionTreeClassifier extends Classifier implements
		Randomizable, OptionHandler, TechnicalInformationHandler,
		AdditionalMeasureProducer, genlib.classifier.Classifier,
		WekaClassifierExtension {

	/** for serialization */
	private static final long serialVersionUID = 5314273117546487901L;
	/** Evolution classificator not dependant on weka. */
	private EvolutionTreeClassifier e_tree_class;

	/**
	 * Constructor for EvolutionClassifier which initialize
	 * EvolutionTreeClasifier class that is used as main classificator not
	 * dependant on weka. This approach let you use the library in weka as well
	 * as in different projects.
	 * 
	 * @throws Exception
	 *             Throws exception if
	 */
	public WekaEvolutionTreeClassifier() {
		GenLib.reconfig();
		this.e_tree_class = new EvolutionTreeClassifier(true);
	}

	/**
	 * List the available options which can be used for this classifier. It uses
	 * Vector for backcompatibility with Weka. SuppressWarnings is here because
	 * of this vector usage which is considered rawtype.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Enumeration listOptions() {

		Vector newVector = new Vector(12);

		newVector.addElement(new Option("\tFitness functions.\n"
				+ "\t(default " + e_tree_class.getFitnessFunctionsString()
				+ ")", "FF", 1, "-FF <function probability>"));

		newVector.addElement(new Option("\tFitness comparator.\n"
				+ "\t(default " + e_tree_class.getFitnessComparatorString()
				+ ")", "FC", 1, "-FC <comparator params>"));

		newVector.addElement(new Option("\tMutation probability.\n"
				+ "\t(default " + e_tree_class.getMutString() + ")", "MP", 1,
				"-MP <mutation probability>"));
		newVector.addElement(new Option("\tCrossover probability.\n"
				+ "\t(default " + e_tree_class.getXoverString() + ")", "XP", 1,
				"-XP <xover probability>"));
		newVector
				.addElement(new Option("\tElitism rate.\n" + "\t(default "
						+ e_tree_class.getElitism() + ")", "E", 1,
						"-E <elitism rate>"));
		newVector.addElement(new Option("\tSelectors.\n" + "\t(default "
				+ e_tree_class.getSelectorsString() + ")", "SE", 1,
				"-SE <selector param>"));
		newVector.addElement(new Option("\tEnvironmental selectors.\n"
				+ "\t(default " + e_tree_class.getEnvSelectorsString() + ")",
				"ESE", 1, "-ESE <env_selector param>"));
		newVector.addElement(new Option("\tSeed for random data shuffling.\n"
				+ "\t(default " + e_tree_class.getSeed() + ")", "S", 1,
				"-S <seed>"));
		newVector.addElement(new Option("\tPopulation generator", "PopP", 1,
				"-PopP <pop_params>"));
		newVector.addElement(new Option("\tIndividual generator", "IG", 1,
				"-IG <indgen_params>"));
		newVector.addElement(new Option("\tPopulation size", "IP", 1,
				"-IP <pop_size>"));
		newVector.addElement(new Option("\tFitness threads", "FT", 1,
				"-FT <#threads>"));
		newVector.addElement(new Option("\tGenerator threads", "GT", 1,
				"-GT <#threads>"));
		newVector.addElement(new Option("\tForced Reconfiguration?", "C", 1,
				"-C <True|False>"));

		return newVector.elements();
	}

	/**
	 * Gets the current settings of the evolution classifier. SuppressWarnings
	 * are present because of usage of Vector, but for the back-compatibility
	 * with Weka we are using it anyways.
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

		// stands for fitness functions
		result.add("-FF");
		result.add(getFitnessFunctions());

		// stands for fitness comparator
		result.add("-FC");
		result.add(getFitComp());

		// stands for mutation probability
		result.add("-MP");
		result.add(getMutationOperators());

		// stands for crossover probability
		result.add("-XP");
		result.add(getXoverOperatorsString());

		// stands for elitism
		result.add("-E");
		result.add("" + getElitism());

		// stands for selectors
		result.add("-SE");
		result.add("" + getSelectors());

		// stands for environmental selectors
		result.add("-ESE");
		result.add("" + getEnvSelectors());

		// stands for initial population size
		result.add("-IP");
		result.add("" + getPopulationSize());

		// stands for population generator parameter
		result.add("-PopP");
		result.add(getPopulationInit());

		// stands for individual generator parameter
		result.add("-IG");
		result.add(getIndividualGenerator());

		// stands for fitness threads
		result.add("-FT");
		result.add("" + getFitnessThreads());

		// stands for generator threads
		result.add("-GT");
		result.add("" + getGeneratorThreads());

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

	/**
	 * Parses a given list of options. Valid options are:
	 * <p>
	 * 
	 * -D <br>
	 * If set, classifier is run in debug mode and may output additional info to
	 * the console.
	 * <p>
	 * -S <br>
	 * Seed for this run.
	 * <p>
	 * -C <br>
	 * Forced reconfig if it's already configured and we need to load new
	 * configuration.
	 * <p>
	 * -FF <br>
	 * Fitness functions with their probability for this run separated by [;].
	 * <p>
	 * -FC <br>
	 * Fitness comparator with its params for this run. Comparator is only one.
	 * <p>
	 * -MP <br>
	 * Mutation operators with their probability for this run separated by [;].
	 * <p>
	 * -XP <br>
	 * Crossover operators with their probability for this run separated by [;].
	 * <p>
	 * -PopP <br>
	 * Population initializator
	 * <p>
	 * -IG <br>
	 * Individual generator that will populate population initializator
	 * <p>
	 * -E <br>
	 * Elitism rate of individuals for this run.
	 * <p>
	 * -IP <br>
	 * Population size with which we will be working.
	 * <p>
	 * 
	 * @param options
	 *            the list of options as an array of strings
	 * @exception Exception
	 *                if an option is not supported
	 */
	@Override
	public void setOptions(String[] options) throws Exception {
		String tmpStr;

		tmpStr = Utils.getOption("FF", options);
		if (tmpStr.length() != 0) {
			e_tree_class.setFitFuncsString(tmpStr);
		}

		tmpStr = Utils.getOption("FC", options);
		if (tmpStr.length() != 0) {
			e_tree_class.setFitCompString(tmpStr);
		}

		tmpStr = Utils.getOption("MP", options);
		if (tmpStr.length() != 0) {
			e_tree_class.setMutString(tmpStr);
		}

		tmpStr = Utils.getOption("XP", options);
		if (tmpStr.length() != 0) {
			e_tree_class.setXoverString(tmpStr);
		}

		tmpStr = Utils.getOption("E", options);
		if (tmpStr.length() != 0) {
			e_tree_class.setElitism(Double.parseDouble(tmpStr));
		}

		tmpStr = Utils.getOption("SE", options);
		if (tmpStr.length() != 0) {
			e_tree_class.setSelectorsString(tmpStr);
		}

		tmpStr = Utils.getOption("ESE", options);
		if (tmpStr.length() != 0) {
			e_tree_class.setEnvSelectorsString(tmpStr);
		}

		tmpStr = Utils.getOption("PopP", options);
		if (tmpStr.length() != 0) {
			e_tree_class.setPopInitString(tmpStr);
		}

		tmpStr = Utils.getOption("IG", options);
		if (tmpStr.length() != 0) {
			e_tree_class.setIndividualGeneratorString(tmpStr);
		}

		tmpStr = Utils.getOption("IP", options);
		if (tmpStr.length() != 0) {
			e_tree_class.setPopulationSize(Integer.parseInt(tmpStr));
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

	/**
	 * {@inheritDoc}
	 * 
	 * @throws ConfigInternalException
	 *             if any parameter pulled from config is null.
	 * @throws EmptyConfigParamException
	 *             if any parameter is empty.
	 */
	@Override
	public void buildClassifier(Instances data) throws Exception {
		if (e_tree_class.getMutString() == null
				|| e_tree_class.getXoverString() == null
				|| e_tree_class.getPopInitString() == null
				|| e_tree_class.getFitnessComparatorString() == null
				|| e_tree_class.getFitnessFunctionsString() == null
				|| e_tree_class.getIndividualGeneratorString() == null)
			throw new ConfigInternalException(
					TextResource.getString(TextKeys.eInvalidInputParams));

		if (e_tree_class.getMutString().isEmpty()
				|| e_tree_class.getXoverString().isEmpty()
				|| e_tree_class.getPopInitString().isEmpty()
				|| e_tree_class.getFitnessComparatorString().isEmpty()
				|| e_tree_class.getFitnessFunctionsString().isEmpty()
				|| e_tree_class.getIndividualGeneratorString().isEmpty()) {
			throw new EmptyConfigParamException(
					TextResource.getString(TextKeys.eEmptyInputParams));
		}

		// can classifier tree handle the data?
		getCapabilities().testWithFail(data);

		// remove instances with missing class
		data = new Instances(data);
		data.deleteWithMissingClass();

		e_tree_class.buildClassifier(data);
		// that's all for this method
	}

	/**
	 * Classifies an {@link GenLibInstance} with newly created
	 * {@link TreeIndividual}. It's slightly different from weka classify method
	 * because it uses different type of representation for individual.
	 *
	 * @param instance
	 *            the instance to classify
	 * @return the classification for the instance
	 * @throws Exception
	 *             if instance can't be classified successfully
	 */
	public double classifyInstance(Instance instance) throws Exception {
		TreeIndividual bestIndividual = e_tree_class.getBestIndividual();

		Node root = bestIndividual.getRootNode();

		while (!root.isLeaf()) {
			if (instance.attribute(root.getAttribute()).isNumeric()) {
				if (genlib.utils.Utils.isValueProper(
						instance.value(root.getAttribute()), root.getSign(),
						root.getValue())) {
					root = root.getChildAt(0);
				} else {
					root = root.getChildAt(1);
				}
			} else {
				root = root
						.getChildAt((int) instance.value(root.getAttribute()));
			}
		}

		return root.getValue();

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
	 * @param seed
	 *            Value to assign to Seed.
	 */
	@Override
	public void setSeed(int seed) {
		e_tree_class.setSeed(seed);
	}

	/**
	 * Return the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String seedTipText() {
		return TextResource.getString("wSeedTipText");
	}

	/**
	 * Getter method for the parameter FF. It contains information about fitness
	 * functions.
	 * 
	 * @return String value of parameter FF
	 */
	public String getFitnessFunctions() {
		return e_tree_class.getFitnessFunctionsString();
	}

	/**
	 * Setter method for the parameter FF. It contains information about fitness
	 * functions.
	 * 
	 * @param String
	 *            value of parameter FF
	 */
	public void setFitnessFunctions(String fitParam) {
		e_tree_class.setFitFuncsString(fitParam);
	}

	/**
	 * Return the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String fitnessFunctionsTipText() {
		return TextResource.getString("wfitFuncTipText");
	}

	/**
	 * Getter method for the parameter FC. It contains information about fitness
	 * comparator/evaluator.
	 * 
	 * @return String value of parameter FC
	 */
	public String getFitComp() {
		return e_tree_class.getFitnessComparatorString();
	}

	/**
	 * Setter method for the parameter FC. It contains information about fitness
	 * comparator/evaluator.
	 * 
	 * @param String
	 *            value of parameter FC
	 */
	public void setFitComp(String fitCompParam) {
		e_tree_class.setFitCompString(fitCompParam);
	}

	/**
	 * Return the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String fitCompTipText() {
		return TextResource.getString("wfitCompTipText");
	}

	/**
	 * Getter method for the parameter MP. It contains information about
	 * mutation operators.
	 * 
	 * @return String value of parameter XP
	 */
	public String getMutationOperators() {
		return e_tree_class.getMutString();
	}

	/**
	 * Setter method for the parameter MP. It contains information about
	 * mutation operators.
	 * 
	 * @param String
	 *            value of parameter MP
	 */
	public void setMutationOperators(String mutParam) {
		e_tree_class.setMutString(mutParam);
	}

	/**
	 * Return the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String mutationOperatorsTipText() {
		return TextResource.getString("wMutProbTipText");
	}

	/**
	 * Getter method for the parameter XP. It contains information about
	 * crossover operators.
	 * 
	 * @return String value of parameter XP
	 */
	public String getXoverOperatorsString() {
		return e_tree_class.getXoverString();
	}

	/**
	 * Setter method for the parameter XP. It contains information about
	 * crossover operators.
	 * 
	 * @param String
	 *            value of parameter XP
	 */
	public void setXoverOperators(String xoverParam) {
		e_tree_class.setXoverString(xoverParam);
	}

	/**
	 * Return the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String xoverOperatorsTipText() {
		return TextResource.getString("wXoverProbTipText");
	}

	/**
	 * Method returns fraction of population that will be elitized (advanced) to
	 * the next offspring. It is wide known parameter in an Evolution
	 * Algorithms.
	 * 
	 * @return Value of elitism parameter.
	 */
	public double getElitism() {
		return e_tree_class.getElitism();
	}

	/**
	 * Method set the fraction of population that will be elitized (advanced) to
	 * the next offspring. It is wide known parameter in an Evolution
	 * Algorithms.
	 * 
	 * @param elitism
	 *            Value of elitism parameter.
	 * 
	 */
	public void setElitism(double elitism) {
		e_tree_class.setElitism(elitism);
	}

	/**
	 * Return the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String elitismTipText() {
		return TextResource.getString("wElitism");
	}

	/**
	 * Getter method for the parameter IP. It contains information about initial
	 * population generator.
	 * 
	 * @return String value of parameter IP
	 */
	public String getPopulationInit() {
		return e_tree_class.getPopInitString();
	}

	/**
	 * Setter method for the parameter IP. It contains information about initial
	 * population generator
	 * 
	 * @param popInitString
	 *            value of parameter IP
	 */
	public void setPopulationInit(String popInitString) throws Exception {
		e_tree_class.setPopInitString(popInitString);
	}

	/**
	 * Return the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String populationInitTipText() {
		return TextResource.getString("wPopInitTipText");
	}

	/**
	 * Getter method for the parameter IG. It contains information about
	 * individual generator.
	 * 
	 * @return String value of parameter IG
	 */
	public String getIndividualGenerator() {
		return e_tree_class.getIndividualGeneratorString();
	}

	/**
	 * Setter method for the parameter IG. It contains information about
	 * individual generator.
	 * 
	 * @param popInitString
	 *            value of parameter IG
	 */
	public void setIndividualGenerator(String indGenString) throws Exception {
		e_tree_class.setIndividualGeneratorString(indGenString);
	}

	/**
	 * Return the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String individualGeneratorTipText() {
		return TextResource.getString("wIndGenTipText");
	}

	/**
	 * Returns the size of population which we are working with.
	 * 
	 * @return size of population
	 */
	public int getPopulationSize() {
		return e_tree_class.getPopulationSize();
	}

	/**
	 * Sets the size of population which we are going to work with.
	 * 
	 * @param populationSize
	 *            new size of population
	 */
	public void setPopulationSize(int populationSize) {
		e_tree_class.setPopulationSize(populationSize);
	}

	/**
	 * Return the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String populationSizeTipText() {
		return TextResource.getString("wPopSizeTipText");
	}

	/**
	 * Returns number of threads that compute fitness
	 * 
	 * @return number of threads
	 */
	public int getFitnessThreads() {
		return e_tree_class.getFitnessThreads();
	}

	/**
	 * Sets the number of threads that compute fitness.
	 * 
	 * @param number
	 *            of threads
	 */
	public void setFitnessThreads(int fitThreads) {
		e_tree_class.setFitnessThreads(fitThreads);
	}

	/**
	 * Return the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String fitnessThreadsTipText() {
		return TextResource.getString("wfitThreadsTipText");
	}

	/**
	 * Returns number of threads that compute fitness
	 * 
	 * @return number of threads
	 */
	public int getGeneratorThreads() {
		return e_tree_class.getGeneratorThreads();
	}

	/**
	 * Sets the number of threads that compute fitness.
	 * 
	 * @param number
	 *            of threads
	 */
	public void setGeneratorThreads(int fitThreads) {
		e_tree_class.setGeneratorThreads(fitThreads);
	}

	/**
	 * Return the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String generatorThreadsTipText() {
		return TextResource.getString(TextKeys.wGenThreadsTipText);
	}

	/**
	 * Getter method for the parameter SE. It contains information about
	 * selectors.
	 * 
	 * @param String
	 *            value of parameter SE
	 */
	public String getSelectors() {
		return e_tree_class.getSelectorsString();
	}

	/**
	 * Setter method for the parameter SE. It contains information about
	 * selectors.
	 * 
	 * @param String
	 *            value of parameter SE
	 */
	public void setSelectors(String selectorsString) {
		e_tree_class.setSelectorsString(selectorsString);
	}

	/**
	 * Return the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String selectorsTipText() {
		return TextResource.getString(TextKeys.wSelectorTipText);
	}

	/**
	 * Setter method for the parameter ESE. It contains information about
	 * environmental selectors.
	 * 
	 * @param String
	 *            value of parameter ESE
	 */
	public String getEnvSelectors() {
		return e_tree_class.getEnvSelectorsString();
	}

	/**
	 * Setter method for the parameter ESE. It contains information about
	 * environmental selectors.
	 * 
	 * @param String
	 *            value of parameter ESE
	 */
	public void setEnvSelectors(String selectorsString) {
		e_tree_class.setEnvSelectorsString(selectorsString);
	}

	/**
	 * Return the tip text for this property
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String envSelectorsTipText() {
		return TextResource.getString(TextKeys.wEnvSelectorTipText);
	}

	/**
	 * Sets if this classifier is already configured.
	 * 
	 * @param configured
	 *            true/false if it's configured or not.
	 */
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
	 * 
	 * @return tip text for this property suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String configuredTipText() {
		return TextResource.getString("wConfigTipText");
	}

	/**
	 * Get capabilities of this classifier what it can be working with. This
	 * classifier can work with nominal, numeric attributes even if some values
	 * are missing. It is normal classifier (no regression) so it classifies
	 * into nominal classes and even if provided data are missing some values.
	 * </br> It must be set because without further specification this
	 * classificator would be capable of nothing (what we do not want).
	 * 
	 * @return Object Capabilities that is defined inside weka.
	 * @see Capabilities
	 */
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

	/**
	 * Creates globalInfo about this classifier. It consists of simple
	 * description of this classificator with conjuction of technical
	 * information that can be returned via getTechnicalInformation method.
	 * 
	 * @return global info of this classificator.
	 */
	public String globalInfo() {
		return "Class for constructing decision tree with evolution metaheuristic.\n\n"
				+ "For more information see: \n\n" + getTechnicalInformation();
	}

	/**
	 * Method will built up the TechnicalInformation object with basic
	 * information about this classificator that can be accessed from weka.
	 * 
	 * @return Object TechnicalInformation with basic stats
	 * @see TechnicalInformation
	 */
	@Override
	public TechnicalInformation getTechnicalInformation() {
		TechnicalInformation result;
		result = new TechnicalInformation(Type.ARTICLE);
		result.setValue(Field.AUTHOR, "Lukas Surin");
		result.setValue(Field.YEAR, "2015");
		result.setValue(Field.TITLE, "Evolutionary Trees");
		return result;
	}

	/**
	 * Returns an enumeration of the additional measure names
	 * 
	 * @return an enumeration of the measure names
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Enumeration enumerateMeasures() {
		Vector newVector = new Vector(3);
		newVector.addElement("measureTreeSize");
		newVector.addElement("measureNumLeaves");
		return newVector.elements();
	}

	/**
	 * Returns the value of the named measure
	 * 
	 * @param additionalMeasureName
	 *            the name of the measure to query for its value
	 * @return the value of the named measure
	 * @throws IllegalArgumentException
	 *             if the named measure is not supported
	 */
	public double getMeasure(String additionalMeasureName) {
		if (additionalMeasureName.compareToIgnoreCase("measureTreeSize") == 0) {
			return e_tree_class.getBestIndividual().getTreeSize();
		} else if (additionalMeasureName
				.compareToIgnoreCase("measureNumLeaves") == 0) {
			return e_tree_class.getBestIndividual().getNumLeaves();
		} else {
			throw new IllegalArgumentException(additionalMeasureName
					+ " not supported (genlib EAUniTreeClassifier)");
		}
	}

	/**
	 * Main method for testing this class
	 *
	 * @param argv
	 *            the commandline options
	 */
	public static void main(String[] argv) {
		System.out.println("DONE");
		runClassifier(new WekaEvolutionTreeClassifier(), argv);
	}

	@Override
	public void buildClassifier(Data data) throws Exception {
		if (data.isInstances()) {
			buildClassifier(data.toInstances());
		} else {
			throw new WrongDataException(String.format(
					TextResource.getString(TextKeys.eTypeParameter),
					Instances.class.getName(), data.getData().getClass()
							.getName()));
		}
	}

	@Override
	public double[] classifyData(Data data) throws Exception {
		if (data.isInstances()) {
			double[] classifications = new double[data.numInstances()];

			Instances instances = data.toInstances();

			// should be enumeration of instances
			@SuppressWarnings("unchecked")
			Enumeration<Instance> enumeration = (Enumeration<Instance>) instances
					.enumerateInstances();

			int index = 0;
			while (enumeration.hasMoreElements()) {
				classifications[index++] = classifyInstance(enumeration
						.nextElement());
			}
			return classifications;
		} else {
			throw new WrongDataException(String.format(
					TextResource.getString(TextKeys.eTypeParameter),
					Instances.class.getName(), data.getData().getClass()
							.getName()));
		}
	}

}
