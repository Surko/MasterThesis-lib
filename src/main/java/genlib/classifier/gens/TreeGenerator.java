package genlib.classifier.gens;

import java.util.HashMap;

import genlib.classifier.popinit.PopulationInitializator;
import genlib.classifier.popinit.TreePopulationInitializator;
import genlib.classifier.splitting.SplitCriteria;
import genlib.evolution.individuals.TreeIndividual;

public abstract class TreeGenerator implements PopGenerator<TreeIndividual> {
	
	/** for serialization */
	private static final long serialVersionUID = -7452763095100232747L;
	/** loaded population initializators */
	public static final HashMap<String, Class<? extends TreeGenerator>> treeGens = new HashMap<>();
	
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
	protected int genHeight;
	/** Individuals created with this generator */
	protected TreeIndividual[] individuals;
	/** Object of the part of instances */
	protected Object data;
	/** Generator where all individuals will gather */
	protected TreeGenerator gatherGen; 
	/** Count of individuals in array */
	protected int individualCount = 0;		
	/** Population initializator associated with this generator*/ 
	protected TreePopulationInitializator treeInit;
	
	public TreePopulationInitializator getPopulationInitializator() {
		return treeInit;
	}
	
	public void setPopulationInitializator(TreePopulationInitializator treeInit) {
		this.treeInit = treeInit;
	}
	
	@Override
	public void setInstances(Object data) {
		this.data = data;
	}
	
	/**
	 * Depth of generated trees. (for example: stumps depth = 1)
	 * @return depth of trees
	 */
	public int getGeneratorHeight() {
		return genHeight;
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
	public void setGatherGen(PopGenerator<TreeIndividual> gatherGen) {
		if (gatherGen instanceof TreeGenerator)
			this.gatherGen = (TreeGenerator)gatherGen;		
	}

	public void setIndividuals(TreeIndividual[] individuals) {
		this.individuals = individuals;
	}	
	
	@Override
	public void setAdditionalOptions(String[] options) throws Exception {}
	
	protected synchronized int incCountOfIndividuals(int count) {
		int returnValue = individualCount;
		individualCount += count;
		return returnValue;
	}
	
	public abstract TreeGenerator copy();
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
