package genlib.classifier;

import genlib.classifier.gens.TreeGenerator;
import genlib.classifier.popinit.TreePopulationInitializator;
import genlib.classifier.weka.WekaClassifierExtension;
import genlib.configurations.Config;
import genlib.evolution.EvolutionAlgorithm;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.fitness.comparators.FitnessComparator.FitCompare;
import genlib.evolution.fitness.comparators.ParetoFitnessComparator;
import genlib.evolution.fitness.comparators.PriorityFitnessComparator;
import genlib.evolution.fitness.comparators.SingleFitnessComparator;
import genlib.evolution.fitness.comparators.WeightedFitnessComparator;
import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.operators.Operator;
import genlib.evolution.population.IPopulation;
import genlib.evolution.population.Population;
import genlib.evolution.selectors.Selector;
import genlib.exceptions.CompatibleWekaException;
import genlib.exceptions.NotDefClassException;
import genlib.exceptions.NotInitializedFieldException;
import genlib.exceptions.NumericHandleException;
import genlib.exceptions.format.FitCompStringFormatException;
import genlib.exceptions.format.FitnessStringFormatException;
import genlib.exceptions.format.OperatorStringFormatException;
import genlib.exceptions.format.PopulationInitStringFormatException;
import genlib.exceptions.format.PopulationTypeStringFormatException;
import genlib.exceptions.format.SelectorStringFormatException;
import genlib.locales.TextKeys;
import genlib.locales.TextResource;
import genlib.structures.Data;
import genlib.structures.data.GenLibInstances;
import genlib.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import weka.core.Instances;

