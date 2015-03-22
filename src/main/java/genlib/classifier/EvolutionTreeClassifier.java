package genlib.classifier;

import genlib.classifier.gens.TreeGenerator;
import genlib.classifier.popinit.TreePopulationInitializator;
import genlib.classifier.splitting.GainCriteria;
import genlib.classifier.splitting.InformationGainCriteria;
import genlib.classifier.splitting.SplitCriteria;
import genlib.classifier.weka.WekaClassifierExtension;
import genlib.configurations.Config;
import genlib.evolution.EvolutionAlgorithm;
import genlib.evolution.Population;
import genlib.evolution.fitness.FitnessFunction;
import genlib.evolution.fitness.FitnessFunction.FitnessIndeces;
import genlib.evolution.fitness.comparators.FitnessComparator;
import genlib.evolution.fitness.comparators.FitnessComparator.FitCompare;
import genlib.evolution.fitness.comparators.ParetoFitnessComparator;
import genlib.evolution.fitness.comparators.PriorityFitnessComparator;
import genlib.evolution.fitness.comparators.SingleFitnessComparator;
import genlib.evolution.fitness.comparators.WeightedFitnessComparator;
import genlib.evolution.individuals.Individual;
import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.operators.Operator;
import genlib.evolution.selectors.Selector;
import genlib.exceptions.format.FitCompStringFormatException;
import genlib.exceptions.format.FitnessStringFormatException;
import genlib.exceptions.format.OperatorStringFormatException;
import genlib.exceptions.format.SelectorStringFormatException;
import genlib.locales.TextResource;
import genlib.structures.ArrayInstances;
import genlib.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import weka.core.Instances;

public class EvolutionTreeClassifier implements Serializable,WekaClassifierExtension,Classifier{
	private static final Logger LOG = Logger
			.getLogger(EvolutionTreeClassifier.class.getName());	
	/**
	 * for serialization
	 */
	private static final long serialVersionUID = -762451118775604722L;
	/* Random generator for this run */
	private Random random;
	private Config c;
	private double elitism, improvementRate;
	private String mutString, xoverString, popInitString, genString,
	fitFuncsString, fitCompString, selectorString, envSelectorString;
	private FitnessComparator<TreeIndividual> fitComp;
	private ArrayList<FitnessFunction<TreeIndividual>> fitFuncs;
	private ArrayList<Operator<TreeIndividual>> mutSet, xoverSet;
	private ArrayList<Selector> selectors, envSelectors;
	private int populationSize, numberOfGenerations;
	private int seed;
	private boolean isWeka;
	private TreePopulationInitializator popInit;
	private EvolutionAlgorithm<TreeIndividual> ea;

	public EvolutionTreeClassifier(boolean isWeka) throws Exception {
		this.c = Config.getInstance();
		this.isWeka = isWeka;
		this.elitism = c.getElitismRate();
		this.seed = c.getSeed();
		this.populationSize = c.getPopulationSize();
		this.mutString = c.getMutationOperators();
		this.xoverString = c.getXoverOperators();
		this.popInitString = c.getPopulationInit();
		this.genString = c.getGenerators();
		this.fitFuncsString = c.getFitnessFunctions();
		this.fitCompString = c.getFitnessComparator();
		this.selectorString = c.getSelectors();
		this.envSelectorString = c.getEnvSelectors();
		this.fitFuncs = new ArrayList<>();
		this.mutSet = new ArrayList<>();
		this.xoverSet = new ArrayList<>();
		this.selectors = new ArrayList<>();
		this.envSelectors = new ArrayList<>();
		this.random = Utils.randomGen;
	}

	@Override
	public void buildClassifier(Instances data) throws Exception {
		random.setSeed(seed);
		// making properties from string
		makePropsFromString();		
		// Shuffle data
		data.randomize(random);
		// setting additional params for created fields
		setAdditionalParams(data);
		
		popInit.setRandomGenerator(new Random(random.nextLong()));
		popInit.setInstances(data);
		popInit.initPopulation();
		// Population object that contains tree individuals from population
		// initializator
		Population<TreeIndividual> population = new Population<>(
				popInit.getPopulation(), populationSize);
		
		// Evolution algorithm that evolves population of tree individuals.
		ea = new EvolutionAlgorithm<>(population, populationSize);		
		// original basic individuals which were used to create population
		ea.setPopInit(popInit);
		// number of generations to be run through
		ea.setNumberOfGenerations(numberOfGenerations);
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
		ea.setElitism(elitism);
		// run population evolving
		ea.run();

		TreeIndividual theBest = population.getBestIndividual();
	}

	@Override
	public void buildClassifier(ArrayInstances data) throws Exception {

	}

	public void setAdditionalParams(Object data) {
		// set the data for fitness functions
		for  (FitnessFunction<TreeIndividual> function : fitFuncs ) {
			function.setData(data);
		}
	}
	
