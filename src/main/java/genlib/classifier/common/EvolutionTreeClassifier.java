package genlib.classifier.common;

import genlib.GenDTLib;
import genlib.configurations.Config;
import genlib.evolution.EvolutionAlgorithm;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.fitness.comparators.FitnessComparator.FitCompare;
import genlib.evolution.fitness.comparators.ParetoFitnessComparator;
import genlib.evolution.fitness.comparators.PriorityFitnessComparator;
import genlib.evolution.fitness.comparators.SingleFitnessComparator;
import genlib.evolution.fitness.comparators.WeightedFitnessComparator;
import genlib.evolution.individuals.Individual;
import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.operators.Operator;
import genlib.evolution.population.IPopulation;
import genlib.evolution.population.Population;
import genlib.evolution.selectors.Selector;
import genlib.exceptions.CompatibleWekaException;
import genlib.exceptions.NotDefClassException;
import genlib.exceptions.NotInitializedFieldException;
import genlib.exceptions.NumericHandleException;
import genlib.exceptions.TypeParameterException;
import genlib.exceptions.format.FitCompStringFormatException;
import genlib.exceptions.format.FitnessStringFormatException;
import genlib.exceptions.format.OperatorStringFormatException;
import genlib.exceptions.format.PopulationInitStringFormatException;
import genlib.exceptions.format.PopulationTypeStringFormatException;
import genlib.exceptions.format.SelectorStringFormatException;
import genlib.generators.Generator;
import genlib.generators.TreeGenerator;
import genlib.initializators.PopulationInitializator;
import genlib.initializators.TreePopulationInitializator;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;
import genlib.plugins.PluginManager;
import genlib.structures.Data;
import genlib.structures.data.GenLibInstances;
import genlib.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import weka.core.Instances;

/**
 * Common class that summarize the genetic algorithm and allow to use it from
 * Classifier (weka or non weka). Other classes should call
 * {@link EvolutionTreeClassifier#buildClassifier(GenLibInstances)} or
 * {@link EvolutionTreeClassifier#buildClassifier(Instances)} depending on type
 * of classifier
 * 
 * @author Lukas Surin
 *
 */
public class EvolutionTreeClassifier implements Serializable {
	private static final Logger LOG = Logger
			.getLogger(EvolutionTreeClassifier.class.getName());
	/** for serialization */
	private static final long serialVersionUID = -762451118775604722L;

	/** data which we work with */
	private Data workData;

	/* Random generator for this run */
	private Random random;
	/** config for this run from which we load the configurations */
	private Config c;
	/** fitness comparator to be used in sorting individuals */
	private FitnessComparator<TreeIndividual> fitComp;
	/** fitness functions which we evaluate and use in fitComp */
	private ArrayList<FitnessFunction<TreeIndividual>> fitFuncs;
	/** mutation operators which are used in reproduction stage */
	private ArrayList<Operator<TreeIndividual>> mutSet;
	/** crossover operators which are used in reproduction stage */
	private ArrayList<Operator<TreeIndividual>> xoverSet;
	/** selectors that select the individuals to be reproduced */
	private ArrayList<Selector> selectors;
	/** envselectors that select the offspring into next generation */
	private ArrayList<Selector> envSelectors;
	/** tag if we are using weka */
	private boolean isWeka;
	/** population initializator that created the first individuals */
	private TreePopulationInitializator popInit;
	/**
	 * object with evolution algorithm to which we set all the
	 * operators,selectors,... and evaluate.
	 */
	private EvolutionAlgorithm<TreeIndividual> ea;
	/** individuals with the sorted individuals from the last population */
	private ArrayList<TreeIndividual> theBestIndividuals;
	/** the best start individual from the starting population */
	private TreeIndividual startIndividual;

	private String populationTypeString, populationInitString, indGenString;
	private String selectorsString, envSelectorsString, fitCompString;
	private String fitFunctionsString, xOverOpString, mutOpString, dataString;
	private int classify;
	private int populationSizeInt, numOfGensInt, fitThreadsInt, genThreadsInt,
			operThreadsInt;
	private double elitismRateDouble;
	private long seed;

	/**
	 * Constructor that creates instance of EvolutionTreeClassifier. It sets the
	 * private fields for components from {@link Config}.
	 * 
	 * @param isWeka
	 *            true iff we are using it from Weka.
	 */
	public EvolutionTreeClassifier(boolean isWeka) {
		this.c = Config.getInstance();
		this.isWeka = isWeka;
		this.fitFuncs = new ArrayList<>();
		this.mutSet = new ArrayList<>();
		this.xoverSet = new ArrayList<>();
		this.selectors = new ArrayList<>();
		this.envSelectors = new ArrayList<>();

		this.seed = c.getSeed();
		this.dataString = c.getData();
		this.classify = c.getClassify();

		this.populationTypeString = c.getPopulationType();
		this.populationInitString = c.getPopulationInit();
		this.populationSizeInt = c.getPopulationSize();
		this.indGenString = c.getIndGenerators();
		this.genThreadsInt = c.getGenNumOfThreads();

		this.selectorsString = c.getSelectors();
		this.envSelectorsString = c.getEnvSelectors();

		this.fitCompString = c.getFitnessComparator();
		this.fitFunctionsString = c.getFitnessFunctions();
		this.fitThreadsInt = c.getFitNumOfThreads();

		this.xOverOpString = c.getXoverOperators();
		this.mutOpString = c.getMutationOperators();
		this.operThreadsInt = c.getOperNumOfThreads();

		this.elitismRateDouble = c.getElitismRate();
		this.numOfGensInt = c.getNumberOfGenerations();

		this.random = new Random(seed);
	}

