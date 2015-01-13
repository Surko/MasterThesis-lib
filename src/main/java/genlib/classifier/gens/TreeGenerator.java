package genlib.classifier.gens;

import genlib.classifier.popinit.PopulationInitializator;
import genlib.classifier.splitting.SplitCriteria;
import genlib.evolution.individuals.Individual;
import genlib.evolution.individuals.TreeIndividual;

public abstract class TreeGenerator implements PopGenerator<TreeGenerator> {
	
	/** for serialization */
	private static final long serialVersionUID = -7452763095100232747L;

	/**
	 * Different TreeGenerators introduced up to this date.
	 * @see PopulationInitializator
	 * @author kirrie	 
	 */
	public enum TreeGenerators {
		SSGEN,
		J48;
	}
	
	/** Splitting criteria at nodes for attributes, computes information gain, etc... */
	protected SplitCriteria splitCriteria;
	/** Max depth of the generated trees. for example simple stumps have depth = 1 */
	protected int genDepth;
	/** Individuals created with this generator */
	protected TreeIndividual[] individuals;
	/** Object of the part of instances */
	protected Object data;
	/** Generator where all individuals will gather */
	protected TreeGenerator gatherGen; 
	/** Count of individuals in array */
	protected int individualCount = 0;	
	
	@Override
	public void setInstances(Object data) {
		this.data = data;
	}
	
	/**
	 * Depth of generated trees. (for example: stumps depth = 1)
	 * @return depth of trees
	 */
	public int getGeneratorDepth() {
		return genDepth;
	}
	
	public SplitCriteria getSplitCriteria() {
		return splitCriteria;
	}
	
	public void setSplitCriteria(SplitCriteria splitCriteria) {
		this.splitCriteria = splitCriteria;		
	}
	
	public boolean hasSplitCriteria() {
		return splitCriteria != null;
	}		
	
	public TreeIndividual[] getIndividuals() {
		return individuals;
	}		
	
	@Override
	public void setGatherGen(TreeGenerator gatherGen) {
		this.gatherGen = gatherGen;		
	}

	public void setIndividuals(TreeIndividual[] individuals) {
		this.individuals = individuals;
	}	
	
	protected synchronized int incCountOfIndividuals(int count) {
		int returnValue = individualCount;
		individualCount += count;
		return returnValue;
	}
	
	/**
	 * Default run method for TreeGenerator that is executed with ExecutorService from PopInitializator.
	 * Its work is to createPopulation from provided instances. After that TreeIndividuals will be set 
	 * and should be copied into gatherer. Starting point of where we will copy individuals is 
	 * returned from synchronized method incCountOfIndividuals that works inside gatherer.
	 * Threads should not interfere with each other and for each thread there should be different
	 * value of startIndex   
	 */
	@Override
	public void run() {
		try {
			/** Creation of population with setting of individualCount */
			createPopulation();
			/** Synchronized increase of gatherGen individuals and return of copy starting point */
			int startIndex = gatherGen.incCountOfIndividuals(individualCount);
			/** Copy all individuals into gatherGen individuals from startIndex */
			System.arraycopy(individuals, 0, gatherGen.individuals, startIndex, individualCount);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
