package genlib.classifier.popinit;

import genlib.classifier.gens.TreeGenerator;
import genlib.evolution.individuals.TreeIndividual;
import genlib.locales.TextResource;

import java.util.Random;

public abstract class TreePopulationInitializator implements PopulationInitializator<TreeGenerator> {

	/** for serialization */
	private static final long serialVersionUID = 7222530982342870829L;
	/** depth of generated trees. */
	protected int maxDepth;
	/** number of division of trainin data */
	protected int divideParam;
	/** only resampling instead of dividing */
	protected boolean resample;
	/** Individuals that makes this population */
	protected TreeIndividual[] population;

	/** Generator of tree population. It contains all of the generated trees that are used to combine. */
	protected TreeGenerator gen;
	/** Random seeded object for this run of algorithm. Default from Utils. Can be changed. */
	protected Random random;
	/** Object of all instances */
	protected Object data;
	/** Final population size */
	protected int popSize;
	
	/**
	 * Method that returns population of generated individuals from
	 * TreeGenerator.
	 */
	public TreeIndividual[] getPopulation() {
		return population;
	}

	/**
	 * Gets the depth of the combined trees (population trees) from generated trees. 
	 * @return Depth of the trees in population.
	 */
	public int getDepth() {
		return maxDepth;
	}

	public int getDivideParam() {
		return divideParam;
	}

	@Override
	public TreeGenerator getGenerator() {
		return gen;
	}

	public void setRandomGenerator(Random random) {
		this.random = random;
	}

	public void setDepth(int depth) {
		this.maxDepth = depth;
	}

	public void setDivideParam(int divideParam) {
		this.divideParam = divideParam;
	}

	public void setResample(boolean resample) {
		this.resample = resample;
	}

	@Override
	public void setGenerator(TreeGenerator gen) {
		this.gen = gen;
	}

	@Override
	public void setInstances(Object data) {
		this.data = data;
	}

	@Override
	public int getPopulationSize() {
		return popSize;
	}

	@Override
	public void setPopulationSize(int popSize) {
		this.popSize = popSize;
	}
	
}