	/**
	 * Method which initialize the components and data to be used and calls the
	 * {@link #commonBuildClassifier(Data)} that calls the population
	 * initialization and evolution algorithm
	 * 
	 * @param data
	 *            with weka Instances
	 * @throws Exception
	 *             if error occured when creating model
	 */
	public void buildClassifier(Instances data) throws Exception {
		GenDTLib.reconfig();

		theBestIndividuals = null;
		startIndividual = null;

		// create adapter Data around instances
		this.workData = new Data(data, new Random(random.nextLong()));
		// split the data into train and validation
		workData.setParam(dataString);
		// making properties from string
		makePropsFromString(data.classAttribute().isNumeric());
		// Shuffle data
		data.randomize(random);
		// setting additional params for created fields
		setAdditionalParams(workData);

		commonBuildClassifier(workData);
	}

	/**
	 * Method which initialize the components and data to be used and calls the
	 * {@link #commonBuildClassifier(Data)} that calls the population
	 * initialization and evolution algorithm
	 * 
	 * @param data
	 *            with GenLibInstances
	 * @throws Exception
	 *             if error occured when creating model
	 */
	public void buildClassifier(GenLibInstances data) throws Exception {
		GenDTLib.reconfig();
		theBestIndividuals = null;
		startIndividual = null;

		// create adapter Data around instances
		this.workData = new Data(data, new Random(random.nextLong()));
		// split the data into train and validation
		workData.setParam(dataString);
		// making properties from string
		makePropsFromString(data.numClasses() == -1);
		// Shuffle data
		data.randomize(random);
		// setting additional params for created fields
		setAdditionalParams(workData);

		commonBuildClassifier(workData);

	}

	/**
	 * Method that initializes the population and calls the evolution algorithm
	 * that is firstly set up. In the end the bestIndividual is set up.
	 * 
	 * @param workData
	 *            that are used by evolution algorithm
	 * @throws Exception
	 *             if error occured when creating model
	 */
	private void commonBuildClassifier(Data workData) throws Exception {
		popInit.setRandomGenerator(new Random(random.nextLong()));
		popInit.setData(workData);
		popInit.initPopulation();

		// Population object that contains tree individuals from population
		// initializator
		IPopulation<TreeIndividual> population = makePopulation();
		startIndividual = population.getSortedIndividuals(fitComp).get(0)
				.copy();

		// Evolution algorithm that evolves population of tree individuals.
		ea = new EvolutionAlgorithm<>(workData, population, numOfGensInt);
		// number of generations to be run through
		ea.setNumberOfGenerations(numOfGensInt);
		// mates are selected with selector
		ea.setSelectors(selectors);
		// selectors for environment selection
		ea.setEnvSelectors(envSelectors);
		// mates are crossed with operators inside this list
		ea.setCrossOperators(xoverSet);
		// mates are mutated with operators inside this list
		ea.setMutationOperators(mutSet);
		// mates are evaluated with fitness functions inside this list
		ea.setFitnessFunctions(fitFuncs);
		// fitness functions are evaluated by one comparator
		ea.setFitnessComparator(fitComp);
		// percentage of parents that stay into next generation
		ea.setElitism(elitismRateDouble);

		// start run time
		long start = System.nanoTime();
		// run population evolving
		ea.run();
		// log the amount of time to build classifier
		LOG.info(String.format(TextResource.getString(TextKeys.done),
				((double) (System.nanoTime() - start)) / 1000000000d));

		theBestIndividuals = ea.getActualPopulation().getSortedIndividuals(
				fitComp);

	}

	/**
	 * Reconfig method that resets the config of components.
	 */
	public void reconfig() {
		this.seed = c.getSeed();
		this.dataString = c.getData();

		this.populationTypeString = c.getPopulationType();
		this.populationInitString = c.getPopulationInit();
		this.populationSizeInt = c.getPopulationSize();
		this.indGenString = c.getIndGenerators();
		this.genThreadsInt = c.getGenNumOfThreads();

		this.selectorsString = c.getSelectors();
		this.envSelectorsString = c.getEnvSelectors();

		this.fitCompString = c.getFitnessComparator();
		this.fitFunctionsString = c.getFitnessFunctions();
		this.fitThreadsInt = c.getFitNumOfThreads();

		this.xOverOpString = c.getXoverOperators();
		this.mutOpString = c.getMutationOperators();
		this.operThreadsInt = c.getOperNumOfThreads();

		this.elitismRateDouble = c.getElitismRate();
		this.numOfGensInt = c.getNumberOfGenerations();

		this.random = new Random(seed);
	}

