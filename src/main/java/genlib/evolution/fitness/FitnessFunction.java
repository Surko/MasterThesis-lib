package genlib.evolution.fitness;

import genlib.evolution.EvolutionAlgorithm;
import genlib.evolution.Population;
import genlib.evolution.individuals.Individual;
import genlib.evolution.individuals.TreeIndividual;

import java.util.HashMap;

/**
 * Interface for fitness functions that defines basic to-be implemented methods
 * to compute fitness for Individual or individuals inside of a population. </br>
 * Different type of fitnesses are decided by index which fit to static values in this class. 
 * You can look at particular examples of implemented fitness functions
 * @author kirrie 
 * @see TreeAccuracyFitness
 * @see TreeHeightFitness
 *
 */
public abstract class FitnessFunction<T extends Individual> {
	
	public static final HashMap<String, Class<FitnessFunction<TreeIndividual>>> tFitFuncs = new HashMap<>();
	
	protected EvolutionAlgorithm<T> ea;
	protected int index;
	/** index of accuracy fitness in an individual fitness array*/ 
	public static final int TREE_ACCURACY = 0;
	/** 
	 * Index of tree height fitness in an individual fitness array.
	 * It is same as for tree depth because it's almost the same.
	 */  
	public static final int TREE_HEIGHT = 1;
	/** 
	 * Index of tree depth fitness in an individual fitness array.
	 * It is same as for tree height because it's almost the same.
	 */ 
	public static final int TREE_DEPTH = 1;
	
	/**
	 * Abstract method which should compute fitness for specific class that 
	 * you create. You can look at particular examples of implemented fitness functions
	 * and their compute fitness method. Usually this method contains some type of 
	 * differentiation of which data we use (weka or built-in type). Other thing you can take 
	 * into consideration is that individual saves his state of changing from last time, so it can be utilized 
	 * for speeding up running time of computation.
	 * @param individual individual for which we compute fitness
	 * @return fitness value for an individual
	 * @see TreeAccuracyFitness#computeFitness(genlib.evolution.individuals.TreeIndividual)
	 * @see TreeHeightFitness#computeFitness(genlib.evolution.individuals.TreeIndividual)
	 */
	public abstract double computeFitness(T individual);

	public void computeFitness(Population<T> population) {		
		for (T individual : population.getIndividuals())
			computeFitness(individual);
	}

	public abstract Class<T> getIndividualClassType();
	
	public void setEvolutionAlgorithm(EvolutionAlgorithm<T> ea) {
		this.ea = ea;
	}
	
	public int getIndex() {
		return index;
	}	
	
}
