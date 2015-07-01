package genlib.classifier.common;

import genlib.classifier.gens.PopGenerator;
import genlib.classifier.gens.TreeGenerator;
import genlib.classifier.popinit.PopulationInitializator;
import genlib.classifier.popinit.TreePopulationInitializator;
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

public class EvolutionTreeClassifier implements Serializable {
	private static final Logger LOG = Logger
			.getLogger(EvolutionTreeClassifier.class.getName());
	/** for serialization */
	private static final long serialVersionUID = -762451118775604722L;

	/* Random generator for this run */
	private Random random;
	private Config c;
	private double improvementRate;
	private FitnessComparator<TreeIndividual> fitComp;
	private ArrayList<FitnessFunction<TreeIndividual>> fitFuncs;
	private ArrayList<Operator<TreeIndividual>> mutSet, xoverSet;
	private ArrayList<Selector> selectors, envSelectors;
	private boolean isWeka;
	private TreePopulationInitializator popInit;
	private EvolutionAlgorithm<TreeIndividual> ea;
	private ArrayList<TreeIndividual> theBestIndividuals;
	private TreeIndividual startIndividual;

	private String populationTypeString, populationInitString, indGenString;
	private String selectorsString, envSelectorsString, fitCompString;
	private String fitFunctionsString, xOverOpString, mutOpString, dataString;
	private int classify;
	private int populationSizeInt, numOfGensInt, fitThreadsInt, genThreadsInt,
			operThreadsInt;
	private double elitismRateDouble;
	private long seed;

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

	public void buildClassifier(Instances data) throws Exception {		
		theBestIndividuals = null;
		startIndividual = null;

		// create adapter Data around instances
		Data workData = new Data(data, new Random(random.nextLong()));
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

	public void buildClassifier(GenLibInstances data) throws Exception {
		theBestIndividuals = null;
		startIndividual = null;

		// create adapter Data around instances
		Data workData = new Data(data, new Random(random.nextLong()));
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

	private void commonBuildClassifier(Data workData) throws Exception {
		popInit.setRandomGenerator(new Random(random.nextLong()));
		popInit.setData(workData);
		popInit.initPopulation();

		// Population object that contains tree individuals from population
		// initializator
		IPopulation<TreeIndividual> population = makePopulation();
		startIndividual = population.getSortedIndividuals(fitComp).get(0).copy();

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
		// run population evolving
		ea.run();

		theBestIndividuals = ea.getActualPopulation().getSortedIndividuals(
				fitComp);
	}

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
			fitComp = new PriorityFitnessComparator<>();
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

		HashMap<String, Class<? extends PopGenerator<? extends Individual>>> h = PluginManager.gens;
		ArrayList<TreeGenerator> genList = new ArrayList<>();

		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "") {
				// bad format
				throw new PopulationInitStringFormatException(
						TextResource.getString(TextKeys.eGenFormat));
			}

			// inner error
			Class<? extends PopGenerator<? extends Individual>> genClass = h
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

			PopGenerator<? extends Individual> genGenerator = genClass
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

	public long getSeed() {
		return seed;
	}

	public Random getRandom() {
		return random;
	}

	public String getData() {
		return dataString;
	}

	public int getClassify() {
		return classify;
	}
	
	public int getGeneratorThreads() {
		return genThreadsInt;
	}

	public int getOperatorThreads() {
		return operThreadsInt;
	}

	public int getFitnessThreads() {
		return fitThreadsInt;
	}

	public int getNumberOfGenerations() {
		return numOfGensInt;
	}

	public double getElitism() {
		return elitismRateDouble;
	}

	public ArrayList<Operator<TreeIndividual>> getMutSet() {
		return mutSet;
	}

	public String getMutString() {
		return mutOpString;
	}

	public ArrayList<Operator<TreeIndividual>> getXoverSet() {
		return xoverSet;
	}

	public String getXoverString() {
		return xOverOpString;
	}

	public int getPopulationSize() {
		return populationSizeInt;
	}

	public double getImprovementRate() {
		return improvementRate;
	}

	public String getIndividualGeneratorString() {
		return indGenString;
	}

	public String getPopInitString() {
		return populationInitString;
	}

	public String getSelectorsString() {
		return selectorsString;
	}

	public String getEnvSelectorsString() {
		return envSelectorsString;
	}

	public TreePopulationInitializator getPopInitializator() {
		return popInit;
	}

	public FitnessComparator<TreeIndividual> getFitnessComparator() {
		return fitComp;
	}

	public String getFitnessComparatorString() {
		return fitCompString;
	}

	public ArrayList<FitnessFunction<TreeIndividual>> getFitnessFunctions() {
		return fitFuncs;
	}

	public String getFitnessFunctionsString() {
		return fitFunctionsString;
	}

	public TreeIndividual getStartIndividual() {
		return startIndividual;
	}

	public ArrayList<TreeIndividual> getBestIndividuals() {
		return theBestIndividuals;
	}

	public ArrayList<TreeIndividual> getActualIndividuals() {
		return ea.getActualPopulation().getIndividuals();
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

	public void setFitnessThreads(int fitThreads) {
		this.fitThreadsInt = fitThreads;
	}

	public void setGeneratorThreads(int genThreads) {
		this.genThreadsInt = genThreads;
	}

	public void setOperatorThreads(int operThreads) {
		this.operThreadsInt = operThreads;
	}

	public void setNumberOfGenerations(int numberOfGenerations) {
		this.numOfGensInt = numberOfGenerations;
	}

	public void setFitFuncsString(String fitFuncsString) {
		this.fitFunctionsString = fitFuncsString;
	}

	public void setFitCompString(String fitCompString) {
		this.fitCompString = fitCompString;
	}

	public void setMutString(String mutString) {
		this.mutOpString = mutString;
	}

	public void setElitism(double elitism) {
		this.elitismRateDouble = elitism;
	}

	public void setXoverString(String xoverString) {
		this.xOverOpString = xoverString;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSizeInt = populationSize;
	}

	public void setImprovementRate(double improvementRate) {
		this.improvementRate = improvementRate;
	}

	public void setIndividualGeneratorString(String indGenString) {
		this.indGenString = indGenString;
	}

	public void setPopInitString(String popInitString) throws Exception {
		this.populationInitString = popInitString;
	}

	public void setSelectorsString(String selectorString) {
		this.selectorsString = selectorString;
	}

	public void setEnvSelectorsString(String envSelectorString) {
		this.envSelectorsString = envSelectorString;
	}

	public void setPopInitializator(TreePopulationInitializator popInit) {
		this.popInit = popInit;
	}
}