	/**
	 * Method which sets the additional parameters to components. In this case
	 * we set up the data object into components.
	 * 
	 * @param data
	 *            object that are used by components
	 */
	public void setAdditionalParams(Data data) {
		// set the data for fitness functions
		for (FitnessFunction<TreeIndividual> function : fitFuncs) {
			function.setData(data);
		}

		// set the data for mutation operators
		for (Operator<TreeIndividual> op : mutSet) {
			op.setData(data);
		}

		// set the data for xover operators
		for (Operator<TreeIndividual> op : xoverSet) {
			op.setData(data);
		}
	}

	/**
	 * Method that make different type of sets from configuration file.
	 * 
	 * @param isNumeric
	 *            if the attributes are numeric
	 * @throws Exception
	 *             if error occured when creating component from configuration
	 *             file
	 */
	public void makePropsFromString(boolean isNumeric) throws Exception {
		if (fitFunctionsString != null) {
			makeFitnessFunctionsSet(isNumeric);
		}
		if (fitCompString != null) {
			makeFitnessCompSet();
		}
		if (xOverOpString != null) {
			makeXoverOperatorSet();
		}
		if (mutOpString != null) {
			makeMutationOperatorSet();
		}
		if (populationInitString != null) {
			makePopInitializator();
		}
		if (indGenString != null) {
			makeGenerators();
		}
		if (selectorsString != null) {
			makeSelectorSet();
		}
		if (envSelectorsString != null) {
			makeEnvSelectorSet();
		}
	}

	/**
	 * Method accessed only from this method that parses parameter population
	 * type which contains information about population containers. Method makes
	 * the population container. Definition is taken from config property file.
	 * 
	 * @throws PopulationTypeStringFormatException
	 *             if there is bad format for population containers
	 * @throws NotDefClassException
	 *             if there isn't config for population
	 */
	public IPopulation<TreeIndividual> makePopulation() throws Exception {
		String[] parameters = populationTypeString.split(Utils.oDELIM);

		if (parameters.length < 2) {
			throw new PopulationTypeStringFormatException(
					TextResource.getString(TextKeys.ePopTypeFormat));
		}

		@SuppressWarnings("rawtypes")
		Class<? extends IPopulation> iPopClass = PluginManager.populationTypes
				.get(parameters[0]);

		if (iPopClass != null) {
			IPopulation<?> population = iPopClass.newInstance();
			IPopulation<TreeIndividual> toReturn = population.makeNewInstance();
			toReturn.setIndividuals(popInit.getPopulation());
		}

		return new Population<>(popInit.getPopulation(), populationSizeInt);
	}

	/**
	 * Method accessed only from this method that parses parameter selector-set
	 * which contains information about selectors. Method makes the selector
	 * set. Definition is taken from config property file.
	 * 
	 * @throws SelectorStringFormatException
	 *             if there is bad format for selectors
	 * @throws NotDefClassException
	 *             if there isn't config for selectors
	 */
	private void makeSelectorSet() throws Exception {
		selectors.clear();
		String[] parameters = selectorsString.split(Utils.oDELIM);
		if (parameters.length % 2 != 0) {
			// bad format
			throw new SelectorStringFormatException(
					TextResource.getString("eSelFormat"));
		}

		HashMap<String, Class<? extends Selector>> h = PluginManager.selectors;

		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "") {
				selectors.clear();
				// blank alias
				throw new Exception();
			}

			Class<? extends Selector> selectorClass = h.get(parameters[i]);

			if (selectorClass == null) {
				LOG.log(Level.SEVERE, String.format(
						TextResource.getString(TextKeys.eNotDefClass),
						parameters[0]));
				// not defined class for pop init
				throw new NotDefClassException(String.format(
						TextResource.getString(TextKeys.eNotDefClass),
						parameters[0]));
			}

			Selector selector = selectorClass.newInstance();

			selector.setRandomGenerator(new Random(random.nextLong()));
			selector.setParam(parameters[i + 1]);