public class EvolutionTreeClassifier implements Serializable,
		WekaClassifierExtension, Classifier {
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

	public EvolutionTreeClassifier(boolean isWeka) throws Exception {
		this.c = Config.getInstance();
		this.isWeka = isWeka;
		this.fitFuncs = new ArrayList<>();
		this.mutSet = new ArrayList<>();
		this.xoverSet = new ArrayList<>();
		this.selectors = new ArrayList<>();
		this.envSelectors = new ArrayList<>();
		this.random = Utils.randomGen;
	}

	@Override
	public void buildClassifier(Instances data) throws Exception {
		random.setSeed(c.getSeed());
		// making properties from string
		makePropsFromString(data.classAttribute().isNumeric());
		// Shuffle data
		data.randomize(random);
		// create adapter Data around instances
		Data workData = new Data(data);
		// setting additional params for created fields
		setAdditionalParams(workData);

		popInit.setRandomGenerator(new Random(random.nextLong()));
		popInit.setInstances(workData);
		popInit.initPopulation();
		// Population object that contains tree individuals from population
		// initializator
		IPopulation<TreeIndividual> population = makePopulation();

		// Evolution algorithm that evolves population of tree individuals.
		ea = new EvolutionAlgorithm<>(workData, population,
				c.getPopulationSize());
		// original basic individuals which were used to create population
		ea.setPopInit(popInit);
		// number of generations to be run through
		ea.setNumberOfGenerations(c.getNumberOfGenerations());
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
		ea.setElitism(c.getElitismRate());
		// run population evolving
		ea.run();

		TreeIndividual theBest = population.getBestIndividual();
	}

	@Override
	public void buildClassifier(GenLibInstances data) throws Exception {

	}

	public void setAdditionalParams(Data data) {
		// set the data for fitness functions
		for (FitnessFunction<TreeIndividual> function : fitFuncs) {
			function.setData(data);
		}
	}

	@Override
	public void makePropsFromString(boolean isNumeric) throws Exception {
		if (c.getFitnessFunctions() != null) {
			makeFitnessFunctionsSet(isNumeric);
		}
		if (c.getFitnessComparator() != null) {
			makeFitnessCompSet();
		}
		if (c.getXoverOperators() != null) {
			makeXoverOperatorSet();
		}
		if (c.getMutationOperators() != null) {
			makeMutationOperatorSet();
		}
		if (c.getPopulationInit() != null) {
			makePopInitializator();
		}
		if (c.getIndGenerators() != null) {
			makeGenerators();
		}
		if (c.getSelectors() != null) {
			makeSelectorSet();
		}
		if (c.getEnvSelectors() != null) {
			makeEnvSelectorSet();
		}
	}
	
	public IPopulation<TreeIndividual> makePopulation() throws Exception {
		String[] parameters = c.getPopulationType().split(Utils.oDELIM);

		if (parameters.length < 2) {
			throw new PopulationTypeStringFormatException(
					TextResource.getString(TextKeys.ePopTypeFormat));
		}

		if (parameters[0].equals(Population.initName)) {
			return new Population<>(popInit.getPopulation(),
					c.getPopulationSize());
		}

		@SuppressWarnings("rawtypes")
		Class<? extends IPopulation> iPopClass = IPopulation.populationTypes.get(parameters[0]);

		if (iPopClass != null) {
			IPopulation<?> population = iPopClass.newInstance();
			return population.makeNewInstance();
		}
		
		return new Population<>(popInit.getPopulation(),
				c.getPopulationSize());
	}

	private void makeSelectorSet() throws Exception {
		String param = c.getSelectors();
		selectors.clear();
		String[] parameters = param.split(Utils.oDELIM);
		if (parameters.length % 2 != 0) {
			// bad format
			throw new SelectorStringFormatException(
					TextResource.getString("eSelFormat"));
		}

		HashMap<String, Class<Selector>> h = Selector.selectors;

		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "") {
				selectors.clear();
				// blank alias
				throw new Exception();
			}

			Selector selector = h.get(parameters[i]).newInstance();
			selector.setRandomGenerator(new Random(random.nextLong()));
			selector.setParam(parameters[i + 1]);

			selectors.add(selector);
		}
	}

	private void makeEnvSelectorSet() throws Exception {
		String param = c.getEnvSelectors();
		envSelectors.clear();
		String[] parameters = param.split(Utils.oDELIM);
		if (parameters.length % 2 != 0) {
			// bad format
			throw new SelectorStringFormatException(
					TextResource.getString("eSelFormat"));
		}

		HashMap<String, Class<Selector>> h = Selector.envSelectors;

		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "") {
				envSelectors.clear();
				// blank alias
				throw new Exception();
			}

			Selector selector = h.get(parameters[i]).newInstance();
			selector.setRandomGenerator(new Random(random.nextLong()));
			selector.setParam(parameters[i + 1]);

			envSelectors.add(selector);
		}
	}

	private void makeFitnessCompSet() throws Exception {
		String param = c.getFitnessComparator();
		fitComp = null;
		String[] parameters = param.split(Utils.oDELIM);

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
			if (parameters.length >= 2) {
				fitComp.setParam(parameters[1]);
			} else {
				fitComp.setParam(null);
			}
			break;
		default:
			break;
		}

	}

	private void makeFitnessFunctionsSet(boolean isNumeric) throws Exception {
		String param = c.getFitnessFunctions();
		fitFuncs.clear();
		String[] parameters = param.split(Utils.oDELIM);
		if (parameters.length % 2 != 0) {
			// bad format
			throw new FitnessStringFormatException(
					TextResource.getString("eFitnessFormat"));
		}

		int max = 0;

		HashMap<String, Class<? extends FitnessFunction<TreeIndividual>>> h = FitnessFunction.tFitFuncs;
		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "") {
				fitFuncs.clear();
				// blank alias
				throw new FitnessStringFormatException(
						TextResource.getString("eFitnessFormat"));
			}

			// inner error
			FitnessFunction<TreeIndividual> func = h.get(parameters[i])
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
	 * 
	 * @param param
	 */
	private void makeXoverOperatorSet() throws Exception {
		String param = c.getXoverOperators();
		xoverSet.clear();
		String[] parameters = param.split(Utils.oDELIM);
		if (parameters.length % 2 != 0) {
			// bad format
			throw new OperatorStringFormatException(
					TextResource.getString("eOperFormat"));
		}

		HashMap<String, Class<Operator<TreeIndividual>>> h = Operator.tXOper;
		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "") {
				xoverSet.clear();
				// blank alias
				throw new OperatorStringFormatException(
						TextResource.getString("eOperFormat"));
			}

			// inner error
			Operator<TreeIndividual> func = h.get(parameters[i]).newInstance();
			xoverSet.add(func);
		}
	}

	private void makeMutationOperatorSet() throws Exception {
		String param = c.getMutationOperators();
		mutSet.clear();
		String[] parameters = param.split(Utils.oDELIM);
		if (parameters.length % 2 != 0) {
			// bad format
			throw new OperatorStringFormatException(
					TextResource.getString("eOperFormat"));
		}

		HashMap<String, Class<Operator<TreeIndividual>>> h = Operator.tMOper;
		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "") {
				mutSet.clear();
				// blank alias
				throw new OperatorStringFormatException(
						TextResource.getString("eOperFormat"));
			}

			// inner error
			Operator<TreeIndividual> func = h.get(parameters[i]).newInstance();
			mutSet.add(func);
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
		String popInitString = c.getPopulationInit();
		popInit = null;
		String[] parameters = popInitString.split(Utils.oDELIM);

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
		Class<? extends TreePopulationInitializator> popInitClass = TreePopulationInitializator.treePopInits
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

		popInit = popInitClass.newInstance();

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

		popInit.setPopulationSize(c.getPopulationSize());
		popInit.setNumOfThreads(c.getGenNumOfThreads());
	}

	private void makeGenerators() throws Exception {
		String indGen = c.getPopulationInit();
		String[] parameters = indGen.split(Utils.oDELIM);

		if (popInit == null) {
			throw new NotInitializedFieldException();
		}

		if (parameters.length % 2 != 0) {
			throw new PopulationInitStringFormatException();
		}

		HashMap<String, Class<? extends TreeGenerator>> h = TreeGenerator.treeGens;
		ArrayList<TreeGenerator> genList = new ArrayList<>();

		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "") {
				// bad format
				throw new PopulationInitStringFormatException(
						TextResource.getString("eOperFormat"));
			}

			// inner error
			TreeGenerator generator = h.get(parameters[i]).newInstance();
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

	public int getSeed() {
		return c.getSeed();
	}

	@Override
	public Random getRandom() {
		return random;
	}

	public int getGeneratorThreads() {
		return c.getGenNumOfThreads();
	}

	public int getOperatorThreads() {
		return c.getOperNumOfThreads();
	}

	public int getFitnessThreads() {
		return c.getFitNumOfThreads();
	}

	public int getNumberOfGenerations() {
		return c.getNumberOfGenerations();
	}

	public double getElitism() {
		return c.getElitismRate();
	}

	public ArrayList<Operator<TreeIndividual>> getMutSet() {
		return mutSet;
	}

	public String getMutString() {
		return c.getMutationOperators();
	}

	public ArrayList<Operator<TreeIndividual>> getXoverSet() {
		return xoverSet;
	}

	public String getXoverString() {
		return c.getXoverOperators();
	}

	public int getPopulationSize() {
		return c.getPopulationSize();
	}

	public double getImprovementRate() {
		return improvementRate;
	}

	public String getIndividualGeneratorString() {
		return c.getIndGenerators();
	}

	public String getPopInitString() {
		return c.getPopulationInit();
	}

	public String getSelectorsString() {
		return c.getSelectors();
	}

	public String getEnvSelectorsString() {
		return c.getEnvSelectors();
	}

	public TreePopulationInitializator getPopInitializator() {
		return popInit;
	}

	public FitnessComparator<TreeIndividual> getFitnessComparator() {
		return fitComp;
	}

	public String getFitnessComparatorString() {
		return c.getFitnessComparator();
	}

	public ArrayList<FitnessFunction<TreeIndividual>> getFitnessFunctions() {
		return fitFuncs;
	}

	public String getFitnessFunctionsString() {
		return c.getFitnessFunctions();
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
	public void setSeed(int seed) {
		if (c.getSeed() != seed) {
			c.setSeed(seed);
		}
	}

	public void setFitnessThreads(int fitThreads) {
		c.setFitnessThreads(fitThreads);
	}

	public void setGeneratorThreads(int genThreads) {
		c.setGeneratorThreads(genThreads);
	}

	public void setOperatorThreads(int operThreads) {
		c.setOperatorThreads(operThreads);
	}

	public void setNumberOfGenerations(int numberOfGenerations) {
		c.setNumberOfGenerations(numberOfGenerations);
	}

	public void setFitFuncsString(String fitFuncsString) {
		c.setFitnessFunctions(fitFuncsString);
	}

	public void setFitCompString(String fitCompString) {
		c.setFitnessComparator(fitCompString);
	}

	public void setMutString(String mutString) {
		c.setMutationOperators(mutString);
	}

	public void setElitism(double elitism) {
		c.setElitismRate(elitism);
	}

	public void setXoverString(String xoverString) {
		c.setXoverOperators(xoverString);
	}

	public void setPopulationSize(int populationSize) {
		c.setPopulationSize(populationSize);
	}

	public void setImprovementRate(double improvementRate) {
		this.improvementRate = improvementRate;
	}

	public void setIndividualGeneratorString(String indGenString) {
		c.setIndGenerators(indGenString);
	}

	public void setPopInitString(String popInitString) throws Exception {
		c.setPopulationInit(popInitString);
	}

	public void setSelectorsString(String selectorString) {
		c.setSelectors(selectorString);
	}

	public void setEnvSelectorsString(String envSelectorString) {
		c.setEnvSelectors(envSelectorString);
	}

	public void setPopInitializator(TreePopulationInitializator popInit) {
		this.popInit = popInit;
		c.setPopulationInit(popInit.objectInfo());
	}
}
