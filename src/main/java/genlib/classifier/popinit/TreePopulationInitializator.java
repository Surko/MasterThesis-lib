package genlib.classifier.popinit;

import genlib.classifier.gens.PopGenerator;
import genlib.classifier.gens.TreeGenerator;
import genlib.evolution.individuals.TreeIndividual;

import java.util.HashMap;
import java.util.Random;

public abstract class TreePopulationInitializator implements PopulationInitializator<TreeIndividual> {
	
	/** for serialization */
	private static final long serialVersionUID = 7222530982342870829L;
	/** loaded population initializators */
	public static final HashMap<String, Class<? extends TreePopulationInitializator>> treePopInits = new HashMap<>();
	/** depth of generated trees. */
	protected int maxDepth;
	/** number of division of trainin data */
	protected int divideParam;
	/** only resampling instead of dividing */
	protected boolean resample;
	/** recounting of depth inside trees */
	protected boolean autoDepth;
	/** Individuals that makes this population */
	protected TreeIndividual[] population;
	/** Number of threads that will be creating population */
	protected int nThreads;

	/** Generator of tree population. It contains all of the generated trees that are used to combine. */
	protected TreeGenerator gen;
	/** Random seeded object for this run of algorithm. Default from Utils. Can be changed. */
	protected Random random;
	/** Object of all instances */
	protected Object data;
	/** Final population size */
	protected int popSize;
	/** Index of attribute values to access correct array values */
	protected HashMap<String, Integer>[] attrValueIndexMap;
	/** Index of attribute to access correct attribute from String */
	protected HashMap<String,Integer> attrIndexMap;
	
	/**
	 * Method that returns population of generated individuals from
	 * TreeGenerator.
	 */
	public TreeIndividual[] getPopulation() {
		return population;
	}

	@Override
	public TreeIndividual[] getOriginPopulation() {
		return gen.getIndividuals();
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
	
	public boolean getAutoDepth() {
		return autoDepth;
	}

	@Override
	public TreeGenerator getGenerator() {
		return gen;
	}

	public HashMap<String, Integer>[] getAttrValueIndexMap() {
		return attrValueIndexMap;
	}
	
	public HashMap<String, Integer> getAttrIndexMap() {
		return attrIndexMap;
	}
	
	public boolean isResampling() {
		return resample;
	}
	
	@Override
	public int getPopulationSize() {
		return popSize;
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

	public void setAutoDepth(boolean autoDepth) {
		this.autoDepth = autoDepth;
	}
	
	@Override
	public void setGenerator(PopGenerator<TreeIndividual> gen) {
		if (gen instanceof TreeGenerator) {
			this.gen = (TreeGenerator)gen;
		}
	}

	@Override
	public void setInstances(Object data) {
		this.data = data;
	}

	@Override
	public void setPopulationSize(int popSize) {
		this.popSize = popSize;
	}

	public void setNumOfThreads(int nThreads) {
		this.nThreads = nThreads;
	}

	
	
	
}