			selectors.add(selector);
		}
	}

	/**
	 * Method accessed only from this method that parses parameter
	 * envselector-set which contains information about environmental set.
	 * Method makes the environmental selector set. Definition is taken from
	 * config property file.
	 * 
	 * @throws SelectorStringFormatException
	 *             if there is bad format for env selectors
	 * @throws NotDefClassException
	 *             if there isn't config for env selectors
	 */
	private void makeEnvSelectorSet() throws Exception {
		envSelectors.clear();
		String[] parameters = envSelectorsString.split(Utils.oDELIM);
		if (parameters.length % 2 != 0) {
			// bad format
			throw new SelectorStringFormatException(
					TextResource.getString("eSelFormat"));
		}

		HashMap<String, Class<? extends Selector>> h = PluginManager.envSelectors;

		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "") {
				envSelectors.clear();
				// blank alias
				throw new Exception();
			}

			Class<? extends Selector> selectorClass = h.get(parameters[i]);

			if (selectorClass == null) {
				LOG.log(Level.SEVERE, String.format(
						TextResource.getString(TextKeys.eNotDefClass),
						parameters[0]));
				// not defined class for pop init
				throw new NotDefClassException(String.format(
						TextResource.getString(TextKeys.eNotDefClass),
						parameters[0]));
			}

			Selector selector = selectorClass.newInstance();
			selector.setRandomGenerator(new Random(random.nextLong()));
			selector.setParam(parameters[i + 1]);

			envSelectors.add(selector);
		}
	}

	/**
	 * Method accessed only from this method that parses parameter fitComp which
	 * contains information about fitness comparators. Method makes the fitness
	 * comparators set. Definition is taken from config property file.
	 * 
	 * @throws FitCompStringFormatException
	 *             if there is bad format for fit comparators
	 * @throws NotDefClassException
	 *             if there isn't config for fit comparators
	 */
	private void makeFitnessCompSet() throws Exception {
		fitComp = null;
		String[] parameters = fitCompString.split(Utils.oDELIM);

		if (parameters.length <= 0 || parameters.length > 2) {
			// bad format
			throw new FitCompStringFormatException(
					TextResource.getString("eFitCompFormat"));
		}

		FitCompare fitCompare = FitCompare.valueOf(parameters[0]);

		switch (fitCompare) {
		case PARETO:
			fitComp = new ParetoFitnessComparator<>();
			break;
		case PRIORITY:
			fitComp = new PriorityFitnessComparator<>(fitFuncs.size());
			break;
		case SINGLE:
			fitComp = new SingleFitnessComparator<>();
			break;
		case WEIGHT:
			fitComp = new WeightedFitnessComparator<>(fitFuncs.size());
			break;
		default:
			break;
		}

		if (fitComp != null) {
			fitComp.setFitFuncs(fitFuncs);
			if (parameters.length >= 2) {
				fitComp.setParam(parameters[1]);
			}
		}
	}

	/**
	 * Method accessed only from this method that parses parameter fit-functions
	 * which contains information about fitness functions. Method makes the
	 * fitness function set. Definition is taken from config property file.
	 * 
	 * @throws FitnessStringFormatException
	 *             if there is bad format for fitness functions
	 * @throws NotDefClassException
	 *             if there isn't config for fitness functions
	 */
	private void makeFitnessFunctionsSet(boolean isNumeric) throws Exception {
		fitFuncs.clear();
		String[] parameters = fitFunctionsString.split(Utils.oDELIM);
		if (parameters.length % 2 != 0) {
			// bad format
			throw new FitnessStringFormatException(
					TextResource.getString("eFitnessFormat"));
		}

		int max = 0;

		HashMap<String, Class<? extends FitnessFunction<? extends Individual>>> h = PluginManager.fitFuncs;
		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "") {
				fitFuncs.clear();
				// blank alias
				throw new FitnessStringFormatException(
						TextResource.getString("eFitnessFormat"));
			}

			// inner error
			Class<? extends FitnessFunction<? extends Individual>> fitFuncClass = h
					.get(parameters[i]);

			if (fitFuncClass == null) {
				LOG.log(Level.SEVERE, String.format(
						TextResource.getString(TextKeys.eNotDefClass),
						parameters[i]));
				// not defined class for fitness functions
				throw new NotDefClassException(String.format(
						TextResource.getString(TextKeys.eNotDefClass),
						parameters[i]));
			}

			FitnessFunction<? extends Individual> genFunc = fitFuncClass
					.newInstance();
			if (genFunc.getIndividualClassType() != TreeIndividual.class) {
				throw new TypeParameterException(String.format(
						TextResource.getString(TextKeys.eTypeParameter),
						genFunc.getIndividualClassType().getName(),
						TreeIndividual.class.getName()));
			}

			// we checked the type, so the retype is always correct
			@SuppressWarnings("unchecked")
			FitnessFunction<TreeIndividual> func = (FitnessFunction<TreeIndividual>) fitFuncClass
					.newInstance();

			// if class attribute is numeric and function can't handle it.
			if (isNumeric && !func.canHandleNumeric()) {
				throw new NumericHandleException(
						String.format(TextResource.getString("eNumericHandle"),
								func.getClass().getName()));
			}

			// if there's not already defined index than set the index
			if (func.getIndex() == -1) {
				func.setIndex(max++);
			}
			func.setParam(parameters[i + 1]);
			fitFuncs.add(func);
		}
		FitnessFunction.registeredFunctions = max;
	}

	/**
	 * Method accessed only from this method that parses parameter xover-set
	 * which contains information about crossover operators. Method makes the
	 * crossover operators set. They are created from config property file.
	 * <ul>
	 * <li>If there is some problem with initialization than the excepcion is
	 * thrown.</li>
	 * <li>If the crossover is dependent on weka and we are not using weka than
	 * exception is thrown.</li>
	 * <li>If the crossover isn't compatible with weka and we are using weka
	 * than exception is thrown.</li>
	 * </ul>
	 * 
	 * @throws OperatorStringFormatException
	 *             if there is bad format for operators
	 * @throws NotDefClassException
	 *             if there isn't mutation operator for config
	 * @throws CompatibleWekaException
	 *             if the mutation operator is dependent on weka or isn't
	 *             compatible with weka when we use weka.
	 */
	private void makeXoverOperatorSet() throws Exception {
		xoverSet.clear();
		String[] parameters = xOverOpString.split(Utils.oDELIM);
		if (parameters.length % 2 != 0) {
			// bad format
			throw new OperatorStringFormatException(
					TextResource.getString("eOperFormat"));
		}

		HashMap<String, Class<? extends Operator<? extends Individual>>> h = PluginManager.xOper;
		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "") {
				xoverSet.clear();
				// blank alias
				throw new OperatorStringFormatException(
						TextResource.getString("eOperFormat"));
			}

			// inner error
			Class<? extends Operator<? extends Individual>> xOperClass = h
					.get(parameters[i]);

			if (xOperClass == null) {
				LOG.log(Level.SEVERE, String.format(
						TextResource.getString(TextKeys.eNotDefClass),
						parameters[0]));
				// not defined class for pop init
				throw new NotDefClassException(String.format(
						TextResource.getString(TextKeys.eNotDefClass),
						parameters[0]));
			}

			Operator<? extends Individual> genXOper = xOperClass.newInstance();
			if (genXOper.getIndividualClassType() != TreeIndividual.class) {
				throw new TypeParameterException(String.format(
						TextResource.getString(TextKeys.eTypeParameter),
						genXOper.getIndividualClassType().getName(),
						TreeIndividual.class.getName()));
			}

			// we checked the type, so the retype is always correct
			@SuppressWarnings("unchecked")
			Operator<TreeIndividual> xOper = (Operator<TreeIndividual>) genXOper;

			if (xOper.isWekaDependent() && !isWeka) {
				LOG.log(Level.SEVERE, String.format(
						TextResource.getString(TextKeys.eWekaDependency),
						xOperClass.getName()));
				throw new CompatibleWekaException(String.format(
						TextResource.getString(TextKeys.eWekaDependency),
						xOperClass.getName()));
			}

			if (!xOper.isWekaCompatible() && isWeka) {
				LOG.log(Level.SEVERE, String.format(
						TextResource.getString(TextKeys.eWekaCompatibility),
						xOperClass.getName()));
				throw new CompatibleWekaException(String.format(
						TextResource.getString(TextKeys.eWekaCompatibility),
						xOperClass.getName()));
			}

			xOper.setRandomGenerator(new Random(random.nextLong()));
			xOper.setParam(parameters[i + 1]);
			xoverSet.add(xOper);
		}
	}

	/**
	 * Method accessed only from this method that parses parameter mut-set which
	 * contains information about mutation operators. Method makes the mutation
	 * operators set. They are created from config property file.
	 * <ul>
	 * <li>If there is some problem with initialization than the excepcion is
	 * thrown.</li>
	 * <li>If the mutation is dependent on weka and we are not using weka than
	 * exception is thrown.</li>
	 * <li>If the mutation isn't compatible with weka and we are using weka than
	 * exception is thrown.</li>
	 * </ul>
	 * 
	 * @throws OperatorStringFormatException
	 *             if there is bad format for operators
	 * @throws NotDefClassException
	 *             if there isn't crossover operator for config
	 * @throws CompatibleWekaException
	 *             if the crossover operator is dependent on weka or isn't
	 *             compatible with weka when we use weka.
	 */
	private void makeMutationOperatorSet() throws Exception {
		mutSet.clear();
		String[] parameters = mutOpString.split(Utils.oDELIM);
		if (parameters.length % 2 != 0) {
			// bad format
			throw new OperatorStringFormatException(
					TextResource.getString("eOperFormat"));
		}

		HashMap<String, Class<? extends Operator<? extends Individual>>> h = PluginManager.mutOper;
		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "") {
				mutSet.clear();
				// blank alias
				throw new OperatorStringFormatException(
						TextResource.getString("eOperFormat"));
			}

			Class<? extends Operator<? extends Individual>> mutOperClass = h
					.get(parameters[i]);

			if (mutOperClass == null) {
				LOG.log(Level.SEVERE, String.format(
						TextResource.getString(TextKeys.eNotDefClass),
						parameters[0]));
				// not defined class for pop init
				throw new NotDefClassException(String.format(
						TextResource.getString(TextKeys.eNotDefClass),
						parameters[0]));
			}

			Operator<? extends Individual> genMutOper = mutOperClass
					.newInstance();
			if (genMutOper.getIndividualClassType() != TreeIndividual.class) {
				throw new TypeParameterException(String.format(
						TextResource.getString(TextKeys.eTypeParameter),
						genMutOper.getIndividualClassType().getName(),
						TreeIndividual.class.getName()));
			}

			// we checked the type, so the retype is always correct
			@SuppressWarnings("unchecked")
			Operator<TreeIndividual> mutOper = (Operator<TreeIndividual>) genMutOper;

			if (mutOper.isWekaDependent() && !isWeka) {
				LOG.log(Level.SEVERE, String.format(
						TextResource.getString(TextKeys.eWekaDependency),
						mutOperClass.getName()));
				throw new CompatibleWekaException(String.format(
						TextResource.getString(TextKeys.eWekaDependency),
						mutOperClass.getName()));
			}

			if (!mutOper.isWekaCompatible() && isWeka) {
				LOG.log(Level.SEVERE, String.format(
						TextResource.getString(TextKeys.eWekaCompatibility),
						mutOperClass.getName()));
				throw new CompatibleWekaException(String.format(
						TextResource.getString(TextKeys.eWekaCompatibility),
						mutOperClass.getName()));
			}

			mutOper.setRandomGenerator(new Random(random.nextLong()));
			mutOper.setParam(parameters[i + 1]);
			mutSet.add(mutOper);
		}
	}

	/**
	 * 
	 * Method accessed only from this method that parses parameter IP which
	 * contains information about initial population generator. Parameters are
	 * then set into population initializator.
	 * 
	 * @throws PopulationInitStringFormatException
	 *             if there is bad format for population initializator
	 * @throws NotDefClassException
	 *             if there isn't population initializator for type
	 * @throws IllegalAccessException
	 *             if there is problem with access to class
	 * @throws InstantiationException
	 *             if there is problem with instancing of class
	 */
	private void makePopInitializator() throws InstantiationException,
			IllegalAccessException {
		popInit = null;
		String[] parameters = populationInitString.split(Utils.oDELIM);

		if (parameters.length % 2 != 0) {
			// bad format
			throw new PopulationInitStringFormatException(
					TextResource.getString(TextKeys.eOperFormat));
		}

		if (parameters[0] == "") {
			mutSet.clear();
			// blank alias
			throw new PopulationInitStringFormatException(
					TextResource.getString(TextKeys.eOperFormat));
		}

		// inner error
		Class<? extends PopulationInitializator<? extends Individual>> popInitClass = PluginManager.popInits
				.get(parameters[0]);

		if (popInitClass == null) {
			LOG.log(Level.SEVERE, String.format(
					TextResource.getString(TextKeys.eNotDefClass),
					parameters[0]));
			// not defined class for pop init
			throw new NotDefClassException(String.format(
					TextResource.getString(TextKeys.eNotDefClass),
					parameters[0]));
		}

		PopulationInitializator<? extends Individual> genPopInit = popInitClass
				.newInstance();
		if (!(genPopInit instanceof TreePopulationInitializator)) {
			throw new TypeParameterException(String.format(
					TextResource.getString(TextKeys.eTypeParameter),
					popInitClass.getName(),
					TreePopulationInitializator.class.getName()));
		}

		popInit = (TreePopulationInitializator) genPopInit;

		if (popInit.isWekaCompatible() && !isWeka) {
			LOG.log(Level.SEVERE, String.format(
					TextResource.getString(TextKeys.eWekaCompatibility),
					popInit.getClass().getName()));
			throw new CompatibleWekaException(String.format(
					TextResource.getString(TextKeys.eWekaCompatibility),
					popInit.getClass().getName()));
		}

		if (parameters.length > 1) {
			popInit.setParam(parameters[1]);
		}

		popInit.setPopulationSize(populationSizeInt);
		popInit.setNumOfThreads(genThreadsInt);
	}

	/**
	 * 
	 * Method accessed only from this method that parses parameter ind-gen which
	 * contains information about initial generators. Parameters are transferred
	 * into generator list and then set into population initializator.
	 * 
	 * @throws PopulationInitStringFormatException
	 *             if there is bad format for individual generators
	 * @throws NotDefClassException
	 *             if there isn't population initializator for config
	 * @throws IllegalAccessException
	 *             if there is problem with access to class
	 * @throws InstantiationException
	 *             if there is problem with instancing of class
	 * @throws CompatibleWekaException
	 *             if the generator is dependent on weka
	 */
	private void makeGenerators() throws Exception {
		String[] parameters = indGenString.split(Utils.oDELIM);

		if (popInit == null) {
			throw new NotInitializedFieldException();
		}

		if (parameters.length % 2 != 0) {
			throw new PopulationInitStringFormatException();
		}

		HashMap<String, Class<? extends Generator<? extends Individual>>> h = PluginManager.gens;
		ArrayList<TreeGenerator> genList = new ArrayList<>();

		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "") {
				// bad format
				throw new PopulationInitStringFormatException(
						TextResource.getString(TextKeys.eGenFormat));
			}

			// inner error
			Class<? extends Generator<? extends Individual>> genClass = h
					.get(parameters[i]);

			if (genClass == null) {
				LOG.log(Level.SEVERE, String.format(
						TextResource.getString(TextKeys.eNotDefClass),
						parameters[0]));
				// not defined class for pop init
				throw new NotDefClassException(String.format(
						TextResource.getString(TextKeys.eNotDefClass),
						parameters[0]));
			}

			Generator<? extends Individual> genGenerator = genClass
					.newInstance();
			if (!(genGenerator instanceof TreeGenerator)) {
				throw new TypeParameterException(String.format(
						TextResource.getString(TextKeys.eTypeParameter),
						genClass.getName(), TreeGenerator.class.getName()));
			}

			// we checked the type, so the retype is always correct
			TreeGenerator generator = (TreeGenerator) genGenerator;

			if (generator.isWekaDependent() && !isWeka) {
				LOG.log(Level.SEVERE, String.format(
						TextResource.getString(TextKeys.eWekaCompatibility),
						generator.getClass().getName()));
				throw new CompatibleWekaException(String.format(
						TextResource.getString(TextKeys.eWekaCompatibility),
						generator.getClass().getName()));
			}

			generator.setParam(parameters[i + 1]);
			genList.add(generator);
		}

		popInit.setGenerator(genList);
	}

	/**
	 * Gets the seed used by pseudo random generator.
	 * 
	 * @return seed
	 */
	public long getSeed() {
		return seed;
	}

	/**
	 * Gets the random object used by classifier.
	 * 
	 * @return Random object
	 */
	public Random getRandom() {
		return random;
	}

	/**
	 * Gets the parameters for data argument
	 * 
	 * @return parameters in string format
	 */
	public String getData() {
		return dataString;
	}

	/**
	 * Gets the parameters for classify argument
	 * 
	 * @return parameters in string format
	 */
	public int getClassify() {
		return classify;
	}

	/**
	 * Gets the number of threads used by generators
	 * 
	 * @return number of threads
	 */
	public int getGeneratorThreads() {
		return genThreadsInt;
	}

	/**
	 * Gets the number of threads used by operators
	 * 
	 * @return number of threads
	 */
	public int getOperatorThreads() {
		return operThreadsInt;
	}

	/**
	 * Gets the number of threads used by fitness functions
	 * 
	 * @return number of threads
	 */
	public int getFitnessThreads() {
		return fitThreadsInt;
	}

	/**
	 * Gets the count of generations to be cycled throught by evolution
	 * algorithm.
	 * 
	 * @return number of generations
	 */
	public int getNumberOfGenerations() {
		return numOfGensInt;
	}

	/**
	 * Gets the elitism rate.
	 * 
	 * @return elitism rate
	 */
	public double getElitism() {
		return elitismRateDouble;
	}

	/**
	 * List of tree individual mutation operators used by genetic algorithm.
	 * 
	 * @return list of mutation operators
	 */
	public ArrayList<Operator<TreeIndividual>> getMutSet() {
		return mutSet;
	}

	/**
	 * Gets the mutation operators config.
	 * 
	 * @return mutation operators
	 */
	public String getMutString() {
		return mutOpString;
	}

	/**
	 * List of tree individual crossover operators used by genetic algorithm.
	 * 
	 * @return list of crossover operators
	 */
	public ArrayList<Operator<TreeIndividual>> getXoverSet() {
		return xoverSet;
	}

	/**
	 * Gets the crossover operators config.
	 * 
	 * @return crossover operators
	 */
	public String getXoverString() {
		return xOverOpString;
	}

	/**
	 * Gets the size of the population
	 * 
	 * @return population size
	 */
	public int getPopulationSize() {
		return populationSizeInt;
	}

	/**
	 * Gets the individual generator argument string
	 * 
	 * @return individual generator string
	 */
	public String getIndividualGeneratorString() {
		return indGenString;
	}

	/**
	 * Gets the population initializator argument string
	 * 
	 * @return population initializator string
	 */
	public String getPopInitString() {
		return populationInitString;
	}

	/**
	 * Gets the selector argument string
	 * 
	 * @return selector string
	 */
	public String getSelectorsString() {
		return selectorsString;
	}

	/**
	 * Gets the environmental selector argument string
	 * 
	 * @return environmental selector string
	 */
	public String getEnvSelectorsString() {
		return envSelectorsString;
	}

	/**
	 * Gets the tree population initializator argument string
	 * 
	 * @return population initializator string
	 */
	public TreePopulationInitializator getPopInitializator() {
		return popInit;
	}

	/**
	 * Gets the fitness comparator
	 * 
	 * @return fitness comparator
	 */
	public FitnessComparator<TreeIndividual> getFitnessComparator() {
		return fitComp;
	}

	/**
	 * Gets the fitness comparator argument
	 * 
	 * @return fitness comparator string
	 */
	public String getFitnessComparatorString() {
		return fitCompString;
	}

	/**
	 * Gets the fitness function list
	 * 
	 * @return fitness functions as array list
	 */
	public ArrayList<FitnessFunction<TreeIndividual>> getFitnessFunctions() {
		return fitFuncs;
	}

	/**
	 * Gets the fitness function argument
	 * 
	 * @return fitness functions string
	 */
	public String getFitnessFunctionsString() {
		return fitFunctionsString;
	}

	/**
	 * Gets the best starting tree individual from starting population.
	 * 
	 * @return best starting tree individual
	 */
	public TreeIndividual getStartIndividual() {
		return startIndividual;
	}

	/**
	 * Gets the n best tree individuals created with genetic algorithm
	 * 
	 * @return n best tree individuals
	 */
	public ArrayList<TreeIndividual> getBestIndividuals() {
		return theBestIndividuals;
	}

	/**
	 * Gets the whole final population created with genetic algorithms.
	 * 
	 * @return tree individuals from last generations
	 */
	public ArrayList<TreeIndividual> getActualIndividuals() {
		return ea.getActualPopulation().getIndividuals();
	}

	/**
	 * Gets the working data which we work with
	 * 
	 * @return data with instances
	 */
	public Data getWorkData() {
		return workData;
	}

	/**
	 * Set seed for this run of classifier. Seed is further set from
	 * buildClassifier function into Random object. It even serves purpose of
	 * parameter filling in weka environment (called from weka gui parameter
	 * functions).
	 * 
	 * @param seed
	 *            Seed to be set
	 */
	public void setSeed(long seed) {
		this.seed = seed;
		random.setSeed(seed);
	}

	/**
	 * Sets the data argument string
	 * 
	 * @param dataString
	 *            argument
	 */
	public void setData(String dataString) {
		this.dataString = dataString;
	}

	/**
	 * Set the how many of the best individuals are used in classification.
	 * 
	 * @param classify
	 *            as number of instances used in classification
	 */
	public void setClassify(int classify) {
		this.classify = classify;
	}

	/**
	 * Sets the fitness threads
	 * 
	 * @param fitThreads
	 *            number of threads
	 */
	public void setFitnessThreads(int fitThreads) {
		this.fitThreadsInt = fitThreads;
	}

	/**
	 * Sets the generator threads
	 * 
	 * @param genThreads
	 *            number of threads
	 */
	public void setGeneratorThreads(int genThreads) {
		this.genThreadsInt = genThreads;
	}

	/**
	 * Sets the operator threads
	 * 
	 * @param operThreads
	 *            number of threads
	 */
	public void setOperatorThreads(int operThreads) {
		this.operThreadsInt = operThreads;
	}

	/**
	 * Sets the number of generations to be cycled through with genetic
	 * algorithm.
	 * 
	 * @param numberOfGenerations
	 *            for genetic algorithm
	 */
	public void setNumberOfGenerations(int numberOfGenerations) {
		this.numOfGensInt = numberOfGenerations;
	}

	/**
	 * Sets the fitness functions argument string
	 * 
	 * @param fitFuncsString
	 *            argument
	 */
	public void setFitFuncsString(String fitFuncsString) {
		this.fitFunctionsString = fitFuncsString;
	}

	/**
	 * Sets the fitness comparator argument string
	 * 
	 * @param fitCompString
	 *            argument
	 */
	public void setFitCompString(String fitCompString) {
		this.fitCompString = fitCompString;
	}

	/**
	 * Sets the mutation operators config.
	 * 
	 * @param mutString
	 *            mutation operators string
	 */
	public void setMutString(String mutString) {
		this.mutOpString = mutString;
	}

	/**
	 * Sets the elitism rate
	 * 
	 * @param elitism
	 *            rate
	 */
	public void setElitism(double elitism) {
		this.elitismRateDouble = elitism;
	}

	/**
	 * Sets the crossover operators config.
	 * 
	 * @param xoverString
	 *            crossover operators string
	 */
	public void setXoverString(String xoverString) {
		this.xOverOpString = xoverString;
	}

	/**
	 * Sets the fixed population size
	 * 
	 * @param populationSize
	 *            of the population from algorithm
	 */
	public void setPopulationSize(int populationSize) {
		this.populationSizeInt = populationSize;
	}

	/**
	 * Sets the individual generator string argument
	 * 
	 * @param indGenString
	 *            argument
	 */
	public void setIndividualGeneratorString(String indGenString) {
		this.indGenString = indGenString;
	}

	/**
	 * Sets the population initializator string argument
	 * 
	 * @param popInitString
	 *            argument
	 */
	public void setPopInitString(String popInitString) {
		this.populationInitString = popInitString;
	}

	/**
	 * Sets the selector string argument
	 * 
	 * @param selectorString
	 *            argument
	 */
	public void setSelectorsString(String selectorString) {
		this.selectorsString = selectorString;
	}

	/**
	 * Sets the environmental selector string argument
	 * 
	 * @param envSelectorString
	 *            argument
	 */
	public void setEnvSelectorsString(String envSelectorString) {
		this.envSelectorsString = envSelectorString;
	}

	/**
	 * Sets the population initializator used to initialize starting population.
	 * 
	 * @param popInit
	 *            TreePopulationInitializator
	 */
	public void setPopInitializator(TreePopulationInitializator popInit) {
		this.popInit = popInit;
	}

}