	@Override
	public void makePropsFromString() throws Exception {
		if (fitFuncsString != null) {
			makeFitnessFunctionsSet(fitFuncsString);
		}	
		if (fitFuncsString != null) {
			makeFitnessCompSet(fitCompString);
		}
		if (xoverString != null) {
			makeXoverOperatorSet(xoverString);
		}
		if (mutString != null) {
			makeMutationOperatorSet(mutString);
		}
		if (popInitString != null) {
			makePopInitializator(popInitString);
		}
		if (selectorString != null) {
			makeSelectorSet(selectorString);
		}
		if (envSelectorString != null) {
			makeEnvSelectorSet(envSelectorString);
		}
	}

	private void makeSelectorSet(String param) throws Exception {
		selectors.clear();		
		String[] parameters = param.split(Utils.pDELIM);
		if (parameters.length % 2 != 0) {
			// bad format
			throw new SelectorStringFormatException(TextResource.getString("eSelFormat"));
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
			selector.setParam(parameters[i+1]);
			
			selectors.add(selector);
		}
	}
	
	private void makeEnvSelectorSet(String param) throws Exception {
		envSelectors.clear();		
		String[] parameters = param.split(Utils.pDELIM);
		if (parameters.length % 2 != 0) {
			// bad format
			throw new SelectorStringFormatException(TextResource.getString("eSelFormat"));
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
			selector.setParam(parameters[i+1]);
			
			envSelectors.add(selector);
		}		
	}
	
	private void makeFitnessCompSet(String param) throws Exception {
		fitComp = null;
		String[] parameters = param.split(Utils.pDELIM);
		
		System.out.println(parameters.length);
		System.out.println(parameters[0]);
		System.out.println(parameters[1]);
		if (parameters.length <= 0 || parameters.length > 2) {
			// bad format
			throw new FitCompStringFormatException(TextResource.getString("eFitCompFormat"));
		}
		
		FitCompare fitCompare = FitCompare.valueOf(parameters[0]);
		
		switch (fitCompare) {
			case PARETO :
				fitComp = new ParetoFitnessComparator<>();
				break;
			case PRIORITY :
				fitComp = new PriorityFitnessComparator<>();
				break;
			case SINGLE :
				fitComp = new SingleFitnessComparator<>();
				break;
			case WEIGHT :
				fitComp = new WeightedFitnessComparator<>(fitFuncs.size());
				if (parameters.length >= 2) {
					fitComp.setParam(parameters[1]);
				} else {
					fitComp.setParam(null);
				}
				break;
			default :
				break;				
		}
		
	}
	
	private void makeFitnessFunctionsSet(String param) throws Exception {	
		fitFuncs.clear();
		String[] parameters = param.split(Utils.pDELIM);
		if (parameters.length % 2 != 0) {
			// bad format
			throw new FitnessStringFormatException(TextResource.getString("eFitnessFormat"));
		}

		int max = 0;
		for (FitnessIndeces fitnessIndex : FitnessIndeces.values()) {
			if (fitnessIndex.getIndex() > max) {
				max = fitnessIndex.getIndex();
			}
		}
		max++;
		
		HashMap<String, Class<FitnessFunction<TreeIndividual>>> h = FitnessFunction.tFitFuncs;
		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "") {
				fitFuncs.clear();
				// blank alias
				throw new FitnessStringFormatException(TextResource.getString("eFitnessFormat"));
			}

