package genlib.classifier;

import genlib.classifier.gens.SimpleStumpGenerator;
import genlib.classifier.gens.TreeGenerator;
import genlib.classifier.gens.TreeGenerator.TreeGenerators;
import genlib.classifier.gens.WekaJ48TreeGenerator;
import genlib.classifier.gens.WekaSimpleStumpGenerator;
import genlib.classifier.popinit.CompletedTrees;
import genlib.classifier.popinit.PopulationInitializator.Type;
import genlib.classifier.popinit.RandomStumpCombinator;
import genlib.classifier.popinit.TreePopulationInitializator;
import genlib.classifier.popinit.WekaCompletedTrees;
import genlib.classifier.popinit.WekaRandomStumpCombinator;
import genlib.classifier.splitting.GainCriteria;
import genlib.classifier.splitting.InformationGainCriteria;
import genlib.classifier.splitting.SplitCriteria;
import genlib.configurations.Config;
import genlib.evolution.EvolutionAlgorithm;
import genlib.evolution.Population;
import genlib.evolution.individuals.TreeIndividual;
import genlib.evolution.operators.DefaultTreeCrossover;
import genlib.evolution.operators.DefaultTreeMutation;
import genlib.evolution.operators.Operator;
import genlib.evolution.operators.Operator.MutationOperators;
import genlib.evolution.operators.Operator.XoverOperators;
import genlib.evolution.selectors.Tournament;
import genlib.locales.PermMessages;
import genlib.structures.ArrayInstances;
import genlib.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import weka.core.Instances;

public class EvolutionTreeClassifier implements Serializable {

	/**
	 * for serialization
	 */
	private static final long serialVersionUID = -762451118775604722L;
	/* Random generator for this run */
	private Random random;
	private double elitism, improvementRate;
	private String mutString, xoverString, popInitString;
	private ArrayList<Operator<TreeIndividual>> mutSet, xoverSet;
	private int populationSize, numberOfGenerations;
	private int seed;
	private boolean isWeka;
	private TreePopulationInitializator popInit;
	private EvolutionAlgorithm<TreeIndividual> ea;

	public EvolutionTreeClassifier(boolean isWeka) throws Exception {
		Config c = Config.getInstance();
		this.isWeka = isWeka;
		this.elitism = c.getElitismRate();
		this.seed = c.getSeed();
		this.populationSize = c.getPopulationSize();
		makeMutationOperatorSet(c.getMutationOperators());
		makeXoverOperatorSet(c.getXoverOperators());
		makePopInitializator(c.getPopulationInit());
		this.random = Utils.randomGen;
	}

	public void buildClassifier(Instances data) throws Exception {
		random.setSeed(seed);
		// Shuffle data
		data.randomize(random);

		popInit.setInstances(data);
		popInit.initPopulation();
		// Population object that contains tree individuals from population
		// initializator
		Population<TreeIndividual> population = new Population<>(
				popInit.getPopulation(), populationSize);

		// Evolution algorithm that evolves population of tree individuals.
		ea = new EvolutionAlgorithm<>(population, populationSize);
		ea.setNumberOfGenerations(numberOfGenerations);
		// mates are selected with selector
		ea.addSelector(new Tournament());
		// mates are crossed with operators inside this list
		ea.setCrossOperators(null);
		// mates are mutated with operators inside this list
		ea.setMutationOperators(null);
		// mates are evaluated with fitness functions inside this list
		ea.setFitnessFunctions(null);
		// percentage of parents that stay into next generation
		ea.setElitism(elitism);

		// run population evolving
		ea.run();

		population.getBestIndividual();
	}

	public void buildClassifier(ArrayInstances data) throws Exception {

	}

