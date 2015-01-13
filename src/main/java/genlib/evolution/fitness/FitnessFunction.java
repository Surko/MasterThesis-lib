package genlib.evolution.fitness;

import genlib.evolution.Population;
import genlib.evolution.individuals.Individual;

/**
 * Interface for fitness functions that defines basic to-be implemented methods
 * to compute fitness for Individual or individuals inside of a population.
 * 
 * @author kirrie
 *
 */
public abstract class FitnessFunction<T extends Individual> {	
	
	public static final int ACCURACY = 0;
	public static final int TREE_SIZE_INDEX = 1;
	
	public abstract void computeFitness(T individual);

	public void computeFitness(Population<T> population) {		
		for (T individual : population.getIndividuals())
			computeFitness(individual);
	}

}