			// inner error
			FitnessFunction<TreeIndividual> func = h.get(parameters[i])
					.newInstance();
			// if there's not already defined index than set the index
			if (func.getIndex() == -1) {
				func.setIndex(max++);
			}
			func.setParam(parameters[i+1]);
			fitFuncs.add(func);
		}
		Individual.registeredFunctions = max;
	}

	/**
	 * 
	 * @param param
	 */
	private void makeXoverOperatorSet(String param) throws Exception {
		xoverSet.clear();
		String[] parameters = param.split(Utils.pDELIM);
		if (parameters.length % 2 != 0) {
			// bad format
			throw new OperatorStringFormatException(TextResource.getString("eOperFormat"));
		}

		HashMap<String, Class<Operator<TreeIndividual>>> h = Operator.tXOper;
		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "") {
				xoverSet.clear();
				// blank alias
				throw new OperatorStringFormatException(TextResource.getString("eOperFormat"));
			}

			// inner error
			Operator<TreeIndividual> func = h.get(parameters[i]).newInstance();
			xoverSet.add(func);
		}
	}

	private void makeMutationOperatorSet(String param) throws Exception { 
		mutSet.clear();
		String[] parameters = param.split(Utils.pDELIM);
		if (parameters.length % 2 != 0) {
			// bad format
			throw new OperatorStringFormatException(TextResource.getString("eOperFormat"));
		}

		HashMap<String, Class<Operator<TreeIndividual>>> h = Operator.tMOper;		
		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "") {
				mutSet.clear();
				// blank alias
				throw new OperatorStringFormatException(TextResource.getString("eOperFormat"));
			}

			// inner error
			Operator<TreeIndividual> func = h.get(parameters[i]).newInstance();
			mutSet.add(func);
		}
	}

	/**
	 * Method accessed only from this method that parses parameter IP which
	 * contains information about initial population generator. Different
	 * informations are stored into Properties object that is further used
	 * inside buildClassifier method.
	 * 
	 * @return Properties file with stored information about initial pop.
	 *         generator
	 */
	private Properties parsePopInitParameters(String popInitString) {
		Properties prop = new Properties();

		String[] parameters = popInitString.split(Utils.pDELIM);

		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "")
				return null;
			prop.put(parameters[i], parameters[i + 1]);
		}

		return prop;
	}
	
	private void makePopInitializator(String popInitString) throws Exception {
		// TODO - exceptions
		final Properties prop = parsePopInitParameters(popInitString);

		if (prop == null || prop.size() == 0) {
			this.popInit = null;
			return;
		} else {
			String popInitType = prop.getProperty("type");
			if (popInitType == null) {
				LOG.log(Level.SEVERE,
						TextResource.getString("eBadPopInitType"));
				// bad pop init type
				throw new Exception();
			}
			Class<? extends TreePopulationInitializator> popInitClass = TreePopulationInitializator.treePopInits
					.get(popInitType);
			if (popInitClass == null) {
				LOG.log(Level.SEVERE, TextResource.getString("eBadPopInit"));
				// not recognized pop init
				throw new Exception();
			}
			popInit = popInitClass.newInstance();
			
			if (popInit.isWekaCompatible() && !isWeka) {
				LOG.log(Level.SEVERE,"");
				// not consistent initializators
				throw new Exception();
			}
			
			setPopInitAttributes(prop);
		}
	}

	private ArrayList<TreeGenerator> makeGenerators(String param) throws Exception {
		// TODO - exceptions
		String[] parameters = param.split(Utils.pDELIM);
		if (parameters.length % 2 != 0) {
			// bad format
			//TODO
			throw new Exception();
		}
		
		HashMap<String, Class<? extends TreeGenerator>> h = TreeGenerator.treeGens;
		ArrayList<TreeGenerator> genList = new ArrayList<>();
		
		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "") {	
				genList.clear();
				// blank alias
				//TODO
				throw new Exception();
			}

			// inner error
			TreeGenerator generator = h.get(parameters[i]).newInstance();
			if (generator.isWekaCompatible() && !isWeka) {
				LOG.log(Level.SEVERE,"");
				// not consistent generators
				//TODO
				throw new Exception();
			}
			generator.setAdditionalOptions(parameters[i+1]
					.split(","));
			genList.add(generator);
		}
				
		return genList;
	}

	private void setPopInitAttributes(final Properties prop) throws Exception {
		// Depth of generated trees in all of the population
		int depth = Integer.parseInt(prop.getProperty("depth", "1"));
		int divideParam = Integer.parseInt(prop.getProperty("divide", "10"));
		boolean recount = Boolean.parseBoolean(prop.getProperty("recount",
				"false"));

		ArrayList<TreeGenerator> treeGen = makeGenerators(genString);

		boolean resample = Boolean.parseBoolean(prop.getProperty("resample",
				"true"));

//		// Split criteria for our tree generator
//		switch (SplitCriteria.Criterias.valueOf(prop.getProperty("criteria",
//				"INFORATIO"))) {
//		case GAINRATIO:
//			treeGen.setSplitCriteria(new GainCriteria());
//			break;
//		case INFORATIO:
//			treeGen.setSplitCriteria(new InformationGainCriteria());
//			break;
//		// here add more measures for splitting
//		}

		popInit.setResample(resample);
		popInit.setAutoDepth(recount);
		// now only one. Can be extended to use more than one generator of population
		popInit.setGenerator(treeGen.get(0));
		popInit.setDepth(depth);
		popInit.setDivideParam(divideParam);
		popInit.setNumOfThreads(c.getGenNumOfThreads());
	}

	public int getSeed() {
		return seed;
	}

	public Random getRandom() {
		return random;
	}

	public int getNumberOfGenerations() {
		return numberOfGenerations;
	}

	public double getElitism() {
		return elitism;
	}

	public ArrayList<Operator<TreeIndividual>> getMutSet() {
		return mutSet;
	}

	public String getMutString() {
		return mutString;
	}

	public ArrayList<Operator<TreeIndividual>> getXoverSet() {
		return xoverSet;
	}

	public String getXoverString() {
		return xoverString;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public double getImprovementRate() {
		return improvementRate;
	}

	public String getPopInitString() {
		return popInitString;
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
		return fitFuncsString;
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
		if (this.seed != seed) {
			this.seed = seed;
		}
	}

	public void setNumberOfGenerations(int numberOfGenerations) {
		this.numberOfGenerations = numberOfGenerations;
	}

	public void setMutString(String mutString) {
		this.mutString = mutString;
	}

	public void setElitism(double elitism) {
		this.elitism = elitism;
	}

	public void setXoverString(String xoverString) {
		this.xoverString = xoverString;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public void setImprovementRate(double improvementRate) {
		this.improvementRate = improvementRate;
	}

	public void setPopInit(String popInitString) throws Exception {
		this.popInitString = popInitString;
	}

	public void setPopInitializator(TreePopulationInitializator popInit) {
		this.popInitString = popInit.objectInfo();
		this.popInit = popInit;
	}
}