	/**
	 * 
	 * @param param
	 */
	private void makeXoverOperatorSet(String param) {
		xoverSet = new ArrayList<>();

		String[] parameters = param.split("[=;]");

		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "") {
				xoverSet = null;
				return;
			}
			XoverOperators xoper = XoverOperators.valueOf(parameters[i]);
			switch (xoper) {
			case DEFAULT:
				xoverSet.add(new DefaultTreeCrossover());
				break;
			}
		}
	}

	private void makeMutationOperatorSet(String param) {
		mutSet = new ArrayList<>();

		String[] parameters = param.split("[=;]");

		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "") {
				mutSet = null;
				return;
			}
			MutationOperators moper = MutationOperators.valueOf(parameters[i]);
			switch (moper) {
			case DEFAULT:
				mutSet.add(new DefaultTreeMutation());
				break;
			}
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

		String[] parameters = popInitString.split("[=;]");

		for (int i = 0; i < parameters.length; i += 2) {
			if (parameters[i] == "")
				return null;
			prop.put(parameters[i], parameters[i + 1]);
		}

		return prop;
	}

	private void makePopInitializator(String popInitString) throws Exception {
		final Properties prop = parsePopInitParameters(popInitString);

		if (prop == null || prop.size() == 0) {
			this.popInit = null;
			return;
		} else {
			Type popInitType = Type.valueOf(prop.getProperty("type",
					Type.DECISION_STUMP.name()));
			if (popInitType == null)
				return;
			switch (popInitType) {
			// simple combination of stumps into tree.
			case DECISION_STUMP:
				setDecisionStump(prop);
				break;
			case TREE:
				setCompletedTrees(prop);
				break;
			// here add more population initializators
			}
		}
	}

	private TreeGenerator setGenerators(Properties prop) throws Exception {
		// TreeGenerator for our stump population
		// treegenerators should be generating trees of depth 1 because those are stumps
		switch (TreeGenerators.valueOf(prop.getProperty("gen","SSGEN"))) {		
		case J48 :
			if (isWeka) {
				String[] options = prop.getProperty("param","-C 0.25 -M 2").split(" ");
				return new WekaJ48TreeGenerator(options);
			} else {
				throw new Exception(PermMessages._exc_nonwsupp);
			}			
		case SSGEN :
			if (isWeka) {
				return new WekaSimpleStumpGenerator();
			} else {
				return new SimpleStumpGenerator();
			}			
			// here add more stump treegenerators 
		}
		return null;
	}

	private void setCompletedTrees(final Properties prop) throws Exception {
		int depth = Integer.parseInt(prop.getProperty("depth", "1"));
		int divideParam = Integer.parseInt(prop.getProperty("divide", "10"));

		TreeGenerator treeGen = setGenerators(prop);
		CompletedTrees tree = null;
		
		if (isWeka) {
			tree = new WekaCompletedTrees();
		} else {
			tree = new CompletedTrees();			
		}
		
		boolean resample = Boolean.parseBoolean(prop.getProperty("resample",
				"true"));
		
		tree.setResample(resample);
		tree.setGenerator(treeGen);
		tree.setDepth(depth);
		tree.setDivideParam(divideParam);
		
		popInit = tree;
	}

	private void setDecisionStump(final Properties prop) throws Exception {
		// Depth of generated trees in all of the population
		int depth = Integer.parseInt(prop.getProperty("depth", "1"));
		int divideParam = Integer.parseInt(prop.getProperty("divide", "10"));

		TreeGenerator treeGen = setGenerators(prop);
		RandomStumpCombinator stump = null;
		
		if (isWeka) {
			stump = new WekaRandomStumpCombinator(populationSize, 2, 10, false);
		} else {
			stump = new RandomStumpCombinator(populationSize, 2, 10, false);			
		}

		boolean resample = Boolean.parseBoolean(prop.getProperty("resample",
				"true"));

		// Split criteria for our tree generator
		switch (SplitCriteria.Criterias.valueOf(prop.getProperty("criteria",
				"INFORATIO"))) {
		case GAINRATIO:
			treeGen.setSplitCriteria(new GainCriteria());
			break;
		case INFORATIO:
			treeGen.setSplitCriteria(new InformationGainCriteria());
			break;
		// here add more measures for splitting
		}

		stump.setResample(resample);
		stump.setGenerator(treeGen);
		stump.setDepth(depth);
		stump.setDivideParam(divideParam);

		popInit = stump;
	}

	public Random getRandom() {
		return random;
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		if (this.seed != seed) {
			this.seed = seed;
		}
	}

	public int getNumberOfGenerations() {
		return numberOfGenerations;
	}

	public void setNumberOfGenerations(int numberOfGenerations) {
		this.numberOfGenerations = numberOfGenerations;
	}

	public ArrayList<Operator<TreeIndividual>> getMutSet() {
		return mutSet;
	}

	public String getMutString() {
		return mutString;
	}

	public void setMutString(String mutString) {
		this.mutString = mutString;
		makeMutationOperatorSet(mutString);
	}

	public double getElitism() {
		return elitism;
	}

	public void setElitism(double elitism) {
		this.elitism = elitism;
	}

	public ArrayList<Operator<TreeIndividual>> getXoverSet() {
		return xoverSet;
	}

	public String getXoverString() {
		return xoverString;
	}

	public void setXoverString(String xoverString) {
		this.xoverString = xoverString;
		makeXoverOperatorSet(xoverString);
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public double getImprovementRate() {
		return improvementRate;
	}

	public void setImprovementRate(double improvementRate) {
		this.improvementRate = improvementRate;
	}

	public String getPopInitString() {
		return popInitString == null ? popInit.objectInfo() : popInitString;
	}

	public void setPopInit(String popInitString) throws Exception {
		this.popInitString = popInitString;
		makePopInitializator(popInitString);
	}

	public TreePopulationInitializator getPopInitializator() {
		return popInit;
	}

	public void setPopInitializator(TreePopulationInitializator popInit) {
		this.popInitString = popInit.objectInfo();
		this.popInit = popInit;
	}
}
